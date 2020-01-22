package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

public interface AutoTrajectory {
    double getTotalTimeSeconds();
    Trajectory getTrajectory();
    boolean isReversed();
}
