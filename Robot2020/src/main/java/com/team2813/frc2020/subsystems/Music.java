package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.

public class Music extends Subsystem{

    Orchestra oc;

    private TalonFX INSTRUMENT1;
    private TalonFX INSTRUMENT2;
    private TalonFX INSTRUMENT3;
    private TalonFX INSTRUMENT4;
    private TalonFX INSTRUMENT5;
    private TalonFX INSTRUMENT6;

    Music(){
        INSTRUMENT1 = new TalonFX(1);
        INSTRUMENT2 = new TalonFX(2);
        INSTRUMENT3 = new TalonFX(3);
        INSTRUMENT4 = new TalonFX(4);
        INSTRUMENT5 = new TalonFX(5);
        INSTRUMENT6 = new TalonFX(6);
    }

    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {

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
