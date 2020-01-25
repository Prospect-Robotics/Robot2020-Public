package com.team2813.lib.auto;

import com.team2813.frc2020.subsystems.DriveTalon;
import com.team2813.frc2020.subsystems.Subsystems;
import com.team2813.lib.ctre.PigeonWrapper;
import com.team2813.lib.drive.DriveDemand;

public class RotateTrajectory {
    private double degrees;
    private boolean reversed;
    private double maxVelocity;
    private double originalHeading;
    private double currentHeading;
    private DriveDemand driveDemand;
    private PigeonWrapper pigeon = Subsystems.DRIVE.getPigeon();

    public RotateTrajectory(double degrees, boolean reversed){
        this.degrees = degrees;
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
}
