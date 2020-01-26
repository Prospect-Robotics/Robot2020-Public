package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.TalonFXWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;

public class Magazine extends Subsystem {

    private final SparkMaxWrapper MOTOR;
    private final TalonFXWrapper WHEEL;
    private final Button START_STOP_BUTTON = SubsystemControlsConfig.getMagButton();
    private final Button REVERSE_BUTTON = SubsystemControlsConfig.getMagReverse();

    private final ColorSensorV3 colorSensor;
    private final ColorMatch colorMatcher = new ColorMatch();
    private final Color yellow = ColorMatch.makeColor(.333, .57, .09);
    private final Color blue = ColorMatch.makeColor(0.17, 0.4, 0.4);
    private final Color red = ColorMatch.makeColor(0.65, 0.31,.08);
    private final Color green = ColorMatch.makeColor(0.13, 0.69, 0.17);

    public int ammo = 0;
    public boolean triggered = false;

    private Demand demand;

    Magazine() {
        MOTOR = MotorConfigs.sparks.get("magazine");
        demand = Demand.OFF;
        WHEEL = (TalonFXWrapper) MotorConfigs.talons.get("magazineWheel");
        colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
        colorMatcher.addColorMatch(yellow);
        colorMatcher.addColorMatch(blue);
        colorMatcher.addColorMatch(red);
        colorMatcher.addColorMatch(green);
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("Color Sensor Proximity", colorSensor.getProximity());
        SmartDashboard.putNumber("Color Sensor B", colorSensor.getBlue());
        SmartDashboard.putNumber("Color Sensor R", colorSensor.getRed());
        SmartDashboard.putNumber("Color Sensor G", colorSensor.getGreen());
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);
        if (match.color == yellow) {
            SmartDashboard.putString("Color Match", "yellow");
        } else if (match.color == red) {
            SmartDashboard.putString("Color Match", "red");
        } else if (match.color == green) {
            SmartDashboard.putString("Color Match", "green");
        } else if (match.color == blue) {
            SmartDashboard.putString("Color Match", "Blue");
        } else {
            SmartDashboard.putString("Color Match", "Unknown");
        }
        SmartDashboard.putNumber("Color Confidence", match.confidence);
        SmartDashboard.putString("Detected Color", "R: " + detectedColor.red + " G: " + detectedColor.green + " B: " + detectedColor.blue);

        SmartDashboard.putNumber("Ammo", ammo);
    }

    @Override
    public void teleopControls() {
        START_STOP_BUTTON.whenPressed(() -> {
            demand = demand == Demand.ON ? Demand.OFF : Demand.ON;
        });
        REVERSE_BUTTON.whenPressedReleased(() -> demand = Demand.REV, () -> demand = Demand.OFF);
    }

    @Override
    public void onEnabledStart(double timestamp) {

    }

    @Override
    public void onEnabledLoop(double timestamp) {
    }

    @Override
    public void onEnabledStop(double timestamp) {

    }

    @Override
    public void readPeriodicInputs() {
        if (colorSensor.getProximity() > 256) {
            if (!triggered) { // block only runs once
                ammo++;
            }
            demand = Demand.ON;
            triggered = true;
        } else triggered = false;
        demand = Demand.OFF;
    }

    @Override
    protected void writePeriodicOutputs() {
        MOTOR.set(ControlMode.DUTY_CYCLE, demand.percent);
        if(demand == Demand.ON) {
            WHEEL.set(ControlMode.DUTY_CYCLE, demand.percent);
        } else {
            WHEEL.set(ControlMode.DUTY_CYCLE, Demand.OFF.percent);
        }
    }

    enum Demand {
        ON(0.5), OFF(0.0), REV(-0.3), FEED(.8);

        double percent;

        Demand(double percent) {
            this.percent = percent;
        }
    }
}