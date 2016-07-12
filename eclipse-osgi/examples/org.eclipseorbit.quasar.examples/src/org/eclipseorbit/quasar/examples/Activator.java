package org.eclipseorbit.quasar.examples;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import co.paralleluniverse.actors.Actor;
import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.behaviors.RequestReplyHelper;
import co.paralleluniverse.fibers.SuspendExecution;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("Hello World!!");
		
		ActorRef<IsDivisibleBy> actor = new Actor<IsDivisibleBy, Void>(null, null) {
		    protected Void doRun() throws SuspendExecution, InterruptedException {
		        for(;;) {
		            IsDivisibleBy msg = receive();
		            try {
		                boolean result = (msg.getNumber() % msg.getDivisor() == 0);
		                RequestReplyHelper.reply(msg, result);
		            } catch (ArithmeticException e) {
		                RequestReplyHelper.replyError(msg, e);
		            }
		        }
		    }
		}.spawn();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("Goodbye World!!");
	}

}
