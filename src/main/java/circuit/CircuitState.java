package circuit;

import java.util.ArrayList;

/**
 * @author Lion Gienger
 * Status der Schaltung zu eibem Zeitpunkt-
 * Besteht aus dem Zeitpunkt {@code time} und einer Liste von Status {@code states}.
 *
 */
public class CircuitState {
	private int time;
	private ArrayList<String> states;

	public CircuitState(int time, ArrayList<String> states) {
		this.time = time;
		this.states = states;
	}

	public int getTime() {
		return time;
	}

	public ArrayList<String> getStates() {
		return states;
	}

}
