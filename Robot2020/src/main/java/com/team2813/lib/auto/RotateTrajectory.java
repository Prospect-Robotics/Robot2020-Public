package com.team2813.lib.auto;

import com.team2813.frc2020.subsystems.Subsystems;
import com.team2813.lib.ctre.PigeonWrapper;
import com.team2813.lib.drive.DriveDemand;
import edu.wpi.first.wpilibj.trajectory.Trajectory;

public class RotateTrajectory implements AutoTrajectory {
    private boolean reversed;
    private double maxVelocity;
    private double originalHeading;
    private double currentHeading;
    private DriveDemand driveDemand;
    private PigeonWrapper pigeon = Subsystems.DRIVE.getPigeon();

    public RotateTrajectory(double degrees, boolean reversed){
        this.reversed = reversed;
        maxVelocity = Subsystems.DRIVE.getMaxVelocity();
        driveDemand = Subsystems.DRIVE.getDriveDemand();
        originalHeading = pigeon.getHeading();
        if(!reversed){
            while(currentHeading != (originalHeading + degrees)){
                driveDemand = new DriveDemand(maxVelocity / 2, -maxVelocity / 2);
                currentHeading = pigeon.getHeading();
            }
        }
        else{
            while(currentHeading != (originalHeading - degrees)){
                driveDemand = new DriveDemand(-maxVelocity / 2, maxVelocity / 2);
                currentHeading = pigeon.getHeading();
            }
        }
    }

    @Override
    public double getTotalTimeSeconds() {
        return 0;
    }

    @Override
    public Trajectory getTrajectory() {
        return null;
    }

    @Override
    public boolean isReversed() {
        return reversed;
    }
}
