package com.team2813.frc2020.util;

public enum AutonomousPath {
    TEST1("Test 1"),
    TEST2("Test 2"),
    TEST3("Test 3");

    String name;

    AutonomousPath(String name) {
        ShuffleboardData.pathChooser.addOption(name, this);
    }
}
