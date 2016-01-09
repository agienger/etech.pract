package verify;

import java.util.ArrayList;

import circuit.CircuitState;

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

	public static void clear() {
		results.clear();
	}

}
