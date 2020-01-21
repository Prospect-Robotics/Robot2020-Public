package com.team2813.lib.ctre;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.*;
import com.team2813.lib.motors.TalonWrapper;

public class PigeonWrapper {

	String subsystemName;
	PigeonIMU pigeon;

	public PigeonWrapper(int deviceNumber, String subsystemName) {
		pigeon = new PigeonIMU(deviceNumber);
		this.subsystemName = subsystemName;
	}

	public PigeonWrapper(TalonWrapper talon) {
		pigeon = new PigeonIMU((TalonSRX) talon.controller);// This could throw a ClassCastException if used with a Talon FX
		subsystemName = talon.subsystemName;
	}

	public PigeonIMU getPigeon() {
		return pigeon;
	}

	/**
	 * Get the absolute compass heading.
	 * @return compass heading [0,360) degrees.
	 */
	public double getAbsoluteCompassHeading() throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getAbsoluteCompassHeading());
	}

	/**
	 * Get the continuous compass heading.
	 * @return continuous compass heading [-23040, 23040) degrees. Use
	 *         SetCompassHeading to modify the wrap-around portion.
	 */
	public double getCompassHeading() throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getCompassHeading());
	}

	/**
	 * Gets the compass' measured magnetic field strength.
	 * @return field strength in Microteslas (uT).
	 */
	public double getCompassFieldStrength() throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getCompassFieldStrength());
	}

	/**
	 * Gets the temperature of the pigeon.
	 *
	 * @return Temperature in ('C)
	 */
	public double getTemp() throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getTemp());
	}

	/**
	 * Gets the current Pigeon state
	 *
	 * @return PigeonState enum
	 */
	public PigeonIMU.PigeonState getState() throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getState());
	}

	/**
	 * Gets the current Pigeon uptime.
	 *
	 * @return How long has Pigeon been running in whole seconds. Value caps at
	 *         255.
	 */
	public int getUpTime() throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getUpTime());
	}

	/**
	 * Gets the Fused Heading
	 *
	 * @return The fused heading in degrees.
	 */
	public double getFusedHeading() throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getFusedHeading());
	}

	/**
	 * Gets the firmware version of the device.
	 *
	 * @return param holds the firmware version of the device. Device must be powered
	 * cycled at least once.
	 */
	public int getFirmwareVersion() throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getFirmwareVersion());
	}

	/**
	 * @return true iff a reset has occurred since last call.
	 */
	public boolean hasResetOccurred() throws CTREException {
		return throwIfNotOkElseReturn(pigeon.hasResetOccurred());
	}

	/**
	 * Gets the value of a custom parameter. This is for arbitrary use.
	 *
	 * Sometimes it is necessary to save calibration/declination/offset
	 * information in the device. Particularly if the
	 * device is part of a subsystem that can be replaced.
	 *
	 * @param paramIndex
	 *            Index of custom parameter. [0-1]
	 * @param timoutMs
	 *            Timeout value in ms. If nonzero, function will wait for
	 *            config success and report an error if it times out.
	 *            If zero, no blocking or checking is performed.
	 * @return Value of the custom param.
	 */
	public int configGetCustomParam(int paramIndex, int timoutMs) throws CTREException {
		return throwIfNotOkElseReturn(pigeon.configGetCustomParam(paramIndex, timoutMs));
	}

	/**
	 * Gets the value of a custom parameter. This is for arbitrary use.
	 *
	 * Sometimes it is necessary to save calibration/declination/offset
	 * information in the device. Particularly if the
	 * device is part of a subsystem that can be replaced.
	 *
	 * @param paramIndex
	 *            Index of custom parameter. [0-1]
	 * @return Value of the custom param.
	 */
	public int configGetCustomParam(int paramIndex) throws CTREException {
		return throwIfNotOkElseReturn(pigeon.configGetCustomParam(paramIndex));
	}

	/**
	 * Gets a parameter. Generally this is not used.
	 * This can be utilized in
	 * - Using new features without updating API installation.
	 * - Errata workarounds to circumvent API implementation.
	 * - Allows for rapid testing / unit testing of firmware.
	 *
	 * @param param
	 *            Parameter enumeration.
	 * @param ordinal
	 *            Ordinal of parameter.
	 * @param timeoutMs
	 *            Timeout value in ms. If nonzero, function will wait for
	 *            config success and report an error if it times out.
	 *            If zero, no blocking or checking is performed.
	 * @return Value of parameter.
	 */
	public double configGetParameter(ParamEnum param, int ordinal, int timeoutMs) throws CTREException {
		return throwIfNotOkElseReturn(pigeon.configGetParameter(param, ordinal, timeoutMs));
	}

	/**
	 * Gets a parameter. Generally this is not used.
	 * This can be utilized in
	 * - Using new features without updating API installation.
	 * - Errata workarounds to circumvent API implementation.
	 * - Allows for rapid testing / unit testing of firmware.
	 *
	 * @param param
	 *            Parameter enumeration.
	 * @param ordinal
	 *            Ordinal of parameter.
	 * @return Value of parameter.
	 */
	public double configGetParameter(ParamEnum param, int ordinal ) throws CTREException {
		return throwIfNotOkElseReturn(pigeon.configGetParameter(param, ordinal));
	}

	/**
	 * Gets a parameter.
	 *
	 * @param param
	 *            Parameter enumeration.
	 * @param ordinal
	 *            Ordinal of parameter.
	 * @param timeoutMs
	 *            Timeout value in ms. If nonzero, function will wait for
	 *            config success and report an error if it times out.
	 *            If zero, no blocking or checking is performed.
	 * @return Value of parameter.
	 */
	public double configGetParameter(int param, int ordinal, int timeoutMs) throws CTREException {
		return throwIfNotOkElseReturn(pigeon.configGetParameter(param, ordinal, timeoutMs));
	}

	/**
	 * Gets a parameter.
	 *
	 * @param param
	 *            Parameter enumeration.
	 * @param ordinal
	 *            Ordinal of parameter.
	 * @return Value of parameter.
	 */
	public double configGetParameter(int param, int ordinal) throws CTREException {
		return throwIfNotOkElseReturn(pigeon.configGetParameter(param, ordinal));
	}

	/**
	 * Gets the period of the given status frame.
	 *
	 * @param frame
	 *            Frame to get the period of.
	 * @param timeoutMs
	 *            Timeout value in ms. If nonzero, function will wait for
	 *            config success and report an error if it times out.
	 *            If zero, no blocking or checking is performed.
	 * @return Period of the given status frame.
	 */
	public int getStatusFramePeriod(PigeonIMU_StatusFrame frame, int timeoutMs) throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getStatusFramePeriod(frame, timeoutMs));
	}

	/**
	 * Gets the period of the given status frame.
	 *
	 * @param frame
	 *            Frame to get the period of.
	 * @return Period of the given status frame.
	 */
	public int getStatusFramePeriod(PigeonIMU_StatusFrame frame) throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getStatusFramePeriod(frame));
	}

	/**
	 * @return The Device Number
	 */
	public int getDeviceID() throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getDeviceID());
	}

	/**
	 * Gets all persistant settings.
	 *
	 * @param allConfigs        Object with all of the persistant settings
	 * @param timeoutMs
	 *              Timeout value in ms. If nonzero, function will wait for
	 *              config success and report an error if it times out.
	 *              If zero, no blocking or checking is performed.
	 */
	public void getAllConfigs(PigeonIMUConfiguration allConfigs, int timeoutMs) throws CTREException {
		pigeon.getAllConfigs(allConfigs, timeoutMs);
		throwLastError();
	}

	/**
	 * Gets all persistant settings (overloaded so timeoutMs is 50 ms).
	 *
	 * @param allConfigs        Object with all of the persistant settings
	 */
	public void getAllConfigs(PigeonIMUConfiguration allConfigs) throws CTREException {
		pigeon.getAllConfigs(allConfigs);
		throwLastError();
	}


	/**
	 * Get the current Fusion Status (including fused heading)
	 *
	 * @param toFill 	object reference to fill with fusion status flags.
	 *					Caller may pass null if flags are not needed.
	 * @return The fused heading in degrees.
	 */
	public double getFusedHeading(PigeonIMU.FusionStatus toFill) throws CTREException {
		return throwIfNotOkElseReturn(pigeon.getFusedHeading(toFill));
	}

	protected void throwIfNotOk(ErrorCode error) throws CTREException {
		CTREException.throwIfNotOk(subsystemName, error);
	}

	/**
	 * Helper function for handling talon methods that do not return an error code but still need to check for one
	 *
	 * <p>from TalonSRX.getLastError():</p>
	 * <blockquote>
	 * Gets the last error generated by this object. Not all functions return an
	 * error code but can potentially report errors. This function can be used
	 * to retrieve those error codes.
	 * </blockquote>
	 *
	 * @param value - value to return
	 * @return value
	 * @throws CTREException - if talon had error code
	 */
	protected <T> T throwIfNotOkElseReturn(T value) throws CTREException {
		throwLastError();
		return value;
	}

	//FIXME temporary fix
	public void throwLastError() throws CTREException {
		ErrorCode code = getLastError();
		if(code.value == -3) return;
		throwIfNotOk(code);
	}

	public void DestroyObject() throws CTREException {
		throwIfNotOk(pigeon.DestroyObject());
	}

	public void setYaw(double angleDeg) throws CTREException {
		throwIfNotOk(pigeon.setYaw(angleDeg, TimeoutMode.RUNNING.valueMs));
	}

	public void addYaw(double angleDeg) throws CTREException {
		throwIfNotOk(pigeon.addYaw(angleDeg, TimeoutMode.RUNNING.valueMs));
	}

	public void setYawToCompass() throws CTREException {
		throwIfNotOk(pigeon.setYawToCompass(TimeoutMode.NO_TIMEOUT.valueMs));
	}

	public void setFusedHeading(double angleDeg) throws CTREException {
		throwIfNotOk(pigeon.setFusedHeading(angleDeg, TimeoutMode.RUNNING.valueMs));
	}

	public void addFusedHeading(double angleDeg) throws CTREException {
		throwIfNotOk(pigeon.addFusedHeading(angleDeg, TimeoutMode.RUNNING.valueMs));
	}

	public void setFusedHeadingToCompass() throws CTREException {
		throwIfNotOk(pigeon.setFusedHeadingToCompass(TimeoutMode.NO_TIMEOUT.valueMs));
	}

	public void setAccumZAngle(double angleDeg) throws CTREException {
		throwIfNotOk(pigeon.setAccumZAngle(angleDeg, TimeoutMode.NO_TIMEOUT.valueMs));
	}

	/**
	 * TODO Javadoc
	 *
	 * @param disable
	 * @throws CTREException
	 */
	public void setTemperatureCompensationDisable(boolean disable) throws CTREException {
		throwIfNotOk(pigeon.setTemperatureCompensationDisable(disable, TimeoutMode.NO_TIMEOUT.valueMs));
	}

	public void setCompassDeclination(double angleDegOffset) throws CTREException {
		throwIfNotOk(pigeon.setCompassDeclination(angleDegOffset, TimeoutMode.NO_TIMEOUT.valueMs));
	}

	public void setCompassAngle(double angleDeg) throws CTREException {
		throwIfNotOk(pigeon.setCompassAngle(angleDeg, TimeoutMode.NO_TIMEOUT.valueMs));
	}

	public void enterCalibrationMode(PigeonIMU.CalibrationMode mode) throws CTREException {
		throwIfNotOk(pigeon.enterCalibrationMode(mode, TimeoutMode.NO_TIMEOUT.valueMs));
	}

	public void getGeneralStatus(PigeonIMU.GeneralStatus toFill) throws CTREException {
		throwIfNotOk(pigeon.getGeneralStatus(toFill));
	}

	public ErrorCode getLastError() {
		return pigeon.getLastError();
	}

	public void get6dQuaternion(double[] wxyz) throws CTREException {
		throwIfNotOk(pigeon.get6dQuaternion(wxyz));
	}

	/**
	 * Get Yaw, Pitch, and Roll data.
	 *
	 * @param ypr_deg Array to fill with yaw[0], pitch[1], and roll[2] data.
	 *                Yaw is within [-368,640, +368,640] degrees.
	 *                Pitch is within [-90,+90] degrees.
	 *                Roll is within [-90,+90] degrees.
	 */
	public void getYawPitchRoll(double[] ypr_deg) throws CTREException {
		throwIfNotOk(pigeon.getYawPitchRoll(ypr_deg));
	}

	/**
	 * Get AccumGyro data.
	 * AccumGyro is the integrated gyro value on each axis.
	 *
	 * @param xyz_deg Array to fill with x[0], y[1], and z[2] AccumGyro data
	 */
	public void getAccumGyro(double[] xyz_deg) throws CTREException {
		throwIfNotOk(pigeon.getAccumGyro(xyz_deg));
	}

	/**
	 * Get Raw Magnetometer data.
	 *
	 * @param rm_xyz Array to fill with x[0], y[1], and z[2] data
	 *               Number is equal to 0.6 microTeslas per unit.
	 */
	public void getRawMagnetometer(short[] rm_xyz) throws CTREException {
		throwIfNotOk(pigeon.getRawMagnetometer(rm_xyz));
	}

	/**
	 * Get Biased Magnetometer data.
	 *
	 * @param bm_xyz Array to fill with x[0], y[1], and z[2] data
	 *               Number is equal to 0.6 microTeslas per unit.
	 */
	public void getBiasedMagnetometer(short[] bm_xyz) throws CTREException {
		throwIfNotOk(pigeon.getBiasedMagnetometer(bm_xyz));
	}

	/**
	 * Get Biased Accelerometer data.
	 *
	 * @param ba_xyz Array to fill with x[0], y[1], and z[2] data.
	 *               These are in fixed point notation Q2.14.  eg. 16384 = 1G
	 */
	public void getBiasedAccelerometer(short[] ba_xyz) throws CTREException {
		throwIfNotOk(pigeon.getBiasedMagnetometer(ba_xyz));
	}

	/**
	 * Get Raw Gyro data.
	 *
	 * @param xyz_dps Array to fill with x[0], y[1], and z[2] data in degrees per second.
	 */
	public void getRawGyro(double[] xyz_dps) throws CTREException {
		throwIfNotOk(pigeon.getRawGyro(xyz_dps));
	}

	/**
	 * Get Accelerometer tilt angles.
	 *
	 * @param tiltAngles Array to fill with x[0], y[1], and z[2] angles in degrees.
	 */
	public void getAccelerometerAngles(double[] tiltAngles) throws CTREException {
		throwIfNotOk(pigeon.getAccelerometerAngles(tiltAngles));
	}

	/**
	 * Sets the value of a custom parameter. This is for arbitrary use.
	 * <p>
	 * Sometimes it is necessary to save calibration/declination/offset
	 * information in the device. Particularly if the
	 * device is part of a subsystem that can be replaced.
	 *
	 * @param newValue   Value for custom parameter.
	 * @param paramIndex Index of custom parameter. [0-1]
	 */
	public void configSetCustomParam(int newValue, int paramIndex) throws CTREException {
		throwIfNotOk(pigeon.configSetCustomParam(newValue, paramIndex, TimeoutMode.NO_TIMEOUT.valueMs));
	}

	/**
	 * Sets a parameter. Generally this is not used.
	 * This can be utilized in
	 * - Using new features without updating API installation.
	 * - Errata workarounds to circumvent API implementation.
	 * - Allows for rapid testing / unit testing of firmware.
	 *
	 * @param param    Parameter enumeration.
	 * @param value    Value of parameter.
	 * @param subValue Subvalue for parameter. Maximum value of 255.
	 * @param ordinal  Ordinal of parameter.
	 */
	public void configSetParameter(ParamEnum param, double value, int subValue, int ordinal) throws CTREException {
		throwIfNotOk(pigeon.configSetParameter(param, value, subValue, ordinal, TimeoutMode.RUNNING.valueMs));
	}


	public void setStatusFramePeriod(PigeonIMU_StatusFrame statusFrame, int periodMs) throws CTREException {
		throwIfNotOk(pigeon.setStatusFramePeriod(statusFrame, periodMs, TimeoutMode.NO_TIMEOUT.valueMs));
	}

	public void setControlFramePeriod(PigeonIMU_ControlFrame controlFrame, int periodMs) throws CTREException {
		throwIfNotOk(pigeon.setControlFramePeriod(controlFrame, periodMs));
	}

	public void getFaults(PigeonIMU_Faults toFill) throws CTREException {
		throwIfNotOk(pigeon.getFaults(toFill));
	}

	public void getStickyFaults(PigeonIMU_StickyFaults toFill) throws CTREException {
		throwIfNotOk(pigeon.getStickyFaults(toFill));
	}

	public void clearStickyFaults() throws CTREException {
		throwIfNotOk(pigeon.clearStickyFaults());
	}

	public void configAllSettings(PigeonIMUConfiguration allConfigs, int timeoutMs) throws CTREException {
		throwIfNotOk(pigeon.configAllSettings(allConfigs, timeoutMs));
	}

	public void configAllSettings(PigeonIMUConfiguration allConfigs) throws CTREException {
		throwIfNotOk(pigeon.configAllSettings(allConfigs));
	}

	public void configFactoryDefault(int timeoutMs) throws CTREException {
		throwIfNotOk(pigeon.configFactoryDefault(timeoutMs));
	}

	public void configFactoryDefault() throws CTREException {
		throwIfNotOk(pigeon.configFactoryDefault());
	}

}
