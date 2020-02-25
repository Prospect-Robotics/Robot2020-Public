package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;

import java.io.IOException;
import java.nio.file.Paths;


public class GeneratedTrajectory implements AutoTrajectory {
    private boolean reversed;

    private Trajectory trajectory;

    public GeneratedTrajectory(String pathName, boolean reversed) {
        this.reversed = reversed;

        try {
            trajectory = TrajectoryUtil.fromPathweaverJson(Paths.get(Filesystem.getDeployDirectory().getAbsolutePath(), "paths", pathName + ".wpilib.json"));
        } catch (IOException e) {
            e.printStackTrace(); // todo samuel li
        }
    }

    public boolean isReversed() {
        return reversed;
    }

    public Trajectory getTrajectory() {
        return trajectory;
    }

    @Override
    public double getTotalTimeSeconds() {
        return trajectory.getTotalTimeSeconds();
    }
}