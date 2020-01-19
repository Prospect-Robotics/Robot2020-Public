package com.team2813.lib.auto;


import edu.wpi.first.wpilibj.trajectory.Trajectory;

import java.io.IOException;

public class GeneratedTrajectory {
    private boolean reversed;

    private Trajectory trajectory;

    public GeneratedTrajectory(String pathName, boolean reversed) throws IOException {
        this.reversed = reversed;

//        trajectory = PathfinderFRC.getTrajectory(pathName);
    }

    public boolean isReversed() {
        return reversed;
    }

    public Trajectory getTrajectory() {
        return trajectory;
    }
}