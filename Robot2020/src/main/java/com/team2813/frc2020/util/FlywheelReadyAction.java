package com.team2813.frc2020.util;

import com.team2813.lib.actions.LockAction;

import java.util.concurrent.Callable;

import static com.team2813.frc2020.subsystems.Subsystems.SHOOTER;

public class FlywheelReadyAction extends LockAction {
    /**
     * Creates a new action that waits until a condition is met
     *
     * @param function
     * @param removeOnDisabled
     */
    private FlywheelReadyAction(Callable<Boolean> function, boolean removeOnDisabled) {
        super(function, removeOnDisabled);
    }

    public FlywheelReadyAction() {
        super(SHOOTER::isFlywheelReady, true);
    }
}
