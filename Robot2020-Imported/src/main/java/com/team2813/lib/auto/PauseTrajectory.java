package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

import java.util.List;

public class PauseTrajectory extends Trajectory implements AutoTrajectory {
    private double time;
    private final int INDEX;

    private PauseTrajectory(List<State> states, int index) { // should never be used
        super(states);
        INDEX = index;
    }

    public PauseTrajectory(double time, int index) {
        super(List.of(new Trajectory.State()));
        this.time = time;
        INDEX = index;
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

    @Override
    public int getIndex() {
        return INDEX;
    }

    public boolean isPause() {
        return true;
    }
}
