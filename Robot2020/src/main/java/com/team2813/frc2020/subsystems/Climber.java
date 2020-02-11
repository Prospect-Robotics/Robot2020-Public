package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.solenoid.PistonSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class Climber extends Subsystem {
    private final Button CLIMBER_BUTTON = SubsystemControlsConfig.getClimberPiston();
    private final Solenoid CLIMBER_PISTON = new Solenoid(0);
    private final SparkMaxWrapper motor = MotorConfigs.sparks.get("climber");

    @Override
    public void outputTelemetry() {
    }

    @Override
    public void teleopControls() {
//        CLIMBER_BUTTON.whenPressed(() -> {
//            System.out.println("climber test");
//            CLIMBER_PISTON.set(CLIMBER_PISTON.get() == PistonSolenoid.PistonState.EXTENDED ? PistonSolenoid.PistonState.RETRACTED : PistonSolenoid.PistonState.EXTENDED);
//        });
        motor.set(SubsystemControlsConfig.getOperatorJoystick().getRawAxis(1));

        CLIMBER_BUTTON.whenPressed(() -> {
            CLIMBER_PISTON.set(!CLIMBER_PISTON.get());
//            System.out.println(CLIMBER_PISTON.get() == PistonSolenoid.PistonState.RETRACTED);
//            CLIMBER_PISTON.set(PistonSolenoid.PistonState.EXTENDED);
        });
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
}
