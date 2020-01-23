package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

public class TrajectorySample {
    private Trajectory.State state;
    private boolean reversed;

    private boolean isPause;

    public TrajectorySample(boolean isPause) {
        this.isPause = isPause;
    }

    public TrajectorySample(Trajectory.State state, boolean reversed) {
        this.state = state;
        this.reversed = reversed;
    }

    public boolean isPause() {
        return isPause;
    }

    public Trajectory.State getState() {
        return state;
    }

    public boolean isReversed() {
        return reversed;
    }
}
