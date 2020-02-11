package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfig;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.solenoid.PistonSolenoid;

public class Climber extends Subsystem1d<Climber.Position> {
    private final Button CLIMBER_BUTTON = SubsystemControlsConfig.getClimberPiston();
    private final PistonSolenoid CLIMBER_PISTON = new PistonSolenoid(0);
    private final SparkMaxWrapper motor;

    Climber() {
        super(MotorConfigs.sparks.get("climber"));
        this.motor = MotorConfigs.sparks.get("climber");
    }

    Climber(SparkMaxWrapper motor) {
        super(motor);
        this.motor = motor;
    }

    @Override
    public void outputTelemetry() {
    }

    @Override
    public void teleopControls() {
        // todo replace with position
        motor.set(SubsystemControlsConfig.getOperatorJoystick().getRawAxis(1));

        CLIMBER_BUTTON.whenPressed(CLIMBER_PISTON::toggle);
    }

    @Override
    public void onEnabledStart(double timestamp) {

    }

    @Override
    public void onEnabledLoop(double timestamp) {
    }

    @Override
    public void onEnabledStop(double timestamp) {
        CLIMBER_PISTON.retract();
    }

    @Override
    void setNextPosition(boolean clockwise) {
    }

    @Override
    void setNextPosition(Position position) {
        setPosition(position == Position.HIGH ? Position.LOW : Position.HIGH);
    }

    public enum Position implements Subsystem1d.Position<Climber.Position> {
        LOW(0), HIGH(0);

        private double position;

        Position(double position) {
            this.position = position;
        }

        @Override
        public double getPos() {
            return position;
        }

        @Override
        public Position getNextClockwise() {
            return null;
        }

        @Override
        public Position getNextCounter() {
            return null;
        }

        @Override
        public Position getMin() {
            return null;
        }

        @Override
        public Position getMax() {
            return null;
        }
    }
}
