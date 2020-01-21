package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

import java.util.List;

public class PauseTrajectory extends Trajectory {
    private double time;

    private PauseTrajectory(List<State> states) { // should never be used
        super(states);
    }

    public PauseTrajectory(double time) {
        super(List.of());
        this.time = time;
    }

    @Override
    public double getTotalTimeSeconds() {
        return time;
    }

    public boolean isPause(double time) {
        return time > this.time;
    }
}
