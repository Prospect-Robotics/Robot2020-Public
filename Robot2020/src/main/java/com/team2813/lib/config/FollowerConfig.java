package com.team2813.lib.config;


import com.team2813.lib.motors.SparkMaxWrapper.InvertType;

public class FollowerConfig {
	private int id;

    private MotorType motorType;

    private MotorControllerType motorControllerType;
    private InvertType inverted = InvertType.FOLLOW_LEADER;

	public void setMotorControllerType(MotorControllerType motorControllerType) {
		this.motorControllerType = motorControllerType;
	}



	public MotorControllerType getMotorControllerType() {
		return motorControllerType;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MotorType getMotorType() {
		return motorType;
	}

	public void setMotorType(MotorType motorType) {
		this.motorType = motorType;
	}

	public InvertType getInverted() {
		return inverted;
	}

	public void setInverted(InvertType inverted) {
		this.inverted = inverted;
	}
}
