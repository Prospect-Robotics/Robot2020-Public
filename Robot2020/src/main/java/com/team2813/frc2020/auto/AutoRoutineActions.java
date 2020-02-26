package com.team2813.frc2020.auto;

import com.team2813.frc2020.util.AutoAimAction;
import com.team2813.lib.actions.Action;
import com.team2813.lib.actions.LockAction;
import com.team2813.lib.actions.SeriesAction;
import com.team2813.lib.actions.WaitAction;
import com.team2813.lib.auto.RamseteTrajectory;

public enum AutoRoutineActions {
    FIVE_BALL_ENEMY("5 Ball Enemy",
            new SeriesAction(
                    new AutoAimAction()/* shoot ball, intake, shoot ball*/
    ), AutoRoutineTrajectories.FIVE_BALL_ENEMY),
    TEST_ROUTINE("Test",
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.TEST_ROUTINE),
    THREE_BALL("3-ball",
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.THREE_BALL),
    SIX_BALL("6-ball",
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.SIX_BALL),
    EIGHT_BALL("8-ball",
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.EIGHT_BALL),
    FIVE_BALL_TWO("5-ball 2",
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.FIVE_BALL_TWO),
    TEST_REVERSE("test reverse",
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.TEST_REVERSE),
    SERIES_TEST("Auto Aim Test",
            new SeriesAction(
                    new AutoAimAction()
            ),AutoRoutineTrajectories.SERIES_TEST);
    private Action action;
    private RamseteTrajectory trajectory;
    public String name;

    AutoRoutineActions(String name, Action action, AutoRoutineTrajectories trajectory) {
        this.action = action;
        this.trajectory = trajectory.getTrajectory();
        this.name = name;
    }

    Action getAction() {
        return action;
    }

    RamseteTrajectory getTrajectory() {
        return trajectory;
    }

}
