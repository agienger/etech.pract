package event;

import java.util.ArrayList;

import main.Circuit;
import file.CircuitState;
import file.Solution;

public class SignalList {
	public static Signal getSignalFromList(ArrayList<Signal> signals,
			String sigName) {
		for (Signal signal : signals) {
			if (signal.getName().equals(sigName)) {
				return signal;
			}
		}
		return null;
	}

	public static ArrayList<ArrayList<String>> solutionList = new ArrayList<ArrayList<String>>();

	public static void logState(ArrayList<Signal> signals, int time) {
		ArrayList<String> states = new ArrayList<String>();
		for (Signal sig : Signal.getSignalList()) {
			SignalKind kind = getSignalFromList(signals, sig.getName())
					.getSignalKind();
			if (kind.equals(SignalKind.INPUT) || kind.equals(SignalKind.OUTPUT)) {
				states.add(Integer.toString(sig.getValue() == true ? 1 : 0));
			}
		}
		Solution.addSolution(new CircuitState(time, states));
	}
	
	
}
