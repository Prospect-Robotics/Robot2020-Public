package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneratedTrajectory {
    private boolean reversed;

    private Trajectory trajectory;

    public GeneratedTrajectory(String pathName, boolean reversed) throws IOException {
        this.reversed = reversed;

        trajectory = TrajectoryUtil.fromPathweaverJson(Paths.get(Filesystem.getDeployDirectory().getAbsolutePath(), "deploy", pathName + ".wpilib.json"));
    }

    public boolean isReversed() {
        return reversed;
    }

    public Trajectory getTrajectory() {
        return trajectory;
    }
}