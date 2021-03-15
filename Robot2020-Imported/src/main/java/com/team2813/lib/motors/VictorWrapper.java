package com.team2813.lib.motors;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
/*
 * getX() - general getter
 * setX() - general setter
 * booleans should also have enableX() and disableX()
 */


/**
 * @author Adrian Guerra
 */
public class VictorWrapper extends VictorSPX {
	public String subsystem;

	public VictorWrapper(int deviceNumber, String subsystemName) {
		super(deviceNumber);
		this.subsystem = subsystemName;
	}
	
	public VictorWrapper(int deviceNumber) {
		this(deviceNumber, "");
	}
}
