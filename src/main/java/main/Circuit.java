package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import event.Signal;
import event.SignalKind;
import event.SignalList;
import file.DateiLeser;
import gatter.And;
import gatter.Exor;
import gatter.Gatter;
import gatter.Nand;
import gatter.Or;

public class Circuit {

	DateiLeser fileReader;
	private HashMap<String, Signal> inputs = new HashMap<String, Signal>();
	private HashMap<String, Signal> outputs=new HashMap<String, Signal>();
	private HashMap<String, Signal> signals=new HashMap<String, Signal>();
	private HashMap<String, Gatter> gates = new HashMap<String, Gatter>();
	private static ArrayList<Signal> signalList = new ArrayList<Signal>();

	public Circuit(File file) {
		fileReader = new DateiLeser(file.getPath());
		
		
		while (fileReader.nochMehrZeilen()) {
			String line = fileReader.gibZeile();
			if (line.startsWith("#") || line.replaceAll("\\s","").equals("")) {
				continue;
			}
			if (line.startsWith("Input")) {
				for (String sigName: getInformationFromLine(line, "Input")) {
					addtoSignalList(sigName, SignalKind.INPUT);
				inputs.put(sigName, new Signal(sigName));
				signals.put(sigName, new Signal(sigName));
				}
			} else if (line.startsWith("Output")) {
				for (String sigName: getInformationFromLine(line, "Output")) {
					addtoSignalList(sigName, SignalKind.OUTPUT);
					outputs.put(sigName, new Signal(sigName));
					signals.put(sigName, new Signal(sigName));
					}
			} else if (line.startsWith("Signal")) {
				for (String sigName: getInformationFromLine(line, "Signal")) {
					addtoSignalList(sigName, SignalKind.INNER);
					signals.put(sigName, new Signal(sigName));
					}
			}
			else if (line.startsWith("Gate")) {
				String [] gateData = line.split("\\s+");
				String gateName = gateData[1];
				String kindOf = gateData[2].replaceAll("([a-zA-Z]+).*", "$1");
				int numberOfInputs = Integer.parseInt(gateData[2].replaceAll(".*([0-9]+)", "$1"));
				int delay = Integer.parseInt(gateData[4].replaceAll("([0-9]+);", "$1"));
				if (kindOf.equals("AND")) {
					And gatter = new And(numberOfInputs, delay); 
					gates.put(gateName, gatter);
				}
				else if (kindOf.equals("NAND")) {
					Nand gatter = new Nand(numberOfInputs, delay); 
					gates.put(gateName, gatter);
				}
				else if (kindOf.equals("EXOR")) {
					Exor gatter = new Exor(numberOfInputs, delay); 
					gates.put(gateName, gatter);
				}
				else if (kindOf.equals("OR")) {
					Or gatter = new Or(numberOfInputs, delay); 
					gates.put(gateName, gatter);
				}
			}
			else {
				gateDefinition(line.replaceAll("\\s+",""));
			}
		}
	}

	private void addtoSignalList(String sigName, SignalKind kind) {
		Signal s = new Signal(sigName);
		s.setSignalKind(kind);
		signalList.add(s);
	}

	private void gateDefinition(String line) {
		String regex = "(\\w+)\\.(\\w+)=(\\w+);";
		String gateName = line.replaceAll(regex, "$1");
		String property = line.replaceAll(regex, "$2");
		String sigName = line.replaceAll(regex, "$3");
		
		Gatter gatter = gates.get(gateName);
//		Signal signal = signals.get(sigName);
		Signal signal = SignalList.getSignalFromList(signalList, sigName);
		if (property.equals("o")) {
			gatter.setOutput(signal);;
		}
		else if (property.startsWith("i")) {
			int channel = Integer.parseInt(property.replaceAll("i([0-9]+)", "$1"));
			gatter.setInput(channel-1, signal);
		}
	}



	private List<String> getInformationFromLine(String line, String keyword) {
		return Arrays.asList(line
				.replaceAll(keyword+" (.*);", "$1").replaceAll("\\s","").split(","));
	}

	public HashMap<String, Signal> getInputs() {
		return inputs;
	}

	
	public HashMap<String, Signal> getOutputs() {
		return outputs;
	}

	public HashMap<String, Gatter> getGates() {
		return gates;
	}

	public HashMap<String, Signal> getSignals() {
		return signals;
	}

	public static ArrayList<Signal> getSignalList() {
		return signalList;
	}

}