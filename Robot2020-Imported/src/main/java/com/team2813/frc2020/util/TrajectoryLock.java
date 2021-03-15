package com.team2813.frc2020.util;

import com.team2813.frc2020.auto.AutoTrajectories;
import com.team2813.lib.actions.LockAction;

import java.util.concurrent.Callable;

public class TrajectoryLock extends LockAction {
    /**
     * Creates a new action that waits until a condition is met
     *
     * @param function
     * @param removeOnDisabled
     */
    private TrajectoryLock(Callable<Boolean> function, boolean removeOnDisabled) {
        super(function, removeOnDisabled);
    }

    public TrajectoryLock(AutoTrajectories trajectory, int index) {
        super(() -> trajectory.getTrajectory().isCurrentTrajectory(index), true);
    }
}
