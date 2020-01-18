package com.team2813.frc2020;

import com.team2813.frc2020.actions.SeriesAction;
import com.team2813.frc2020.subsystems.Subsystems;

/**
 * method to run autonomous in {@link Robot} autonomousPeriodic
 * @author Maharishi Rajarethenam
 */
public class Autonomous {

	private SeriesAction autoAction;

	enum RoutineNum {
		ROUTINE_1,
		ROUTINE_2,
		ROUTINE_3,
		ROUTINE_4
	}

	public void run(RoutineNum routineNum){
		switch(routineNum){ // TODO: finish it
			case ROUTINE_1://6-ball
				autoAction = new SeriesAction(
						  //back up
						  //turn toward target
						  //shoot 3 balls
						  //back up with intake on / intake 3 balls
						  //forward
						  //shoot 3 balls
				);


				break;
			case ROUTINE_2://8-ball
				autoAction = new SeriesAction(
						  //shoot 3 balls
						  //back up to the right
						  //drive to side of shield generator, intake 2 balls
						  //drive around shield generator, intake 3 balls
						  //drive back to initiation line
						  //shoot 5 balls
				);
				break;
			case ROUTINE_3://Steal / 5-ball
				autoAction = new SeriesAction(
						  
				);

				break;
			case ROUTINE_4://3-ball
				autoAction = new SeriesAction(
						//shoot 3 balls

				);
				break;
			default:
				autoAction = new SeriesAction();
		}

		Subsystems.LOOPER.addAction(autoAction);
	}
}
