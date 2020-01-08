package com.team2813.frc2020;

/**
 * method to run autonomous in {@link Robot} autonomousPeriodic
 * @author Maharishi Rajarethenam
 */
public class Autonomous {
    enum RoutineNum {
        ROUTINE_1,
        ROUTINE_2,
        ROUTINE_3
    }
    public static void run(RoutineNum routineNum) {
        switch (routineNum) {
            case ROUTINE_1:
                break;
            case ROUTINE_2:
                break;
            case ROUTINE_3:
                break;
        }
    }
}
