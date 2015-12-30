package event;

import java.util.ArrayList;
import java.util.HashMap;

import main.Circuit;

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

	public static HashMap<Integer,ArrayList<String>> solutionMap = new HashMap<Integer,ArrayList<String>>();
	
	public static void logState(ArrayList<Signal> signals, 	int time) {
		ArrayList<String> states = new ArrayList<String>();
		for (Signal sig : Circuit.getSignalList()) {
			SignalKind kind = getSignalFromList(signals, sig.getName())
					.getSignalKind();
			if (kind.equals(SignalKind.INPUT)
					|| kind.equals(SignalKind.OUTPUT)) {
				states.add(Integer.toString(sig.getValue() == true ? 1 : 0));
			}
		}
		solutionMap.put(time, states);
		}
}
