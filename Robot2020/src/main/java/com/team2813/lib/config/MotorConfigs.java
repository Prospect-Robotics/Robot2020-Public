package com.team2813.lib.config;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.team2813.lib.ctre.CTREException;
import com.team2813.lib.ctre.PIDProfile;
import com.team2813.lib.ctre.TalonWrapper;
import com.team2813.lib.ctre.VictorWrapper;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import edu.wpi.first.wpilibj.Filesystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// singleton for motor configs
public class MotorConfigs {
	public static Map<String, CANSparkMaxWrapper> sparks = new HashMap<>();
	public static Map<String, TalonWrapper> talons = new HashMap<>();
	public static Map<String, VictorWrapper> victors = new HashMap<>();

	public static RootConfigs motorConfigs;

	private static List<Integer> ids = new ArrayList<>();

	public static void read() throws IOException {
		File deployDirectory = Filesystem.getDeployDirectory();
		File configFile = new File(deployDirectory.getAbsolutePath() + "/motorConfig.yaml");

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		motorConfigs = mapper.readValue(configFile, RootConfigs.class);

		motorConfigs.getSparks().forEach(((s, sparkConfig) -> sparks.put(s, initializeSpark(sparkConfig))));
		motorConfigs.getVictors().forEach(((s, victorConfig) -> victors.put(s, initializeVictor(victorConfig))));

		System.out.println("Successful!");
	}

	private static <TalonException extends Throwable> TalonWrapper initializeTalon(TalonConfig config) throws TalonException, CTREException, SparkMaxException {
		for (Integer id : ids)
			if (id == config.getDeviceNumber()) {
				System.err.println("Tried to register talon with already used id");
			}
		ids.add(config.getDeviceNumber());

		System.out.println("Configuring" + config.getSubsystemName());

		TalonWrapper talon = new TalonWrapper(config.getDeviceNumber(), config.getSubsystemName());

		talon.setFactoryDefaults();

		talon.setPeakCurrentLimit(config.getPeakCurrentLimit());
		talon.enableVoltageCompensation();


		talon.setOpenLoopRamp(config.getOpenLoopRampRate());
		talon.setClosedLoopRamp(config.getClosedLoopRampRate());

		talon.setStatusFramePeriod(config.getStatusFrame(), config.getStatusFramePeriod());
//					talon.setSmartMotionMaxVelocity(config.motionCruiseVelocity()); // FIXME: 09/20/2019 need to change parameters/types
//					talon.setSmartMotionMaxAccel(config.motionAcceleration()); // FIXME: 09/20/2019 need to change parameters/types

		talon.setContinuousCurrentLimit(config.getContinuousCurrentLimitAmps());// TODO check this is actually continuous limit

//			for (com.team2813.lib.talon.options.HardLimitSwitch hardLimitSwitch : field.getAnnotationsByType(com.team2813.lib.talon.options.HardLimitSwitch.class)) {
//				System.out.println("\tconfiguring hard limit switch " + hardLimitSwitch.direction());
//				// FIXME remake limit switch stuff differently since it is called differently -- Grady 10/30 I'm not sure this is how it works for Spark Maxs
//			}

		for (LimitSwitchConfig limitSwitch : config.getLimitSwitches()) {
			talon.setLimitSwitchSource(limitSwitch.direction, LimitSwitchSource.FeedbackConnector, limitSwitch.polarity.ctre);
			talon.setClearPositionOnLimit(limitSwitch.direction, limitSwitch.clearOnLimit);
			talon.enableLimitSwitches();
		}

		for (SoftLimitConfig softLimit : config.getSoftLimits()) {
			talon.setSoftLimit(softLimit.direction, softLimit.threshold, softLimit.enable);
			talon.setClearPositionOnLimit(softLimit.direction, softLimit.clearOnLimit);
		}

//
//			for (com.team2813.lib.talon.options.SoftLimit softLimit : field.getAnnotationsByType(com.team2813co.lib.talon.options.SoftLimit.class)) {
//				System.out.println("\tconfiguring soft limit " + softLimit.direction());
//
//				//FIXME remake limit switch stuff differently
//			}


		for (
				  PIDControllerConfig pidController : config.getPidControllers()) {
			PIDProfile.Profile slotID = config.getPidControllers().indexOf(pidController) == 0 ?
					  PIDProfile.Profile.PRIMARY : PIDProfile.Profile.SECONDARY;
			talon.setPIDF(slotID, pidController.getP(), pidController.getI(),
					  pidController.getD(), pidController.getF());
			talon.setMotionMagicCruiseVelocity((int) pidController.getMaxVelocity()); // FIXME: 1/3/2020 Casting because
			// talon uses encoder ticks
			// TODO deal with units issue
			talon.setMotionMagicAcceleration((int) pidController.getMaxAcceleration()); // FIXME see above
			// TODO: 1/3/2020 figure out min velocity with Talons / remove from PID controller so as not to have that attribute
		}


		Inverted inverted = config.getInverted();
		if (inverted != null)
			talon.setInverted(inverted.value);

		for (FollowerConfig followerConfig : config.getFollowers()) {
			System.out.println(
					  "\tCreating follower w/ id of " + followerConfig.getId() + " on " + config.getSubsystemName()
			);

			if (followerConfig.getMotorControllerType() == MotorControllerType.TALON_SRX) {
			    talon.set(ControlMode.Follower, followerConfig.getId());

			    TalonSRX follower = new TalonSRX(followerConfig.getId());
                follower.follow(talon.motorController);
                if (followerConfig.getInverted() == CANSparkMaxWrapper.InvertType.OPPOSE_LEADER) {
                    follower.setInverted(InvertType.OpposeMaster);
                } else {
                    follower.setInverted(InvertType.FollowMaster);
                }
            } else if (followerConfig.getMotorControllerType() == MotorControllerType.VICTOR_SPX) {
                VictorSPX follower = new VictorSPX(followerConfig.getId());
                follower.follow(talon.motorController);
                if (followerConfig.getInverted() == CANSparkMaxWrapper.InvertType.OPPOSE_LEADER) {
                    follower.setInverted(InvertType.OpposeMaster);
                } else {
                    follower.setInverted(InvertType.FollowMaster);
                }
            }
		}

		return talon;
	}

