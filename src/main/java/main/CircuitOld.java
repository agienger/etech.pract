package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import event.Event;
import event.Signal;
import file.DateiLeser;
import gatter.And;
import gatter.Exor;
import gatter.Gatter;
import gatter.Nand;
import gatter.Or;

public class CircuitOld {

	DateiLeser fileReader;
	public static HashMap<String, Signal> signals = new HashMap<String, Signal>();
	private HashMap<String, GateData> gates = new HashMap<String, CircuitOld.GateData>();

	public CircuitOld(File file) {
		fileReader = new DateiLeser(file.getPath());
		ArrayList<String> inAndOutPutsRow = new ArrayList<String>();
		while (fileReader.nochMehrZeilen()) {
			String line = fileReader.gibZeile();
			if (line.startsWith("#") || line.replaceAll("\\s", "").equals("")) {
				continue;
			}
			if (line.startsWith("Input")) {
				for (String sigName : getInformationFromLine(line, "Input")) {
					inAndOutPutsRow.add(sigName);
					signals.put(sigName, new Signal(sigName));
				}
			} else if (line.startsWith("Output")) {
				for (String sigName : getInformationFromLine(line, "Output")) {
					inAndOutPutsRow.add(sigName);
					signals.put(sigName, new Signal(sigName));
				}
			} else if (line.startsWith("Signal")) {
				for (String sigName : getInformationFromLine(line, "Signal")) {
					signals.put(sigName, new Signal(sigName));
				}
			} else if (line.startsWith("Gate")) {
				String[] gateDataArray = line.split("\\s+");

				String gateName = gateDataArray[1];
				String kindOf = gateDataArray[2].replaceAll("([a-zA-Z]+).*",
						"$1");
				int numberOfInputs = Integer.parseInt(gateDataArray[2]
						.replaceAll(".*([0-9]+)", "$1"));
				int delay = Integer.parseInt(gateDataArray[4].replaceAll(
						"([0-9]+);", "$1"));
				GateData gateData = new GateData(gateName, kindOf,
						numberOfInputs, delay);
				gates.put(gateName, gateData);
			} else {
				gateDefinition(line);
			}
			Event.inAndOutPuts.add(inAndOutPutsRow);
		}
		for (GateData gateData : gates.values()) {
			Gatter gatter = null;
			if (gateData.kindOf.equals("AND")) {
				gatter = new And(gateData.numberOfInputs, gateData.delay);
			} else if (gateData.kindOf.equals("NAND")) {
				gatter = new Nand(gateData.numberOfInputs, gateData.delay);
			} else if (gateData.kindOf.equals("EXOR")) {
				gatter = new Exor(gateData.numberOfInputs, gateData.delay);
			} else if (gateData.kindOf.equals("OR")) {
				gatter = new Or(gateData.numberOfInputs, gateData.delay);
			}
			gatter.setOutput(signals.get(gateData.outPutSigName));
			for (int i = 0; i < gateData.inputs.size(); i++) {
				gatter.setInput(i, signals.get(gateData.inputs.get(i)));
			}
		}
	}

	private class GateData {
		public GateData(String gateName, String kindOf, int numberOfInputs,
				int delay) {
			this.gateName = gateName;
			this.kindOf = kindOf;
			this.numberOfInputs = numberOfInputs;
			this.delay = delay;
		}

		private String gateName;
		private String outPutSigName;
		private String kindOf;
		private int delay;
		private int numberOfInputs;
		private List<String> inputs = new ArrayList<String>();
	}

	private void gateDefinition(String line) {
		String regex = "(\\w+)\\.(\\w+)\\s+=\\s+(\\w+);";
		String gateName = line.replaceAll(regex, "$1");
		String property = line.replaceAll(regex, "$2");
		String sigName = line.replaceAll(regex, "$3");

		GateData gateData = gates.get(gateName);

		if (property.equals("o")) {
			gateData.outPutSigName = sigName;
		} else if (property.startsWith("i")) {
			int channel = Integer.parseInt(property.replaceAll("i([0-9]+)",
					"$1"));
			gateData.inputs.add(channel - 1, sigName);
		}
	}

	private List<String> getInformationFromLine(String line, String keyword) {
		return Arrays.asList(line.replaceAll(keyword + " (.*);", "$1")
				.replaceAll("\\s", "").split(","));
	}

	public HashMap<String, Signal> getSignals() {
		return signals;
	}

}
