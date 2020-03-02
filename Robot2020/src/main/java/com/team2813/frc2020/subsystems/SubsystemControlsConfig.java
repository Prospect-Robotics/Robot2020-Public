package com.team2813.frc2020.subsystems;

import com.team2813.lib.controls.Axis;
import com.team2813.lib.controls.Button;
import com.team2813.lib.controls.Controller;
import com.team2813.lib.controls.POV;
import edu.wpi.first.wpilibj.Joystick;

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

	// driver
	private static Controller driveJoystick = new Controller(0);
	private static Button pivotButton = driveJoystick.button(1);
	private static Button autoButton = driveJoystick.button(2);
	private static Button intakeDeployButton = driveJoystick.button(4);
	private static Button intakeSpinButton = driveJoystick.button(3);
	private static Button hoodButton = driveJoystick.button(3);
	private static Button magButton = driveJoystick.button(5);
	private static Axis driveX = driveJoystick.axis(0);
	private static Axis driveY = driveJoystick.axis(3);
	private static Axis driveSteer = driveJoystick.axis(0);
	private static Axis driveForward = driveJoystick.axis(3);
	private static Axis driveReverse = driveJoystick.axis(2);

	// operator
	private static Controller operatorJoystick = new Controller(1);
	private static Button climberPiston = operatorJoystick.button(2);
	private static Button intakeIn = operatorJoystick.button(5);
	private static Button intakeOut = operatorJoystick.button(6);
	private static Button shooterButton = operatorJoystick.button(4);
	private static Button climberButton = operatorJoystick.button(11);
	private static Axis climberElevator = operatorJoystick.axis(1);
	private static POV climberElevator_ = operatorJoystick.pov();
	private static Button climberDisable = operatorJoystick.button(1);
	private static Button hoodInitiation = operatorJoystick.button(9);
	private static Button hoodTrench = operatorJoystick.button(10);
	private static Button magForward = operatorJoystick.button(7);
	private static Button magReverse = operatorJoystick.button(8);

	static Button getIntakeDeployButton() {
        return intakeDeployButton;
    }

	static Button getIntakeSpinButton() {
		return intakeSpinButton;
	}

	static Button getMagForward() {
		return magForward;
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

	static Button getHoodButton() { return hoodButton; }

	static Button getShooterButton() { return shooterButton; }

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

	static Button getIntakeIn() {
		return intakeIn;
	}

	static Button getIntakeOut() {
		return intakeOut;
	}

	static Button getClimberPiston() {
		return climberPiston;
	}

	static Joystick getOperatorJoystick() {
		return operatorJoystick;
	}

	static Button getClimberButton() { return climberButton; }

	public static Axis getClimberElevator() {
		return climberElevator;
	}

	public static POV getClimberElevator_() { return climberElevator_; }

	public static Button getClimberDisable() {
		return climberDisable;
	}

	public static Button getHoodInitiation() {
		return hoodInitiation;
	}

	public static Button getHoodTrench() {
		return hoodTrench;
	}
}