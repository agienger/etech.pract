package file;

import java.util.ArrayList;

public class CircuitState {
	private int time;
	private ArrayList<String> states;

	public CircuitState (int time, ArrayList<String> states ) {
		this.time=time;
		this.states =  states;
	}

	public int getTime() {
		return time;
	}

	public ArrayList<String> getStates() {
		return states;
	}

	
}
