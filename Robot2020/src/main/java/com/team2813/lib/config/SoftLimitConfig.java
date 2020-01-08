package com.team2813.lib.config;

import com.team2813.lib.ctre.LimitDirection;

public class SoftLimitConfig {
	LimitDirection direction;
	boolean clearOnLimit;
	int threshold;
	boolean enable;
}
