package com.team2813.lib.actions;

import org.junit.Test;

import static org.junit.Assert.*;

public class LockActionTest {
    @Test
    public void lock(){
        LockAction lock = new LockAction(() -> trueFalse(), true);
        lock.execute(100);
        assert (lock.isFinished(100) == true);
    }

    public boolean trueFalse(){
        return true;
    }
}