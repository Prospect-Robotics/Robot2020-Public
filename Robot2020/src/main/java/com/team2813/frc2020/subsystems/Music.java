package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;
import com.team2813.lib.motors.TalonFXWrapper;

import javax.sound.midi.Instrument;
import java.util.ArrayList;
import java.util.Arrays;

public class Music extends Subsystem{
    ArrayList<Orchestra> orchestra;

    private ArrayList<TalonFX> instruments;

    Music(){
        orchestra = new ArrayList<>();
        TalonFX INSTRUMENT1 = new TalonFX(1);
        TalonFX INSTRUMENT2 = new TalonFX(2);
        TalonFX INSTRUMENT3 = new TalonFX(3);
        TalonFX INSTRUMENT4 = new TalonFX(4);
        TalonFX INSTRUMENT5 = new TalonFX(5);
        TalonFX INSTRUMENT6 = new TalonFX(6);
        createOrchestra(INSTRUMENT1);
        createOrchestra(INSTRUMENT2);
        createOrchestra(INSTRUMENT3);
        createOrchestra(INSTRUMENT4);
        createOrchestra(INSTRUMENT5);
        createOrchestra(INSTRUMENT6);
        for(Orchestra o : orchestra) o.loadMusic("c.chrp");
    }

    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {

    }

    @Override
    public void onEnabledStart(double timestamp) {

        for(Orchestra o : orchestra) o.play();
    }

    @Override
    public void onEnabledLoop(double timestamp) {

    }

    @Override
    public void onEnabledStop(double timestamp) {

    }

    void createOrchestra(TalonFX... talons){
        orchestra.add(new Orchestra(Arrays.asList(talons)));
    }
}
