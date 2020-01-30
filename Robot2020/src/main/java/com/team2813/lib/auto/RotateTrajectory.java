package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

import java.util.List;

public class RotateTrajectory extends Trajectory implements AutoTrajectory {
    private boolean finished;
    private double degrees;
    private double seconds = .05; // to always stay a little ahead
    private static final double MAX_ROTATION_TIME = 4.0;

    private RotateTrajectory(List<State> states) { // should never be used
        super(states);
    }

    public RotateTrajectory(double degrees) {
        this(List.of(new Trajectory.State()));
        this.degrees = degrees;
    }

    public double getDegrees() {
        return degrees;
    }

    public void poll() {
        this.seconds += 1.0 / 50; // every 20ms
    }

    public void finish() {
        finished = true;
    }

    public void resetTimer(){
        seconds = 0.05;
    }

    @Override
    public double getTotalTimeSeconds() {
        System.out.println(seconds);
        return finished ? seconds : (seconds + 1);// +1 is unreachable so that it will never stop if unfinished
    }

    @Override
    public Trajectory getTrajectory() {
        return this;
    }

    @Override
    public boolean isReversed() {
        return false;
    }
}
