//Copyright (c) 2018 Team 254
package com.team2813.lib.util;

/**
 * Runnable class with reports all uncaught throws to CrashTracker
 */
public abstract class CrashTrackingRunnable implements Runnable {

	@Override
	public final void run() {
		try {
			runCrashTracked();
		} catch (Throwable t) {
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}

	public abstract void runCrashTracked();
}
