package co.paralleluniverse.quasar.core.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.weaving.WeavingHook;

import co.paralleluniverse.fibers.instrument.EntityWeavingHookImpl;

public class Activator implements BundleActivator {
	ServiceRegistration<WeavingHook> reference;

	@Override
	public void start(BundleContext context) throws Exception {
		reference = context.registerService(WeavingHook.class, new EntityWeavingHookImpl(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		reference.unregister();
	}
}
