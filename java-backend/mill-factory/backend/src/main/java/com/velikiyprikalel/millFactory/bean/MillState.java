package com.velikiyprikalel.millFactory.bean;

import java.util.Queue;

import com.velikiyprikalel.millFactory.Mill;

public class MillState {
	private final int water;
	private final int millet;
	private final int flour;
	private final int power;

	private final boolean engineOn;
	private final boolean machineOn;

	public MillState(Mill mill, Queue<Water> water, Queue<Millet> mallets, Queue<Flour> floursQueue) {
		this.water = water.size();
		this.millet = mallets.size();
		this.flour = floursQueue.size();
		
		this.power = mill.getEnginePower();
		this.engineOn = mill.isEngineOn();
		this.machineOn = mill.isMachineOn();
	}

	public int getWater() {
		return water;
	}

	public int getMillet() {
		return millet;
	}

	public int getFlour() {
		return flour;
	}

	public int getPower() {
		return power;
	}

	public boolean getEngineOn() {
		return this.engineOn;
	}

	public boolean getMachineOn() {
		return this.machineOn;
	}
}
