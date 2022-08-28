package com.velikiyprikalel.millFactory.millMachine;

public class Machine extends Thread {
    private boolean workingState = false;

    public boolean isOn() {
        return workingState;
    }

    public void turnOn() {
        workingState = true;
    }

    public void turnOff() {
		workingState = false;
	}
}
