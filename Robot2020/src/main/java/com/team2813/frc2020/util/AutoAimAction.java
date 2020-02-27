package com.team2813.frc2020.util;

import com.team2813.frc2020.subsystems.Shooter;
import com.team2813.frc2020.subsystems.Subsystem;
import com.team2813.lib.actions.FunctionAction;
import com.team2813.lib.actions.LockAction;
import com.team2813.lib.actions.SeriesAction;
import com.team2813.lib.actions.WaitAction;

import static com.team2813.frc2020.subsystems.Subsystems.*;
import static com.team2813.frc2020.subsystems.Subsystems.SHOOTER;

public class AutoAimAction extends SeriesAction {
    public AutoAimAction() {
        super(
                new FunctionAction(()->SHOOTER.setPosition(Shooter.Position.INITIATION), true),
                new FunctionAction(SHOOTER::startSpinningFlywheel, true),
                new WaitAction(1),
                new LockAction(() -> {
                    double steer = SHOOTER.getLimelight().getSteer();
                    DRIVE.setDemand(DRIVE.curvatureDrive.getDemand(0, 0, SHOOTER.getLimelight().getSteer(), true));
                    SHOOTER.getLimelight().setLights(true);

                    SHOOTER.adjustHood();
                    return steer == 0;
                }, true),
                new FunctionAction(MAGAZINE::spinMagazineForward,true),
                new WaitAction(1),
                new FunctionAction(SHOOTER::stopSpinningFlywheel, true),
                new FunctionAction(MAGAZINE::stopMagazine, true),
                new FunctionAction(() -> SHOOTER.setPosition(Shooter.Position.MIN), true));
    }
}