	private static CANSparkMaxWrapper initializeSpark(SparkConfig config) {
		for (Integer id : ids)
			if (id == config.getDeviceNumber()) {
				System.err.println("Tried to register spark max with already used id");
			}

		ids.add(config.getDeviceNumber());

		System.out.println("Configuring " + config.getSubsystemName());

		CANSparkMaxWrapper spark = new CANSparkMaxWrapper(config.getDeviceNumber(), config.getSubsystemName(), config.getType().getValue());

		try {
			spark.factoryDefault();

//					spark.setPeakCurrentDuration(config.peakCurrentDuration()); // FIXME: 09/20/2019 for the spark
			spark.setCurrLimit(config.getPeakCurrentLimit());

			spark.enableVoltageCompensation(config.getCompSaturationVoltage());

			spark.setOpenLoopRamp(config.getOpenLoopRampRate());
			spark.setClosedLoopRamp(config.getClosedLoopRampRate());

			spark.setPeriodicFrame(config.getStatusFrame().getValue(), config.getStatusFramePeriod());

//					spark.setSmartMotionMaxVelocity(config.motionCruiseVelocity()); // FIXME: 09/20/2019 need to change parameters/types
//					spark.setSmartMotionMaxAccel(config.motionAcceleration()); // FIXME: 09/20/2019 need to change parameters/types

			spark.setSecondaryCurrLimit(config.getContinuousCurrentLimitAmps());// TODO check this is actually continuous limit

//			for (com.team2813.lib.sparkMax.options.HardLimitSwitch hardLimitSwitch : field.getAnnotationsByType(com.team2813.lib.sparkMax.options.HardLimitSwitch.class)) {
//				System.out.println("\tconfiguring hard limit switch " + hardLimitSwitch.direction());
//				// FIXME remake limit switch stuff differently since it is called differently -- Grady 10/30 I'm not sure this is how it works for Spark Maxs
//			}
//
//			for (com.team2813.lib.sparkMax.options.SoftLimit softLimit : field.getAnnotationsByType(com.team2813.lib.sparkMax.options.SoftLimit.class)) {
//				System.out.println("\tconfiguring soft limit " + softLimit.direction());
//
//				//FIXME remake limit switch stuff differently
//			}


			for (PIDControllerConfig pidController : config.getPidControllers()) {
				int slotID = config.getPidControllers().indexOf(pidController);
				spark.setPIDF(slotID, pidController.getP(), pidController.getI(),
						  pidController.getD(), pidController.getF());
				spark.getPIDController().setSmartMotionMaxVelocity(pidController.getMaxVelocity(), slotID);
				spark.getPIDController().setSmartMotionMaxAccel(pidController.getMaxAcceleration(), slotID);
				spark.getPIDController().setSmartMotionMinOutputVelocity(pidController.getMinVelocity(), slotID);
			}


			Inverted inverted = config.getInverted();

			if (inverted != null)
				spark.setInverted(inverted == Inverted.INVERTED);
			else
				spark.setInverted(com.team2813.lib.sparkMax.CANSparkMaxWrapper.InvertType.NORMAL.inverted);

			for (FollowerConfig followerConfig : config.getFollowers()) {
				System.out.println(
						  "\tCreating follower w/ id of " + followerConfig.getId() + " on " + config.getSubsystemName()
				);
				CANSparkMaxWrapper sparkMaxFollower = new CANSparkMaxWrapper(followerConfig.getId(), followerConfig.getMotorType().getValue());
				sparkMaxFollower.follow(spark, followerConfig.getInverted().inverted);
			}

			spark.setConfig(config);
		} catch (SparkMaxException e) {
			e.printStackTrace();
		}

		return spark;
	}

	private static VictorWrapper initializeVictor(VictorConfig config) {

		for (Integer id : ids)
			if (id == config.getDeviceNumber()) {
				System.err.println("Tried to register VICTOR SPX with already used id");
			}

		ids.add(config.getDeviceNumber());

		System.out.println("Configuring " + config.getSubsystemName());
		// TODO IMPLEMENT OPTIONS
		return new VictorWrapper(config.getDeviceNumber(), config.getSubsystemName());
	}

	@SuppressWarnings({"unused", "WeakerAccess"})
	public static class RootConfigs {
		private Map<String, SparkConfig> sparks;
		private Map<String, TalonConfig> talons;
		private Map<String, VictorConfig> victors;

		public Map<String, SparkConfig> getSparks() {
			return sparks;
		}

		public void setSparks(Map<String, SparkConfig> sparks) {
			this.sparks = sparks;
		}

		public Map<String, TalonConfig> getTalons() {
			return talons;
		}

		public void setTalons(Map<String, TalonConfig> talons) {
			this.talons = talons;
		}

		public Map<String, VictorConfig> getVictors() {
			return victors;
		}

		public void setVictors(Map<String, VictorConfig> victors) {
			this.victors = victors;
		}
	}
}
