package verify;

import java.util.ArrayList;

import circuit.CircuitState;
import circuit.Signal;
import circuit.SignalKind;

public class Result {
	public static ArrayList<CircuitState> results = new ArrayList<CircuitState>();

	public static void addState(CircuitState newState) {
		if (results.size() == 0) {
			results.add(newState);
		} else {
			int index = results.size() - 1;
			if (results.get(index).getTime() == newState.getTime()) {
				results.set(index, newState);
			}
			if (stateHasChanged(newState)) {
				results.add(newState);
			}
		}
	}

	private static boolean stateHasChanged(CircuitState newState) {
		ArrayList<String> oldStates = results.get(results.size() - 1)
				.getStates();
		int index = 0;
		for (String state : newState.getStates()) {
			if (!oldStates.get(index).equals(state)) {
				return true;
			}
			index++;
		}
		return false;
	}

	/**
	 * Speichert den Status der Schaltung zum Zeitpunkt {@code time} in die statische Liste 
	 * {@link verify.Result#results}, siehe {@link verify.Result}
	 * @param time Zeitpunkt der Schaltung
	 */
	public static void storeCurrentState(int time, ArrayList<Signal> signalList) {
		ArrayList<String> states = new ArrayList<String>();
		for (Signal sig : signalList) {
			SignalKind kind = Signal.getSignalFromList(signalList,
					sig.getName()).getSignalKind();
			if (kind.equals(SignalKind.INPUT) || kind.equals(SignalKind.OUTPUT)) {
				states.add(Integer.toString(sig.getValue() == true ? 1 : 0));
			}
		}
		addState(new CircuitState(time, states));
	}
	
	public static void clear() {
		results.clear();
	}

}
