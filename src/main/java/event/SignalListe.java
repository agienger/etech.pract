package event;

import java.util.ArrayList;

public class SignalListe {
	
	public static Signal getSignalFromList(ArrayList<Signal> signalList, String sigName) {
		for (Signal signal : signalList) {
			if (signal.getName().equals(sigName)) {
				return signal;
			}
		}
		return null;
	}

}
