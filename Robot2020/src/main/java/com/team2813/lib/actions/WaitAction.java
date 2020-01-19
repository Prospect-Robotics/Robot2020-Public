package com.team2813.lib.actions;

public class WaitAction extends Action {

	private double duration;
	private double startTime;

	/**
	 * Create a new action to wait an amount of time in seconds
	 * @param durationInSeconds
	 */
	public WaitAction(double durationInSeconds) {
		this.duration = durationInSeconds;
	}

	@Override
	void execute(double timestamp) {

	}

	@Override
	public boolean isFinished(double timestamp) {
		return timestamp - startTime >= duration;
	}

	@Override
	public void start(double timestamp) {
		startTime = timestamp;
	}

	@Override
	public void end(double timestamp) {

	}

	@Override
	public boolean getRemoveOnDisabled() {
		return false;
	}
}