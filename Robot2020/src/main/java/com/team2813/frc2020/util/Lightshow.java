package com.team2813.frc2020.util;

import com.ctre.phoenix.CANifier;
import com.team2813.frc2020.subsystems.Subsystems;
import edu.wpi.first.wpilibj.Notifier;

/*
ChannelA is Green
ChannelB is Red
ChannelC is Blue
 */
public class Lightshow {
    private CANifier canifier;
    private Light defaultLight;
    private Light light;
    private boolean flash = false;
    private boolean toggle = false;

    // handles flashing lights :)
    private Notifier loop = new Notifier(() -> {
        if (flash) {
            if (toggle) {
                setLight(light);
            } else {
                setLight(255, 255, 255);
            }
            toggle = !toggle;
        } else if (toggle) {
            setLight(light);
        }
    });

    public Lightshow(CANifier canifier) {
        this.canifier = canifier;
        setLight(Light.DEFAULT);
        loop.startPeriodic(.4);
    }

    public void setLight(int r, int g, int b) {
        canifier.setLEDOutput(r / 255.0, CANifier.LEDChannel.LEDChannelB);
        canifier.setLEDOutput(g / 255.0, CANifier.LEDChannel.LEDChannelA);
        canifier.setLEDOutput(b / 255.0, CANifier.LEDChannel.LEDChannelC);
    }

    public void setLight(int r, int g, int b, boolean flash) {
        setLight(r, g, b);
        this.flash = flash;
    }

    public void setLight(Light light, boolean flash) {
        if (light != this.light) {
            this.light = light;
            setLight(light.r, light.g, light.b, flash);
        }
    }

    public void setLight(Light light) {
        setLight(light, false);
    }

    public void setDefaultLight(Light light) {
        this.defaultLight = light;
        this.setLight(light);
    }

    public void resetLight(Light... lights) {
        for (Light light : lights)
            if (defaultLight != light && this.light == light)
                this.setLight(defaultLight);
    }

    public Light getLight() {
        return light;
    }

    public enum Light {
        DEFAULT(255, 255, 255),
        ENABLED(0, 255, 0),
        DISABLED(255, 0, 0),
        AUTONOMOUS(0, 0, 128),
        READY_TO_SHOOT(0, 0, 255),
        NOT_READY_TO_SHOOT(255, 0, 255),
        BEING_GAY(255, 105, 180),
        INTAKE_DONW(255, 165, 0),
        INTAKE_UP(128, 0, 128),
        CLIMBING(255, 215, 0),
        AUTO_AIM(255, 215, 0);

        int r;
        int g;
        int b;

        Light(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}
