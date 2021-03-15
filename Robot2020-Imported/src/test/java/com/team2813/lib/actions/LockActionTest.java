package com.team2813.lib.actions;

import org.junit.Test;

import static org.junit.Assert.*;

public class LockActionTest {

    int testNum = 0;

    @Test
    public void lock(){
        LockFunctionAction lock = new LockFunctionAction(this::setTestNum, this::trueFalse, true);
        lock.start(100);
        lock.execute(100);
        assert (lock.isFinished(100) == true);
        assert (testNum == 1);
    }

    public boolean trueFalse(){
        return true;
    }

    public void setTestNum(){
        testNum = 1;
    }
}