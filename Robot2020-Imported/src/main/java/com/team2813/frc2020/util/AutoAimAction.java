package com.team2813.frc2020.util;

import com.team2813.frc2020.subsystems.Shooter;
import com.team2813.frc2020.subsystems.Subsystem;
import com.team2813.lib.actions.*;

import static com.team2813.frc2020.subsystems.Subsystems.*;
import static com.team2813.frc2020.subsystems.Subsystems.SHOOTER;

public class AutoAimAction extends SeriesAction {
    private static Limelight limelight = Limelight.getInstance();

    public AutoAimAction() {
        super(
                new FunctionAction(() -> SHOOTER.setPosition(Shooter.Position.INITIATION), true),
                new FunctionAction(SHOOTER::startSpinningFlywheel, true),
                new ParallelAction(
                        new LockAction(() -> {
                            double steer = limelight.getSteer();
                            limelight.setLights(true);
                            DRIVE.setDemand(DRIVE.velocityDrive.getVelocity(DRIVE.curvatureDrive.getDemand(0, 0, limelight.getSteer(), true)));
                            SHOOTER.adjustHood();
                            return steer == 0;
                        }, true),
                        new FlywheelReadyAction()
                ),
                new WaitAction(.1),
                new FunctionAction(MAGAZINE::spinMagazineForward, true),
                new WaitAction(1.2),
                new FunctionAction(SHOOTER::stopSpinningFlywheel, true),
                new FunctionAction(MAGAZINE::stopMagazine, true),
                new FunctionAction(() -> SHOOTER.setPosition(Shooter.Position.MIN), true));
    }
}
