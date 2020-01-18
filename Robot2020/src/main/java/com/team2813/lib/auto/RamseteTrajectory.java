package com.team2813.lib.auto;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

// interface between Jaci's trajectory (used in Pathweaver), WPILib 2020's trajectory, as well as RamseteAuto
public class RamseteTrajectory {
    private List<Trajectory> trajectories = new ArrayList<>();
    private List<Boolean> reversed = new ArrayList<>();

    public RamseteTrajectory(Trajectory trajectory, boolean reversed) {
        trajectories.add(trajectory);
        this.reversed.add(reversed);
    }

    public RamseteTrajectory(List<GeneratedTrajectory> generatedTrajectories) {
        for (GeneratedTrajectory generatedTrajectory : generatedTrajectories) {
            trajectories.add(pathweaverToWPI(generatedTrajectory));
            reversed.add(generatedTrajectory.isReversed());
        }
    }

    private Trajectory pathweaverToWPI(GeneratedTrajectory generatedTrajectory) {
        List<Trajectory.State> states = new ArrayList<Trajectory.State>();
//        jaci.pathfinder.Trajectory trajectory = generatedTrajectory.getTrajectory();
//        for (int i = 0; i < trajectory.length(); i++) {
//            double time = i * .02; // milliseconds
//            jaci.pathfinder.Trajectory.Segment segment = trajectory.get(i);
//
//            // find curvature
//            double curvature;
//            if (i == 0)
//                curvature = calculateCurvature(segment, trajectory.get(i + 1), trajectory.get(i + 2));
//            else if (i == trajectory.length() - 1)
//                curvature = calculateCurvature(trajectory.get(i - 2), trajectory.get(i - 1), segment);
//            else
//                curvature = calculateCurvature(trajectory.get(i - 1), segment, trajectory.get(i + 1));
//
//            states.add(new Trajectory.State(time,
//                    segment.velocity,
//                    segment.acceleration,
//                    new Pose2d(segment.x, segment.y, Rotation2d.fromDegrees(Math.toDegrees(segment.heading))),
//                    curvature));
//        }
//
        return new Trajectory(states);
    }

    public TrajectorySample sample(double dt) {
        double time = 0;
        for (int i = 0; i < trajectories.size(); i++) {
            Trajectory trajectory = trajectories.get(i);
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
