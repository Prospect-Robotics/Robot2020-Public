package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;

import java.util.ArrayList;

public class Music extends Subsystem{

    Orchestra orchestra;

    private ArrayList<TalonFX> instruments;
    private TalonFX INSTRUMENT1 = new TalonFX(1);
    private TalonFX INSTRUMENT2 = new TalonFX(2);
    private TalonFX INSTRUMENT3 = new TalonFX(3);
    private TalonFX INSTRUMENT4 = new TalonFX(4);
    private TalonFX INSTRUMENT5 = new TalonFX(5);
    private TalonFX INSTRUMENT6 = new TalonFX(6);

    Music(){
        instruments.add(INSTRUMENT1);
        instruments.add(INSTRUMENT2);
        instruments.add(INSTRUMENT3);
        instruments.add(INSTRUMENT4);
        instruments.add(INSTRUMENT5);
        instruments.add(INSTRUMENT6);
        orchestra = new Orchestra(instruments);
    }

    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {

    }

    @Override
    public void onEnabledStart(double timestamp) {
        orchestra.loadMusic("megalovania.chrp");
        orchestra.play();
    }

    @Override
    public void onEnabledLoop(double timestamp) {

    }

    @Override
    public void onEnabledStop(double timestamp) {

    }
}
