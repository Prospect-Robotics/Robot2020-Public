package com.team2813.lib.auto;

import com.team2813.lib.drive.DriveDemand;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

import java.util.List;

public class RamseteAuto {
    private DifferentialDriveKinematics kinematics;
    private TrajectoryConfig config;
    //    private Trajectory trajectory;
    private RamseteController controller;
    private RamseteTrajectory trajectory;

    private double timeStart = -1;

    public RamseteAuto(DifferentialDriveKinematics kinematics, RamseteTrajectory trajectory) {
        this.kinematics = kinematics;
        this.trajectory = trajectory;
        controller = new RamseteController(); // use default gains
    }

    public RamseteAuto(DifferentialDriveKinematics kinematics, List<AutoTrajectory> trajectories) {
        this(kinematics, new RamseteTrajectory(trajectories));
    }

    public RamseteAuto(DifferentialDriveKinematics kinematics, Pose2d startVector, List<Translation2d> translations, Pose2d endVector, boolean reversed) {
        this.kinematics = kinematics;

        config = new TrajectoryConfig(2, 2)
                .setReversed(reversed);
        trajectory = new RamseteTrajectory(TrajectoryGenerator.generateTrajectory(startVector, translations, endVector, config), reversed);
        controller = new RamseteController();
    }

    public RamseteAuto(DifferentialDriveKinematics kinematics, Pose2d startVector, List<Translation2d> translations, Pose2d endVector) {
        this(kinematics, startVector, translations, endVector, false);
    }

    public DriveDemand getDemand(Pose2d currentRobotPose) {
        if (timeStart == -1) {
            timeStart = System.currentTimeMillis();
        }

        double tDelta = (System.currentTimeMillis() - timeStart) / 1000;
        TrajectorySample goal = trajectory.sample(tDelta); // sample the trajectory

        if (goal.isPause()) // if there is a pause
            return new DriveDemand(0, 0);

        Pose2d robotPose = goal.isReversed() ? new Pose2d(currentRobotPose.getTranslation(), currentRobotPose.getRotation().rotateBy(Rotation2d.fromDegrees(180))) : currentRobotPose;
        ChassisSpeeds adjustedSpeeds = controller.calculate(robotPose, goal.getState());
        System.out.println("Goal: " + goal.getState().poseMeters);
        System.out.println("Robot: " + robotPose);
        DriveDemand demand = new DriveDemand(kinematics.toWheelSpeeds(adjustedSpeeds)).flip();

        return goal.isReversed() ? demand.reverse() : demand;
    }

    public Pose2d initialPose() { // because high possibility there is a pause in the beginning
        for (double i = 0; i < 15; i += .02) {
            if (!trajectory.sample(i).isPause())
                return trajectory.sample(i).getState().poseMeters;
        }
        return new Pose2d(new Translation2d(0, 0), new Rotation2d(0));
    }

    public double getTimeDelta() {
        return (System.currentTimeMillis() - timeStart) / 1000;
    }

    public void reset() {
        timeStart = -1;
    }
}
