/*******************************************************************************
 * Copyright (c) 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.concurrent.internal;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

import com.ibm.ws.concurrent.WSManagedExecutorService;
import com.ibm.ws.ffdc.annotation.FFDCIgnore;
import com.ibm.wsspi.threadcontext.ThreadContext;
import com.ibm.wsspi.threadcontext.ThreadContextDescriptor;

/**
 * Represents an asynchronous method (@Asynchronous).
 * This extension to ManagedCompletableFuture allows join and untimed get
 * to execute inline if it hasn't already started running.
 *
 * @param <T> type of result
 */
public class AsyncMethod<I, T> extends ManagedCompletableFuture<T> {
    /**
     * Completion stage action that runs the asynchronous method.
     */
    private final BiFunction<I, CompletableFuture<T>, T> action;

    /**
     * Thread context that is captured when the asynchronous method is requested,
     * and gets applied to the thread upon which the asynchronous method executes.
     */
    private final ThreadContextDescriptor contextDescriptor;

    /**
     * CDI interceptor's jakarta.interceptor.InvocationContext for the asynchronous method.
     */
    private final I invocation;

    /**
     * Remains false until a thread claims the right to run the asynchronous method.
     */
    private final AtomicBoolean started = new AtomicBoolean(false);

    /**
     * Construct a CompletableFuture that represents the execution of the specified action,
     * to be performed asynchronously to the caller, either on the specified executor
     * or on a thread that attempts join or untimed get.
     *
     * @param <I>        jakarta.interceptor.InvocationContext.
     * @param <T>        type of result.
     * @param invoker    completion stage action that invokes the asynchronous method.
     *                       The first parameter to the BiFunction is the interceptor's InvocationContext.
     *                       The second parameter is the BiFunction is the CompletableFuture that will be returned to the caller.
     *                       The result parameter of the BiFunction is the same CompletableFuture.
     *                       that the Asynchronous method implementation returns to indicate its completion.
     * @param invocation the interceptor's InvocationContext that will be supplied to the BiFunction.
     * @param executor   WSManagedExecutorService to submit the asynchronous method to.
     */
    public AsyncMethod(BiFunction<I, CompletableFuture<T>, T> invoker, I invocation, Executor executor) {
        super(executor, supportsAsync(executor));

        if (JAVA8)
            throw new UnsupportedOperationException();

        rejectManagedTask(invoker);

        this.action = invoker;
        this.contextDescriptor = ((WSManagedExecutorService) executor).captureThreadContext(XPROPS_SUSPEND_TRAN);
        this.invocation = invocation;

        ((Executor) futureRef).execute(this::runIfNotStarted);
    }

    @Override
    public T get() throws ExecutionException, InterruptedException {
        runIfNotStarted();
        return super.get();
    }

    @Override
    public T join() {
        runIfNotStarted();
        return super.join();
    }

    /**
     * Run the method inline if it hasn't started yet,
     * and complete this CompletableFuture with its result.
     */
    @FFDCIgnore({ Error.class, RuntimeException.class })
    private void runIfNotStarted() {
        if (!isDone() && started.compareAndSet(false, true)) {
            T result = null;
            Throwable failure = null;
            ArrayList<ThreadContext> contextApplied = null;
            try {
                if (contextDescriptor != null)
                    contextApplied = contextDescriptor.taskStarting();
                result = action.apply(invocation, this);
            } catch (RuntimeException x) {
                failure = x;
            } catch (Error x) {
                failure = x;
            } finally {
                try {
                    if (contextApplied != null)
                        contextDescriptor.taskStopping(contextApplied);
                } catch (RuntimeException x) {
                    failure = x;
                } finally {
                    if (failure == null)
                        super_complete(result);
                    else
                        super_completeExceptionally(failure);
                }
            }
        }
    }
}