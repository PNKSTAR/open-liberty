/*******************************************************************************
 * Copyright (c) 2017,2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.concurrent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import com.ibm.ws.threading.PolicyExecutor;
import com.ibm.wsspi.threadcontext.ThreadContextDescriptor;

/**
 * Interface that exposes various details about a managed executor service internally to other bundles.
 */
public interface WSManagedExecutorService {
    /**
     * <p>Captures context from the thread that invokes this method,
     * or creates new thread context as determined by the execution properties.
     * Do not expect the captured context to be serializable.</p>
     *
     * @param props execution properties. Custom property keys must not begin with "javax.enterprise.concurrent."
     * @return captured thread context.
     */
    ThreadContextDescriptor captureThreadContext(Map<String, String> props);

    /**
     * When the longRunningPolicy is configured, returns the policy executor for running tasks against the long running concurrency policy.
     * Otherwise, returns null when the longRunningPolicy is not configured.
     *
     * @return the policy executor for running tasks if the long running concurrency policy is configured. Otherwise, returns null.
     */
    PolicyExecutor getLongRunningPolicyExecutor();

    /**
     * Returns the policy executor for running tasks against the normal concurrency policy.
     *
     * @return the policy executor for running tasks against the normal concurrency policy.
     */
    PolicyExecutor getNormalPolicyExecutor();

    /**
     * Construct a CompletableFuture that represents the execution of an asynchronous method,
     * to be performed asynchronously to the caller, either on the specified executor
     * or on a thread that attempts join or untimed get.
     *
     * @param <I>        jakarta.interceptor.InvocationContext.
     * @param <T>        type of result.
     * @param invoker    completion stage action that invokes the asynchronous method.
     *                       The first parameter to the BiFunction is the interceptor's InvocationContext.
     *                       The second parameter is the BiFunction is the CompletableFuture that will be returned to the caller.
     *                       The result parameter of the BiFunction is the same CompletableFuture.
     * @param invocation the interceptor's InvocationContext that will be supplied to the BiFunction.
     * @return CompletableFuture that represents the invocation of the asynchronous method.
     */
    <I, T> CompletableFuture<T> newAsyncMethod(BiFunction<I, CompletableFuture<T>, T> invoker, I invocation);
}
