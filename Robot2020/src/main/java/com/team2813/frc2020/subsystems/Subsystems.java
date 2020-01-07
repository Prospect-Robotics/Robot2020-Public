package com.team2813.frc2020.subsystems;

import com.team2813.frc2020.Robot.RobotMode;
import com.team2813.frc2020.loops.Loop;
import com.team2813.frc2020.loops.Looper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Stores a list of all subsystems and initializes them and
 * runs a loop to put things on the SmartDashboard.
 */
public class Subsystems {

	public static List<Subsystem> allSubsystems;

	public static Drive DRIVE;
	public static GroundIntake GROUND_INTAKE;
	public static MainIntake MAIN_INTAKE;
	public static final Looper LOOPER = new Looper(RobotMode.DISABLED); //FIXME put looper somewhere else

	private static class SmartDashboardLoop implements Loop{
		int currentSubsystem = 0;

		@Override
		public void onAnyLoop(double timestamp){
			if(allSubsystems.size() == 0) return;
			if(currentSubsystem >= allSubsystems.size()) currentSubsystem = 0;
			allSubsystems.get(currentSubsystem).outputTelemetry();
			currentSubsystem++;
		}
	};

	public static void initializeSubsystems() {
		DRIVE = new Drive();
		GROUND_INTAKE = new GroundIntake();
		MAIN_INTAKE = new MainIntake();
		allSubsystems = Collections.unmodifiableList(Arrays.asList(
				DRIVE, GROUND_INTAKE, MAIN_INTAKE
		));
		LOOPER.addLoop(new SmartDashboardLoop());
	}

	public static void teleopControls() {
		for (Subsystem subsystem : allSubsystems)
			subsystem.teleopControls();
	}

	public static void outputTelemetry() {
		for (Subsystem subsystem : allSubsystems)
			subsystem.outputTelemetry();
	}
}