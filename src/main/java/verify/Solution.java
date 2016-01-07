package verify;

import java.util.ArrayList;

import circuit.CircuitState;

public class Solution {
	public static ArrayList<CircuitState> solutions = new ArrayList<CircuitState>();

	public static void addSolution(CircuitState newState) {
		if (solutions.size() == 0) {
			solutions.add(newState);
		} else {
			int index = solutions.size() - 1;
			if (solutions.get(index).getTime() == newState.getTime()) {
				solutions.set(index, newState);
			}
			if (stateHasChanged(newState)) {
				solutions.add(newState);
			}
		}
	}

	private static boolean stateHasChanged(CircuitState newState) {
		ArrayList<String> oldStates = solutions.get(solutions.size() - 1)
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
		solutions.clear();
	}

}
