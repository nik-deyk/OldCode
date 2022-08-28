package com.velikiyprikalel.millFactory;

import java.util.Queue;

import com.velikiyprikalel.millFactory.bean.Flour;
import com.velikiyprikalel.millFactory.bean.Millet;
import com.velikiyprikalel.millFactory.bean.Water;
import com.velikiyprikalel.millFactory.millEngine.Engine;
import com.velikiyprikalel.millFactory.millEngine.WaterWheel;
import com.velikiyprikalel.millFactory.millMachine.Machine;
import com.velikiyprikalel.millFactory.millMachine.Workshop;

public class Mill extends Thread {
    private final Machine machine;
	private final Engine engine;

	public Mill(Queue<Water> waterFlow, Queue<Millet> mallets, Queue<Flour> floursQueue) {
		this.engine = new WaterWheel(waterFlow);
		this.engine.start();

		this.machine = new Workshop(engine, mallets, floursQueue);
		this.machine.start();
	}

	public boolean isMachineOn() {
		return this.machine.isOn();
	}

	public boolean isEngineOn() {
		return this.engine.getPower() > 0;
	}

	public int getEnginePower() {
		return this.engine.getPower();
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			if (engine.getPower() > 0) {
				machine.turnOn();
			}
		}
	}
}
