/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.concurrent.mp;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import javax.enterprise.concurrent.ManagedExecutorService;

import org.eclipse.microprofile.concurrent.ManagedExecutor;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.ibm.websphere.ras.annotation.Trivial;
import com.ibm.ws.concurrency.policy.ConcurrencyPolicy;
import com.ibm.ws.concurrent.service.AbstractManagedExecutorService;
import com.ibm.ws.threading.PolicyExecutor;
import com.ibm.wsspi.application.lifecycle.ApplicationRecycleComponent;
import com.ibm.wsspi.application.lifecycle.ApplicationRecycleCoordinator;
import com.ibm.wsspi.kernel.service.utils.AtomicServiceReference;
import com.ibm.wsspi.resource.ResourceFactory;
import com.ibm.wsspi.threadcontext.WSContextService;

/**
 * Super class of ManagedExecutorServiceImpl to be used with Java 8 and above.
 * This class provides implementation of the MicroProfile Concurrency methods.
 * These methods can be collapsed into ManagedExecutorServiceImpl once there is
 * no longer a need for OpenLiberty to support Java 7.
 */
@Component(configurationPid = "com.ibm.ws.concurrent.managedExecutorService", configurationPolicy = ConfigurationPolicy.REQUIRE,
           service = { ExecutorService.class, ManagedExecutor.class, ManagedExecutorService.class, ResourceFactory.class, ApplicationRecycleComponent.class },
           reference = @Reference(name = "ApplicationRecycleCoordinator", service = ApplicationRecycleCoordinator.class),
           property = { "creates.objectClass=java.util.concurrent.ExecutorService",
                        "creates.objectClass=javax.enterprise.concurrent.ManagedExecutorService",
                        "creates.objectClass=org.eclipse.microprofile.concurrent.ManagedExecutor" })
public class ManagedExecutorImpl extends AbstractManagedExecutorService implements ManagedExecutor {
    /**
     * Constructor for OSGi code path.
     */
    @Trivial
    public ManagedExecutorImpl() {
        super();
    }

    /**
     * Constructor for MicroProfile Concurrency (ManagedExecutorBuilder and CDI injected ManagedExecutor).
     * Also used to construct a managed executor for use by the ManagedCompletableFuture returned from
     * ThreadContext.withContextCapture.
     */
    public ManagedExecutorImpl(String name, PolicyExecutor policyExecutor, WSContextService mpThreadContext,
                               AtomicServiceReference<com.ibm.wsspi.threadcontext.ThreadContextProvider> tranContextProviderRef) {
        super(name, policyExecutor, mpThreadContext, tranContextProviderRef);
    }

    @Activate
    @Override
    @Trivial
    protected void activate(ComponentContext context, Map<String, Object> properties) {
        super.activate(context, properties);
    }

    @Override
    public <U> CompletableFuture<U> completedFuture(U value) {
        return ManagedCompletableFuture.completedFuture(value, this);
    }

    @Override
    public <U> CompletionStage<U> completedStage(U value) {
        return ManagedCompletableFuture.completedStage(value, this);
    }

    @Deactivate
    @Override
    @Trivial
    protected void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    @Override
    public <U> CompletableFuture<U> failedFuture(Throwable ex) {
        return ManagedCompletableFuture.failedFuture(ex, this);
    }

    @Override
    public <U> CompletionStage<U> failedStage(Throwable ex) {
        return ManagedCompletableFuture.failedStage(ex, this);
    }

    @Override
    @Modified
    @Trivial
    protected void modified(final ComponentContext context, Map<String, Object> properties) {
        super.modified(context, properties);
    }

    @Override
    public <U> CompletableFuture<U> newIncompleteFuture() {
        if (ManagedCompletableFuture.JAVA8)
            return new ManagedCompletableFuture<U>(new CompletableFuture<U>(), this, null);
        else
            return new ManagedCompletableFuture<U>(this, null);
    }

    @Override
    public CompletableFuture<Void> runAsync(Runnable runnable) {
        return ManagedCompletableFuture.runAsync(runnable, this);
    }

    @Override
    @Reference(policy = ReferencePolicy.DYNAMIC, target = "(id=unbound)")
    @Trivial
    protected void setConcurrencyPolicy(ConcurrencyPolicy svc) {
        super.setConcurrencyPolicy(svc);
    }

    @Override
    @Reference(policy = ReferencePolicy.DYNAMIC, target = "(id=unbound)")
    @Trivial
    protected void setContextService(ServiceReference<WSContextService> ref) {
        super.setContextService(ref);
    }

    @Override
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL, target = "(id=unbound)")
    @Trivial
    protected void setLongRunningPolicy(ConcurrencyPolicy svc) {
        super.setLongRunningPolicy(svc);
    }

    @Override
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL, target = "(component.name=com.ibm.ws.transaction.context.provider)")
    @Trivial
    protected void setTransactionContextProvider(ServiceReference<com.ibm.wsspi.threadcontext.ThreadContextProvider> ref) {
        super.setTransactionContextProvider(ref);
    }

    @Override
    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return ManagedCompletableFuture.supplyAsync(supplier, this);
    }

    @Override
    @Trivial
    protected void unsetConcurrencyPolicy(ConcurrencyPolicy svc) {
        super.unsetConcurrencyPolicy(svc);
    }

    @Override
    @Trivial
    protected void unsetContextService(ServiceReference<WSContextService> ref) {
        super.unsetContextService(ref);
    }

    @Override
    @Trivial
    protected void unsetLongRunningPolicy(ConcurrencyPolicy svc) {
        super.unsetLongRunningPolicy(svc);
    }

    @Override
    @Trivial
    protected void unsetTransactionContextProvider(ServiceReference<com.ibm.wsspi.threadcontext.ThreadContextProvider> ref) {
        super.unsetTransactionContextProvider(ref);
    }
}
