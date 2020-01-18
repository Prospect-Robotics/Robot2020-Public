package com.team2813.lib.actions;

import com.team2813.frc2020.Robot;

import java.util.List;

import static com.team2813.frc2020.subsystems.Subsystems.LOOPER;

/**
 * @author Adrian Guerra
 * @author Grady Whelan
 */
public abstract class Action {

	public static void updateActions(List<Action> actions, double now) {
		// remove actions that have finished
		actions.removeIf(action -> {
			action.execute(now);
			if(action.isFinished(now) || (action.getRemoveOnDisabled() && LOOPER.mode == Robot.RobotMode.DISABLED)){
				action.end(now);
				return true;
			}
			else return false;
		});
	}

	/**
	 * runs the Action
	 * @param timestamp
	 */
	abstract void execute(double timestamp);

	/**
	 * Returns whether or not the code has finished execution.
	 *
	 * @return true if finished, false otherwise
	 */
	abstract boolean isFinished(double timestamp);

	/**
	 * Run code once when the action is started, for set up
	 */
	public abstract void start(double timestamp);

	/**
	 * Run after update returns true
	 */
	public abstract void end(double timestamp);

	/**
	 * Returns whether action should be removed when robot has been disabled.
	 *
	 * @return Always returns false
	 */
	public boolean getRemoveOnDisabled() {return false;}
}