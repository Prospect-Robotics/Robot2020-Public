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
    FIVE_BALL_ENEMY_SIDE("5 ball Enemy Side",
            new SeriesAction(
                    new AutoAimAction(),
                    new LockAction(() -> AutoTrajectories.FIVE_BALL_ENEMY.getTrajectory().isCurrentTrajectory(2), true),
                    new FunctionAction(() -> INTAKE.autoIntake(true), true),
                    new WaitAction(1),
                    new FunctionAction(() -> INTAKE.autoIntake(false), true),
                    new LockAction(() -> AutoTrajectories.FIVE_BALL_ENEMY.getTrajectory().isCurrentTrajectory(5), true),
                    new AutoAimAction()/* shoot ball, intake, shoot ball*/
            ), AutoTrajectories.FIVE_BALL_ENEMY_SIDE),
    TEST_ROUTINE("Test",
            new SeriesAction(
                    new WaitAction(1)
            ), AutoTrajectories.TEST_ROUTINE),
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
    SIX_BALL_SIDE("6-ball Side",
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
            ), AutoTrajectories.SIX_BALL_SIDE),
    NINE_BALL("9-ball",
            new SeriesAction(
                    new AutoAimAction(),
//                    new LockAction(() -> AutoTrajectories.NINE_BALL.getTrajectory().isCurrentTrajectory(1), true),
                    new FunctionAction(() -> INTAKE.autoIntake(true), true),
                    new LockAction(() -> AutoTrajectories.NINE_BALL.getTrajectory().isCurrentTrajectory(3), true),
                    new FunctionAction(() -> INTAKE.autoIntake(false), true),
//                    new LockAction(() -> AutoTrajectories.NINE_BALL.getTrajectory().isCurrentTrajectory(3), true),
                    new AutoAimAction(),
//                    new LockAction(() -> AutoTrajectories.NINE_BALL.getTrajectory().isCurrentTrajectory(5), true),
                    new FunctionAction(() -> INTAKE.autoIntake(true), true),
//                    new LockAction(() -> AutoTrajectories.NINE_BALL.getTrajectory().isCurrentTrajectory(10), true),

                    new LockAction(() -> AutoTrajectories.NINE_BALL.getTrajectory().isCurrentTrajectory(11), true),
                    new FunctionAction(() -> INTAKE.autoIntake(false), true),
                    new AutoAimAction()
//                    new LockAction(() -> AutoTrajectories.EIGHT_BALL.getTrajectory().isCurrentTrajectory(1), true),
//                    new FunctionAction(() -> INTAKE.autoIntake(true), true),
//                    new LockAction(() -> AutoTrajectories.EIGHT_BALL.getTrajectory().isCurrentTrajectory(4), true),
//                    new FunctionAction(() -> INTAKE.autoIntake(false), true),
//                    new LockAction(() -> AutoTrajectories.EIGHT_BALL.getTrajectory().isCurrentTrajectory(6), true),
//                    new AutoAimAction()
//                    new LockAction(() -> AutoTrajectories.NINE_BALL.getTrajectory().isCurrentTrajectory(4), true),
//                    new AutoAimAction()
            ), AutoTrajectories.NINE_BALL),

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
            AutoTrajectories.SERIES_TEST),
    AUTO_AIM_FORWARD("Auto Aim Forward",
            new SeriesAction(
                    new AutoAimAction()
            ), AutoTrajectories.GO_FORWARD);
    
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
