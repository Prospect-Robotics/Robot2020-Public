package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.util.Units;

import java.util.*;

public class RotateTrajectory implements AutoTrajectory {
    private Trajectory trajectory;


    public RotateTrajectory(double degrees){
        ArrayList<Trajectory.State> states = new ArrayList<>();
        Pose2d pose = new Pose2d(new Translation2d(0, 0), new Rotation2d(Units.degreesToRadians(degrees)));
        states.add(new Trajectory.State());
        states.get(0).poseMeters = pose;
        trajectory = new Trajectory(states);
    }

    @Override
    public double getTotalTimeSeconds() {
        return trajectory.getTotalTimeSeconds();
    }

    @Override
    public Trajectory getTrajectory() {
        return trajectory;
    }

    @Override
    public boolean isReversed() {
        return false;
    }
}
