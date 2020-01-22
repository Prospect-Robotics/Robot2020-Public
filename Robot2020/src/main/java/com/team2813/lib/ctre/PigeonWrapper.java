package com.team2813.lib.ctre;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.*;
import com.team2813.lib.motors.TalonWrapper;

public class PigeonWrapper extends PigeonIMU {
	String subsystemName;

	public PigeonWrapper(int deviceNumber, String subsystemName) {
		super(deviceNumber);
		this.subsystemName = subsystemName;
	}

	public PigeonWrapper(TalonWrapper talon) {
		super((TalonSRX) talon.controller); // this could throw castexception
		subsystemName = talon.subsystemName;
	}

	public double getHeading() {
		return getFusedHeading();
	}

}
