package circuit;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import file.DateiLeser;
import gatter.And;
import gatter.Buf;
import gatter.Exor;
import gatter.FF;
import gatter.Gatter;
import gatter.Latch;
import gatter.Nand;
import gatter.Nor;
import gatter.Not;
import gatter.Or;

public class Circuit {

	
	private static ArrayList<Signal> signalList;
	DateiLeser fileReader;
	private HashMap<String, Gatter> gates = new HashMap<String, Gatter>();

	public Circuit(File file) {
		
		fileReader = new DateiLeser(file.getPath());
		while (fileReader.nochMehrZeilen()) {
			String line = fileReader.gibZeile();
			if (line.startsWith("#") || line.replaceAll("\\s", "").equals("")) {
				continue;
			}
			if (line.toLowerCase().startsWith("input")) {
				for (String sigName : getInformationFromLine(line, "Input")) {
					signalList.add(new Signal(sigName,SignalKind.INPUT));
				}
			} else if (line.toLowerCase().startsWith("output")) {
				for (String sigName : getInformationFromLine(line, "Output")) {
					signalList.add(new Signal(sigName,SignalKind.OUTPUT));
				}
			} else if (line.toLowerCase().startsWith("signal")) {
				for (String sigName : getInformationFromLine(line, "Signal")) {
					signalList.add(new Signal(sigName,SignalKind.INNER));
				}
			} else if (line.toLowerCase().startsWith("gate")) {
				int numberOfInputs = 0;
				String[] gateData = line.split("\\s+");
				String gateName = gateData[1];
				String kindOf = gateData[2].replaceAll("([a-zA-Z]+).*", "$1");
				if (Character
						.isDigit(gateData[2].charAt(gateData[2].length() - 1))) {
					numberOfInputs = Integer.parseInt(gateData[2].replaceAll(
							".*([0-9]+)", "$1"));
				}
				int delay = Integer.parseInt(gateData[4].replaceAll(
						"([0-9]+);", "$1"));
				if (kindOf.equals("AND")) {
					And gatter = new And(numberOfInputs, delay);
					gates.put(gateName, gatter);
				} else if (kindOf.equals("NAND")) {
					Nand gatter = new Nand(numberOfInputs, delay);
					gates.put(gateName, gatter);
				} else if (kindOf.equals("EXOR")) {
					Exor gatter = new Exor(numberOfInputs, delay);
					gates.put(gateName, gatter);
				} else if (kindOf.equals("OR")) {
					Or gatter = new Or(numberOfInputs, delay);
					gates.put(gateName, gatter);
				} else if (kindOf.equals("NOR")) {
					Nor gatter = new Nor(numberOfInputs, delay);
					gates.put(gateName, gatter);
				} else if (kindOf.equals("NOT")) {
					Not gatter = new Not(delay);
					gates.put(gateName, gatter);
				} else if (kindOf.equals("LATCH")) {
					Latch gatter = new Latch(delay);
					gates.put(gateName, gatter);
				} else if (kindOf.equals("FF")) {
					FF gatter = new FF(delay);
					gates.put(gateName, gatter);
				} else if (kindOf.equals("BUF")) {
					Buf gatter = new Buf(delay);
					gates.put(gateName, gatter);
				}
			} else {
				gateDefinition(line.replaceAll("\\s+", ""));
			}
		}
	}

	private void gateDefinition(String line) {
		String regex = "(\\w+)\\.(\\w+)=(\\w+);";
		String gateName = line.replaceAll(regex, "$1");
		String property = line.replaceAll(regex, "$2");
		String sigName = line.replaceAll(regex, "$3");

		Gatter gatter = gates.get(gateName);
		Signal signal = getSignalFromList(sigName);
		String propertyTlc = property.toLowerCase();
		if (propertyTlc.equals("o") || propertyTlc.equals("q")) {
			gatter.setOutput(signal);
		} else if (propertyTlc.startsWith("i")) {
			int channel = Integer.parseInt(property.replaceAll("i([0-9]+)",
					"$1"));
			gatter.setInput(channel - 1, signal);
		} else if (propertyTlc.equals("e") || property.equals("c")) {
			gatter.setInput(0, signal);
		} else if (propertyTlc.equals("d")) {
			gatter.setInput(1, signal);
		} else if (propertyTlc.equals("nq")) {
			FF ff = (FF) gatter;
			ff.setNegOutput(signal);
		} else {
			InvalidParameterException e = new InvalidParameterException();
			System.out.println("The line " + line + " has an unknown property");
			throw e;
		}
	}

	private List<String> getInformationFromLine(String line, String keyword) {
		return Arrays.asList(line.replaceAll(keyword + " (.*);", "$1")
				.replaceAll("\\s", "").split(","));
	}

	public HashMap<String, Gatter> getGates() {
		return gates;
	}

	public static ArrayList<Signal> getSignalList() {
		return signalList;
	}
	
	Signal getSignalFromList(String sigName) {
		for (Signal signal : signalList) {
			if (signal.getName().equals(sigName)) {
				return signal;
			}
		}
		return null;
	}

	public static void setSignalList(ArrayList<Signal> sigList) {
		signalList = sigList;
		
	}

}
