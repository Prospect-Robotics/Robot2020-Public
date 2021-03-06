//package com.team2813.frc2020.subsystems;
//
//import com.team2813.frc2020.loops.Loop;
//import com.team2813.lib.ctre.CTREException;
//import com.team2813.lib.sparkMax.SparkMaxException;
//
///**
// * TODO rewrite this documentation block
// * The Subsystem abstract class, which serves as a basic framework for all robot subsystems. Each subsystem outputs
// * commands to SmartDashboard, has a stop routine (for after each match), and a routine to zero all sensors, which helps
// * with calibration.
// * <p>
// * All Subsystems only have one instance (after all, one robot does not have two drivetrains), and functions get the
// * instance of the drivetrain and act accordingly. Subsystems are also a state machine with a desired state and actual
// * state; the robot code will try to match the two states with actions. Each Subsystem also is responsible for
// * instantializing all member components at the start of the match.
// */
//public abstract class SubsystemOld implements Loop {
//
//	public void writeToLog() {
//	}
//
//	/**
//	 * Read in current status from motors
//	 *
//	 * Optional design pattern for caching periodic reads to avoid hammering the HAL/CAN.
//	 *
//	 * @throws CTREException
//	 */
//	protected void readPeriodicInputs_() throws CTREException, SparkMaxException {
//	}
//
//	/**
//	 * Catches the CTREException to avoid having to catch or throw where method is called
//	 */
//	private final void readPeriodicInputs() {
//		try {
//			readPeriodicInputs_();
//		}
//		catch (CTREException | SparkMaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Output to motors
//	 *
//	 * Optional design pattern for caching periodic writes to avoid hammering the HAL/CAN.
//	 *
//	 * @throws CTREException
//	 */
//	protected void writePeriodicOutputs_() throws CTREException, SparkMaxException {
//	}
//
//
//	/**
//	 * Catches the CTREException to avoid having to catch or throw where method is called
//	 */
//	private final void writePeriodicOutputs() {
//		try {
//			writePeriodicOutputs_();
//		}
//		catch (CTREException | SparkMaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//
//	// TODO remove checkSystem or use it for a good purpose
//	protected abstract boolean checkSystem_() throws CTREException, SparkMaxException;
//
//	public final void checkSystem() {
//		try {
//			checkSystem_();
//		}
//		catch (CTREException | SparkMaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	protected abstract void outputTelemetry_() throws CTREException, SparkMaxException;
//
//	public final void outputTelemetry() {
//		try {
//			outputTelemetry_();
//		}
//		catch (CTREException | SparkMaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	protected void zeroSensors_() throws CTREException, SparkMaxException {
//	}
//
//	public final void zeroSensors() {
//		try {
//			zeroSensors_();
//		}
//		catch (CTREException | SparkMaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	protected void constructorFailed() {
//		System.exit(1);
//	}
//
//	protected abstract void teleopControls_() throws CTREException, SparkMaxException;
//
//	public final void teleopControls() {
//		try {
//			teleopControls_();
//		} catch (SparkMaxException | CTREException e) {
//			e.printStackTrace();
//		}
//	}
//
//	//#region Looping
//
//	protected abstract void onEnabledStart_(double timestamp) throws CTREException, SparkMaxException;
//
//	@Override
//	public synchronized final void onEnabledStart(double timestamp) {
//		System.out.println("Subsystem OnEnabledStart");
//		try {
//			onEnabledStart_(timestamp);
//		}
//		catch (CTREException | SparkMaxException e) {
//			e.printStackTrace();
//		}
//	}
//
//	protected abstract void onEnabledLoop_(double timestamp) throws CTREException, SparkMaxException;
//
//	@Override
//	public synchronized final void onEnabledLoop_(double timestamp) {
//		readPeriodicInputs();
//		try {
//			onEnabledLoop_(timestamp);
//			// System.out.println("Subsystem onEnabledLoop");
//		}
//		catch (CTREException | SparkMaxException e) {
//			e.printStackTrace();
//		}
//		writePeriodicOutputs();
//	}
//
//	protected abstract void onEnabledStop_(double timestamp) throws CTREException, SparkMaxException;
//
//	@Override
//	public synchronized final void onEnabledStop(double timestamp) {
//		try {
//			onEnabledStop_(timestamp);
//		}
//		catch (CTREException | SparkMaxException e) {
//			e.printStackTrace();
//		}
//	}
//
//	protected void onDisabledStart_(double timestamp) throws CTREException, SparkMaxException {};
//
//	@Override
//	public synchronized final void onDisabledStart(double timestamp) {
//		try {
//			onDisabledStart_(timestamp);
//		}
//		catch (CTREException | SparkMaxException e) {
//			e.printStackTrace();
//		}
//	}
//
//	protected void onDisabledLoop_(double timestamp) throws CTREException, SparkMaxException {};
//
//	@Override
//	public synchronized final void onDisabledLoop(double timestamp) {
//		readPeriodicInputs();
//		// System.out.println("Subsystem onDisabledLoop");
//		try {
//			onDisabledLoop_(timestamp);
//		}
//		catch (CTREException | SparkMaxException e) {
//			e.printStackTrace();
//		}
//		writePeriodicOutputs();
//	}
//
//	protected void onAnyLoop_(double timestamp) throws CTREException, SparkMaxException {}
//
//	@Override
//	public synchronized final void onAnyLoop(double timestamp) {
//		try {
//			onAnyLoop_(timestamp);
//		} catch (CTREException | SparkMaxException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//	//#endregion
//}