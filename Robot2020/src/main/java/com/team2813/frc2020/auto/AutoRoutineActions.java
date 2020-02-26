package com.team2813.frc2020.auto;

import com.team2813.frc2020.util.AutoAimAction;
import com.team2813.lib.actions.Action;
import com.team2813.lib.actions.LockAction;
import com.team2813.lib.actions.SeriesAction;
import com.team2813.lib.actions.WaitAction;
import com.team2813.lib.auto.RamseteTrajectory;

public enum AutoRoutineActions {
    FIVE_BALL_ENEMY(
            new SeriesAction(
                    new AutoAimAction()/* shoot ball, intake, shoot ball*/
    ), AutoRoutineTrajectories.FIVE_BALL_ENEMY),
    TEST_ROUTINE(
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.TEST_ROUTINE),
    THREE_BALL(
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.THREE_BALL),
    SIX_BALL(
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.SIX_BALL),
    EIGHT_BALL(
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.EIGHT_BALL),
    FIVE_BALL_TWO(
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.FIVE_BALL_TWO),
    TEST_REVERSE(
            new SeriesAction(new WaitAction(1)
            ),AutoRoutineTrajectories.TEST_REVERSE),
    SERIES_TEST(
            new SeriesAction(
                    new AutoAimAction()
            ),AutoRoutineTrajectories.SERIES_TEST);
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
