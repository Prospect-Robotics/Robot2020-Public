package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

import java.util.ArrayList;
import java.util.List;

/**
 * allows RamseteAuto to only have ot process one trajectory as a WHOLE and simplifies chaining together multiple trajectories;
 * @author Samuel Li
  */
public class RamseteTrajectory {
    private List<Trajectory> trajectories = new ArrayList<>();
    private List<Boolean> reversed = new ArrayList<>();

    public RamseteTrajectory(Trajectory trajectory, boolean reversed) {
        trajectories.add(trajectory);
        this.reversed.add(reversed);
    }

    public RamseteTrajectory(List<GeneratedTrajectory> generatedTrajectories) {
        for (GeneratedTrajectory generatedTrajectory : generatedTrajectories) {
            trajectories.add(generatedTrajectory.getTrajectory());
            reversed.add(generatedTrajectory.isReversed());
        }
    }

    public TrajectorySample sample(double dt) {
        double time = 0;
        for (int i = 0; i < trajectories.size(); i++) {
            Trajectory trajectory = trajectories.get(i);

            if (trajectory instanceof PauseTrajectory) // if it is a pause
                return new TrajectorySample(((PauseTrajectory) trajectory).isPause(time));

            if (dt < time + trajectory.getTotalTimeSeconds())
                return new TrajectorySample(trajectory.sample(dt - time), reversed.get(i));
            else
                time += trajectory.getTotalTimeSeconds();
        }
        return new TrajectorySample(trajectories.get(0).sample(dt), reversed.get(0));
    }

    public List<Trajectory> getTrajectories() {
        return this.trajectories;
    }
}
