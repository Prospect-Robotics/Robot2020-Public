package com.team2813.lib.config;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.team2813.lib.ctre.LimitDirection;

class LimitSwitchConfig {

	LimitDirection direction;
	boolean clearOnLimit;
	Polarity polarity;


	enum Polarity {
		OPEN(LimitSwitchNormal.NormallyOpen, LimitSwitchPolarity.kNormallyOpen),
		CLOSED(LimitSwitchNormal.NormallyClosed, LimitSwitchPolarity.kNormallyClosed);

		LimitSwitchNormal ctre;
		LimitSwitchPolarity sparkMax;

		Polarity(LimitSwitchNormal ctre, LimitSwitchPolarity sparkMax) {
			this.ctre = ctre;
			this.sparkMax = sparkMax;
		}
	}

}
