package com.team2813.frc2020.auto;

import com.team2813.lib.auto.AutoTrajectory;
import com.team2813.lib.auto.RamseteTrajectory;
import edu.wpi.first.wpilibj.trajectory.Trajectory;

import java.util.List;

public enum AutoRoutineTrajectories {
    FIE;

    private RamseteTrajectory trajectory;

    AutoRoutineTrajectories(List<AutoTrajectory> trajectory) {
        this.trajectory = new RamseteTrajectory(trajectory);
    }

    public RamseteTrajectory getTrajectory() {
        return trajectory;
    }
}
