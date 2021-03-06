package com.team2813.frc2020.subsystems;

import com.team2813.frc2020.util.Limelight;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.solenoid.PistonSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2813.frc2020.subsystems.Subsystems.MAGAZINE;

public class Intake extends Subsystem {

    private SparkMaxWrapper INTAKE_MOTOR;
    private final Button INTAKE_PISTONS = SubsystemControlsConfig.getIntakePistons();
    private final Button INTAKE_DEPLOY_BUTTON = SubsystemControlsConfig.getIntakeDeployButton();
    private final Button INTAKE_SPIN_BUTTON = SubsystemControlsConfig.getIntakeSpinButton();
    private final Button INTAKE_IN_BUTTON = SubsystemControlsConfig.getIntakeIn();
    private final Button INTAKE_OUT_BUTTON = SubsystemControlsConfig.getIntakeOut();
    protected Demand demand = Demand.OFF;
    private boolean isAuto;

    Intake() {
        INTAKE_MOTOR = MotorConfigs.sparks.get("intake");
    }

    private Limelight limelight = Limelight.getInstance();

    private static PistonSolenoid PISTONS = new PistonSolenoid(1, 2);

    private boolean deployed = false;

    @Override
    public void outputTelemetry() {
        SmartDashboard.putBoolean("Intake Deployed", deployed);
    }

    @Override
    public void teleopControls() {
        // driver
        INTAKE_DEPLOY_BUTTON.whenPressed(PISTONS::toggle);
        INTAKE_SPIN_BUTTON.whenPressedReleased(() -> {
            setIntake(Demand.IN);
            Subsystems.MAGAZINE.spinMagazineIntake();
        }, () -> {
            setIntake(Demand.OFF);
            Subsystems.MAGAZINE.stopMagazine();
        });

        // operator
        INTAKE_PISTONS.whenPressed(PISTONS::toggle);
        INTAKE_IN_BUTTON.whenPressedReleased(() -> setIntake(Demand.IN), () -> setIntake(Demand.OFF));
        INTAKE_OUT_BUTTON.whenPressedReleased(() -> setIntake(Demand.OUT), () -> setIntake(Demand.OFF));
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
    public void onDisabledStop(double timestamp) {

    }

    public void autoIntake(boolean on) {
        setDeployed(on);
        isAuto = on;
        demand = on ? Demand.IN : Demand.OFF;
        if (on) MAGAZINE.spinMagazineIntake();
        else MAGAZINE.stopMagazine();
    }

    @Override
    public void writePeriodicOutputs() {
        INTAKE_MOTOR.set(demand.percent);
    }

    public enum Demand {
        IN(0.7), OFF(0.0), OUT(-0.7);

        double percent;

        Demand(double percent) {
            this.percent = percent;
        }
    }

    public boolean getDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
        PISTONS.set(deployed ? PistonSolenoid.PistonState.EXTENDED : PistonSolenoid.PistonState.RETRACTED);
    }

    public void setIntake(Demand demand) {
        this.demand = demand;
    }
}
