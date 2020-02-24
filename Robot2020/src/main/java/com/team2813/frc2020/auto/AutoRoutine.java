package com.team2813.frc2020.auto;

import com.team2813.frc2020.subsystems.Magazine;
import com.team2813.frc2020.subsystems.Shooter;
import com.team2813.frc2020.subsystems.Subsystems;
import com.team2813.frc2020.util.AutoAimAction;
import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.lib.actions.*;
import com.team2813.lib.auto.AutoTrajectory;
import com.team2813.lib.auto.GeneratedTrajectory;
import com.team2813.lib.auto.PauseTrajectory;
import com.team2813.lib.auto.RotateTrajectory;
import com.team2813.lib.auto.RamseteTrajectory;
import com.team2813.lib.drive.DriveDemand;


import java.util.List;

import static com.team2813.frc2020.subsystems.Subsystems.*;

public enum AutoRoutine {
    FIVE_BALL_ENEMY("5 Ball Enemy", List.of(
            new PauseTrajectory(1), // shoot 3 balls
            new GeneratedTrajectory("2-ball", true),
            new PauseTrajectory(1), // intake 2 balls, turn intake off
            new GeneratedTrajectory("go back", false),
            new GeneratedTrajectory("return", false),
            new PauseTrajectory(1) // shoot 2 balls
    ), new SeriesAction(new WaitAction(1)/* shoot ball, intake, shoot ball*/)),
    TEST_ROUTINE("Test", List.of(
            new PauseTrajectory(0.5),
            new GeneratedTrajectory("test", false),
            new RotateTrajectory(180),
            new GeneratedTrajectory("another test path", false)
    ), new SeriesAction(new WaitAction(1))),
    THREE_BALL("3-ball", List.of(
            new PauseTrajectory(1), //shoot 3-balls
            new GeneratedTrajectory("3-ball prepare", false),
            new PauseTrajectory(1), //turn intake on
            new GeneratedTrajectory("3-ball", true), //intake 3 balls
            new GeneratedTrajectory("2-ball 2", true), //intake 2 balls
            new PauseTrajectory(1) //turn intake off
    ), new SeriesAction(new WaitAction(1))),
    SIX_BALL("6-ball", List.of(
            new PauseTrajectory(1), // shoot 3 ball turn intake on
            new GeneratedTrajectory("3-ball trench", true), // intake 3 balls
            new PauseTrajectory(1), //turn intake off
            new GeneratedTrajectory("back 3-ball", false),
            new PauseTrajectory(1) // shoot 3 balls
    ), new SeriesAction(new WaitAction(1))),
    EIGHT_BALL("8-ball", List.of(
            new GeneratedTrajectory("back trench", true),
            new PauseTrajectory(1), // turn intake on
            new GeneratedTrajectory("2-ball shield generator", true), // intake 2 balls
            new GeneratedTrajectory("3-ball 2", true), // intake 3 balls
            new PauseTrajectory(1), // turn intake off
            new GeneratedTrajectory("return 8-ball", false),
            new PauseTrajectory(1) // shoot 5 balls
    ), new SeriesAction(new WaitAction(1))),
    FIVE_BALL_TWO("5-ball 2", List.of(
            new PauseTrajectory(1), //turn intake on
            new GeneratedTrajectory("2-ball initiation line", true), //intake 2 balls
            new RotateTrajectory(180),
            new PauseTrajectory(1), //turn intake off, shoot 5 balls
            new GeneratedTrajectory("general zone", false)
    ), new SeriesAction(new WaitAction(1))),
    TEST_REVERSE("test reverse", List.of(
            new GeneratedTrajectory("test reverse", true)
    ), new SeriesAction(new WaitAction(1))),
    SCRIMMAGE_MID("Scrimmage Mid", List.of(new PauseTrajectory(60)),
            new SeriesAction(
                    new LockAction(() -> {
                        System.out.println("HI");
                        double steer = DRIVE.limelight.getSteer();
                        System.out.println(steer);
                        DRIVE.setDemand(DRIVE.curvatureDrive.getDemand(0, 0, steer, true));
                        return steer == 0;
                    }, true),
                    new FunctionAction(() -> SHOOTER.setPosition(Shooter.Position.INITIATION), true),
                    new FunctionAction(() -> System.out.println("SET HOOD"), true),
                    new FunctionAction(() -> SHOOTER.stopSpinningFlywheel(), true),
                    new WaitAction(1),
                    new FunctionAction(MAGAZINE::spinMagazineForward, true),
                    new WaitAction(2),
                    new FunctionAction(SHOOTER::stopSpinningFlywheel, true),
                    new FunctionAction(MAGAZINE::stopMagazine, true),
                    new FunctionAction(() -> DRIVE.setDemand(new DriveDemand(-.3, -.3)), true),
                    new WaitAction(1),
                    new FunctionAction(() -> DRIVE.setDemand(new DriveDemand(0, 0)), true)
            )),
    BRUH("bruh", List.of(new PauseTrajectory(60)),
            new SeriesAction(
                    new FunctionAction(() -> DRIVE.setDemand(new DriveDemand(-.3, -.3)), true),
                    new WaitAction(1),
                    new FunctionAction(() -> DRIVE.setDemand(new DriveDemand(0, 0)), true))),
    SERIES_TEST("Auto Aim Test", List.of(
            new PauseTrajectory(150)
    ), new SeriesAction(
            new AutoAimAction()
    ));


    public String name;
    public RamseteTrajectory trajectory;
    public Action action;

    AutoRoutine(String name, List<AutoTrajectory> trajectory, Action action) {
        this.name = name;
        this.trajectory = new RamseteTrajectory(trajectory);
        this.action = action;
    }


}
