package com.team2813.frc2020.auto;

import com.team2813.lib.actions.Action;
import com.team2813.lib.actions.LockAction;
import com.team2813.lib.actions.WaitAction;
import com.team2813.lib.auto.RamseteTrajectory;

public enum AutoRoutineActions {
    huIFSDJK(new LockAction(() -> AutoRoutineTrajectories.FIE.getTrajectory().isCurrentTrajectory(2), true), AutoRoutineTrajectories.FIE);

    private Action action;
    private RamseteTrajectory trajectory;

    AutoRoutineActions(Action action, AutoRoutineTrajectories trajectory) {
        this.action = action;
        this.trajectory = trajectory.getTrajectory();
    }

    Action getAction() {
        return action;
    }

    RamseteTrajectory getTrajectory() {
        return trajectory;
    }

}
