package com.team2813.frc2020.subsystems;

import com.team2813.lib.controls.Axis;
import com.team2813.lib.controls.Button;
import com.team2813.lib.controls.Controller;

/**
 * Stores all of the controllers and their buttons and axes.
 * Controllers are private as they should never be accessed,
 * to make it easier to move a Button or Axis to a different
 * controller.
 *
 * To add a button or axis: construct a private Button or Axis
 * using controller.button(id). Then add a package-private
 * getter.
 */
class SubsystemControlsConfig {

	private static Controller driveJoystick = new Controller(0);
	private static Button pivotButton = driveJoystick.button(1);
	private static Button autoButton = driveJoystick.button(2);
	private static Button intakeButton = driveJoystick.button(3);
	private static Button magButton = driveJoystick.button(5);
	private static Button magReverse = driveJoystick.button(6);
	private static Axis driveX = driveJoystick.axis(0);
	private static Axis driveY = driveJoystick.axis(3);
	private static Axis driveSteer = driveJoystick.axis(0);
	private static Axis driveForward = driveJoystick.axis(3);
	private static Axis driveReverse = driveJoystick.axis(2);

	static Button getIntakeButton() {
        return intakeButton;
    }

	static Button getMagReverse() {
		return magReverse;
	}

	static Button getMagButton() {
		return magButton;
	}

	static Button getPivotButton() {
		return pivotButton;
	}

	static Button getAutoButton() {
		return autoButton;
	}

	static Axis getDriveX() {
		return driveX;
	}

	static Axis getDriveY() {
		return driveY;
	}

	static Axis getDriveSteer() {
		return driveSteer;
	}

	static Axis getDriveForward() {
		return driveForward;
	}

	static Axis getDriveReverse() {
		return driveReverse;
	}
}