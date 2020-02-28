package com.team2813.frc2020.auto;

import com.team2813.frc2020.subsystems.Intake;
import com.team2813.frc2020.util.AutoAimAction;
import com.team2813.lib.actions.*;
import com.team2813.lib.auto.RamseteTrajectory;

import static com.team2813.frc2020.subsystems.Subsystems.INTAKE;
import static com.team2813.frc2020.subsystems.Subsystems.MAGAZINE;

public enum AutoRoutine {

    FIVE_BALL_ENEMY("5 Ball Enemy",
            new SeriesAction(
                    new AutoAimAction(),
                    new LockAction(() -> AutoTrajectories.FIVE_BALL_ENEMY.getTrajectory().isCurrentTrajectory(2), true),
                    new FunctionAction(() -> INTAKE.autoIntake(true), true),
                    new WaitAction(1),
                    new FunctionAction(() -> INTAKE.autoIntake(false), true),
                    new LockAction(() -> AutoTrajectories.FIVE_BALL_ENEMY.getTrajectory().isCurrentTrajectory(5), true),
                    new AutoAimAction()/* shoot ball, intake, shoot ball*/
            ), AutoTrajectories.FIVE_BALL_ENEMY),
    TEST_ROUTINE("Test",
            new SeriesAction(
                    new WaitAction(1)
            ), AutoTrajectories.TEST_ROUTINE),
    THREE_BALL("3-ball",
            new SeriesAction(
                    new AutoAimAction(),
                    new LockAction(() -> AutoTrajectories.THREE_BALL.getTrajectory().isCurrentTrajectory(2), true),
                    new FunctionAction(() -> INTAKE.autoIntake(true), true),
                    new LockAction(() -> AutoTrajectories.THREE_BALL.getTrajectory().isCurrentTrajectory(5), true),
                    new FunctionAction(() -> INTAKE.autoIntake(false), true)
            ), AutoTrajectories.THREE_BALL),
    SIX_BALL("6-ball",
            new SeriesAction(
                    new AutoAimAction(),
                    new FunctionAction(() -> INTAKE.autoIntake(true), true),
                    new LockAction(() -> AutoTrajectories.SIX_BALL.getTrajectory().isCurrentTrajectory(2), true),
                    new FunctionAction(() -> INTAKE.autoIntake(false), true),
                    new LockAction(() -> AutoTrajectories.SIX_BALL.getTrajectory().isCurrentTrajectory(4), true),
                    new FunctionAction(MAGAZINE::spinMagazineReverse, true),
                    new FunctionAction(() -> INTAKE.setIntake(Intake.Demand.IN), true),
                    new WaitAction(.15),
                    new FunctionAction(MAGAZINE::stopMagazine, true),
                    new FunctionAction(() -> INTAKE.setIntake(Intake.Demand.OFF), true),
                    new AutoAimAction()
            ), AutoTrajectories.SIX_BALL),

    EIGHT_BALL("8-ball",
            new SeriesAction(
//                    new LockAction(() -> AutoTrajectories.EIGHT_BALL.getTrajectory().isCurrentTrajectory(1), true),
//                    new FunctionAction(() -> INTAKE.autoIntake(true), true),
//                    new LockAction(() -> AutoTrajectories.EIGHT_BALL.getTrajectory().isCurrentTrajectory(4), true),
//                    new FunctionAction(() -> INTAKE.autoIntake(false), true),
//                    new LockAction(() -> AutoTrajectories.EIGHT_BALL.getTrajectory().isCurrentTrajectory(6), true),
//                    new AutoAimAction()
                    new WaitAction(1)
            ), AutoTrajectories.EIGHT_BALL),

    FIVE_BALL_TWO("5-ball 2",
            new SeriesAction(
                    new FunctionAction(() -> INTAKE.autoIntake(true), true),
                    new LockAction(() -> AutoTrajectories.FIVE_BALL_TWO.getTrajectory().isCurrentTrajectory(3), true),
                    new FunctionAction(() -> INTAKE.autoIntake(false), false),
                    new AutoAimAction()
            ), AutoTrajectories.FIVE_BALL_TWO),

    TEST_REVERSE("test reverse",
            new SeriesAction(new WaitAction(1)
            ), AutoTrajectories.TEST_REVERSE),

    SERIES_TEST("Auto Aim Test",
            new SeriesAction(
                    new AutoAimAction()
            ),
            AutoTrajectories.SERIES_TEST);
    
    private Action action;
    private RamseteTrajectory trajectory;
    public String name;

    AutoRoutine(String name, Action action, AutoTrajectories trajectory) {
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
