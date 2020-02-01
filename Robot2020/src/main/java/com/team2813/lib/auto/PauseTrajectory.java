package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

import java.util.List;

public class PauseTrajectory extends Trajectory implements AutoTrajectory {
    private double time;

    private PauseTrajectory(List<State> states) { // should never be used
        super(states);
    }

    public PauseTrajectory(double time) {
        super(List.of(new Trajectory.State()));
        this.time = time;
    }

    @Override
    public double getTotalTimeSeconds() {
        return time;
    }

    @Override
    public Trajectory getTrajectory() {
        return this;
    }

    @Override
    public boolean isReversed() {
        return false;
    }

    public boolean isPause() {
        return true;
    }
}
