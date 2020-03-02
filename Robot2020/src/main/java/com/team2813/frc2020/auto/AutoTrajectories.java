package com.team2813.frc2020.auto;

import com.team2813.lib.auto.*;

import java.util.List;

public enum AutoTrajectories {
    FIVE_BALL_ENEMY(List.of(
            new PauseTrajectory(1, 0), // shoot 3 balls
            new GeneratedTrajectory("2-ball", true, 1),
            new PauseTrajectory(1, 2), // intake 2 balls, turn intake off
            new GeneratedTrajectory("go back", false, 3),
            new GeneratedTrajectory("return", false, 4),
            new PauseTrajectory(1, 5) // shoot 2 balls
    )),
    TEST_ROUTINE(List.of(
            new PauseTrajectory(0.5, 0),
            new GeneratedTrajectory("test", false, 1),
            new RotateTrajectory(180, 2),
            new GeneratedTrajectory("another test path", false, 3)
    )),
    THREE_BALL(List.of(
            new PauseTrajectory(1, 0), //shoot 3-balls
            new GeneratedTrajectory("3-ball prepare", false, 1),
            new PauseTrajectory(1, 2), //turn intake on
            new GeneratedTrajectory("3-ball", true, 3), //intake 3 balls
            new GeneratedTrajectory("2-ball 2", true, 4), //intake 2 balls
            new PauseTrajectory(1, 5) //turn intake off
    )),
    SIX_BALL(List.of(
            new PauseTrajectory(3.5, 0), // shoot 3 ball turn intake on
            new GeneratedTrajectory("3-ball trench", true, 1), // intake 3 balls
            new PauseTrajectory(.1, 2), //turn intake off
            new GeneratedTrajectory("back 3-ball", false, 3),
            new PauseTrajectory(1, 4) // shoot 3 balls
    )),
    SIX_BALL_SIDE(List.of(
            new PauseTrajectory(3.5, 0), // shoot 3 ball turn intake on
            new GeneratedTrajectory("3-ball trench side", true, 1), // intake 3 balls
            new PauseTrajectory(.1, 2), //turn intake off
            new GeneratedTrajectory("back 3-ball side", false, 3),
            new PauseTrajectory(1, 4) // shoot 3 balls
    )),
    NINE_BALL(List.of(
            new PauseTrajectory(3.5, 0),
            new GeneratedTrajectory("3-ball trench", true, 1),
            new GeneratedTrajectory("back 3-ball", false, 2),
            new PauseTrajectory(3.5, 3),
            new GeneratedTrajectory("9-ball 3", true, 4),
            new GeneratedTrajectory("9-ball get rev 1", true, 5),
            new GeneratedTrajectory("9-ball get fwd 2", false, 6),
            new GeneratedTrajectory("9-ball get rev 3", true, 7),
            new GeneratedTrajectory("9-ball get fwd 4", false, 8),
            new GeneratedTrajectory("9-ball get rev 5", true, 9),
            new GeneratedTrajectory("return 9-ball 2", false, 10),
            new PauseTrajectory(1, 11)
    )),
    FIVE_BALL_TWO(List.of(
            new PauseTrajectory(1, 0), //turn intake on
            new GeneratedTrajectory("2-ball initiation line", true, 1), //intake 2 balls
            new RotateTrajectory(180, 2),
            new PauseTrajectory(1, 3), //turn intake off, shoot 5 balls
            new GeneratedTrajectory("general zone", false, 4)
    )),
    TEST_REVERSE(List.of(
            new GeneratedTrajectory("test reverse", true, 0)
    )),
    SERIES_TEST(List.of(
            new PauseTrajectory(150, 0)
    )), GO_FORWARD(List.of(
            new PauseTrajectory(6, 0),
            new GeneratedTrajectory("go forward", false, 1)
    ));;



    private RamseteTrajectory trajectory;

    AutoTrajectories(List<AutoTrajectory> trajectory) {
        this.trajectory = new RamseteTrajectory(trajectory);
    }

    public RamseteTrajectory getTrajectory() {
        return trajectory;
    }
}
