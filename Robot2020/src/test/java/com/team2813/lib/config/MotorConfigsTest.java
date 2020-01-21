package com.team2813.lib.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.team2813.lib.util.CrashTracker;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class MotorConfigsTest {

    @Test
    public void read() {
        try {
            File configFile = new File("src\\main\\deploy\\motorConfig.yaml");

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            MotorConfigs.RootConfigs motorConfigs = mapper.readValue(configFile, MotorConfigs.RootConfigs.class);
        } catch (IOException e) {
            System.out.println("Something went wrong while reading config files!");
            CrashTracker.logThrowableCrash(e);
            e.printStackTrace();
            System.out.println("ERROR WHEN READING CONFIG");
            e.printStackTrace();
        }
    }

    @Test
    public void testMotorConfigs() {
        System.out.println("ree");
    }
}