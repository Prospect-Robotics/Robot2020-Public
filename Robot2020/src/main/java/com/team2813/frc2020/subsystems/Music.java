package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;
import com.team2813.lib.motors.TalonFXWrapper;

import java.util.ArrayList;

public class Music extends Subsystem{
    Orchestra orchestra;

    private ArrayList<TalonFX> instruments;

    Music(){
        TalonFX INSTRUMENT1 = new TalonFX(1);
        TalonFX INSTRUMENT2 = new TalonFX(2);
        TalonFX INSTRUMENT3 = new TalonFX(3);
        TalonFX INSTRUMENT4 = new TalonFX(4);
        instruments = new ArrayList<>();
        instruments.add(INSTRUMENT1);
        instruments.add(INSTRUMENT2);
        instruments.add(INSTRUMENT3);
        instruments.add(INSTRUMENT4);
        orchestra = new Orchestra(instruments);
        orchestra.loadMusic("mega.chrp");
    }

    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {

    }

    @Override
    public void onEnabledStart(double timestamp) {
        orchestra.play();
    }

    @Override
    public void onEnabledLoop(double timestamp) {

    }

    @Override
    public void onEnabledStop(double timestamp) {

    }
}
