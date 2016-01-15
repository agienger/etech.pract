package circuit;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import simulator.DateiLeser;
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

/**
 * @author Lion Gienger
 * Diese Klasse beinhaltet einen Schaltkreis (circuit)
 *
 */
public class Circuit {
	
	/**
	 * Liste aller Signale dieses Circuits
	 */
	private ArrayList<Signal> signalList;
	/**
	 * Dieser fileReader liest das Circuit file ein
	 */
	private DateiLeser fileReader;
	/**
	 * Diese Map hält alle Gatter dieses Circuits. Der key dieser Map ist der Name des Gatters, 
	 * der Wert ist die erzeugte Instanz des Gatters
	 * 
	 */
	private HashMap<String, Gatter> gates = new HashMap<String, Gatter>();

	/**
	 * Folgende Schritte werden im Konstruktor des Circuits durchgeführt:
	 * <br> 1. Initialisieren der SignalListe
	 * <br> 2. Erzeugen des fileReaders als Instanz von {@link simulator.DateiLeser}
	 * <br> 3. In einer Schleife werden nun alle Zeilen der Eingabedatei ausgelesen
	 * <br> 4. Alle Zeilen die mit einem {@code #} beginnen oder die nur whitespaces beinhalten werden ignoriert.
	 * <br> 5. Zeilen die mit dem keyword {@code input} beginnen, werden als input Signale in die Signalliste geschrieben.
	 * <br> 7. Zeilen die mit dem keyword {@code output} beginnen, werden als output Signale in die Signalliste geschrieben.
	 * <br> 8. Zeilen die mit dem keyword {@code signal} beginnen, werden als innere Signale in die Signalliste geschrieben.
	 * <br> 9. Zeilen die mit dem keyword {@code gater} beginnen, definieren ein Gatter. Die Zeile wird gesplittet an den 
	 * whitespaces und das Ergebnis in ein Array geschrieben.
	 * <br> 9.1. Das zweite Element dieses Arrays ({@code gateData[1]}) definiert den Namen des Gatters.
	 * <br> 9.2. Das dritte Element dieses Arrays ({@code gateData[2]}) besteht aus einem Wort und einer Zahl.
	 * Das Wort beschreibt die Art des Gatters (z.B {@code AND} oder {@code FF}), die Zahl beschreibt 
	 * die Anzahl der Eingänge. Falls die Zahl fehlt, z.B. bei {@code BUF}, ist die Anzahl der Eingänge bereits
	 * die Art der Gatters definert. 
	 * <br> 9.3. Das fünfte Element dieses Arrays ({@code gateData[1]}) definiert die delay Zeit dieses Gatters
	 * <br> 9.4. Schließlich wird ein Instanz des jeweiligen Gatters erzeugt, abhängig von der Art des Gatters.
	 * <br> 10. Alle anderen Zeilen defineren In- und Outputs eines Gatters, dies wird 
	 * in der Methode {@link #gateDefinition(String)} verarbeitet.
	 * 
	 * @param file Die Datei, in welcher die Schaltkreis-Defintion gepflegt ist (.cir Datei)
	 */
	public Circuit(File file) {
		
		signalList = new ArrayList<Signal>();
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

	/**
	 * Die Gatter Definition folgt dem Pattern [gatter].[in/out9=[signal]. Dies bilden wir in
	 * dem regulären Ausdruck {@code regex} ab, wo wir entsprechend 3 Gruppen definieren.
	 * <br> Die erste Gruppe beinhaltet den Gatternamen
	 * <br> Bei der zweiten Gruppe muss man mehrere Fallunterscheidungen machen: Beginnt sie mit {@code i} 
	 * gefolgt von einer Zahl n, so bezeichnet dies das n-te Eingangssignal des Gatters.
	 * <br> Besteht sie aus einem {@code o} oder {@code q}, so bezeichnet dies das Ausgangssignal des Gatters.
	 * {@code q} wird für ein {@code LATCH} bzw.  Gatter verwendet.
	 * <br> Besteht sie aus einem {@code nq}, so bezeichnet dies das negierte Ausgangssignal des {@code FF} Gatters.
	 * <br> Besteht sie aus einem {@code e} oder einem {@code c}, so bezeichnet dies das "enable" bzw. 
	 * "clock" Signal eines {@code LATCH} bzw. {@code FF} Gatters.
	 * <br> Besteht sie aus einem {@code d}, so bezeichnet dies das Datensignal eines
	 * {@code LATCH} bzw. {@code FF} Gatters.
	 * <br> In allen anderen Fällen wird eine Exception geworfen, das eine solche property nicht bekannt ist.
	 * <br>
	 * <br> Die entsprechenden Signal werden dann über die Methoden {@code setOutput()}, {@code setNegOutput} 
	 * (bei {@code FF}) bzw. {@code setInput} der Gatter gesetzt.
	 * <br>
	 * @param line Zeile der Eingabedatei (ohne Whitespaces), 
	 * in der die In- und Output Signale eines Gatters definiert sind.
	 */
	private void gateDefinition(String line) {
		String regex = "(\\w+)\\.(\\w+)=(\\w+);";
		String gateName = line.replaceAll(regex, "$1");
		String property = line.replaceAll(regex, "$2");
		String sigName = line.replaceAll(regex, "$3");

		Gatter gatter = gates.get(gateName);
		Signal signal = Signal.getSignalFromList(signalList, sigName);
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

	/**
	 * Hilfsmethode, welche für ein Signalzeile angewendet wird. Es werden in der Zeile zunächst 
	 * sas keyword und die whitespaces gelöscht und dann an dem Kommas gesplittet und in eine 
	 * Liste von Signalnamen geschrieben.
	 * @param line Zeile der Eingabedatei
	 * @param keyword Gibt an ob, es sich um InPut, Output oder inneres Signal handelt.
	 * @return Liste der Signalnamen
	 */
	private List<String> getInformationFromLine(String line, String keyword) {
		return Arrays.asList(line.replaceAll(keyword + " (.*);", "$1")
				.replaceAll("\\s", "").split(","));
	}

	/**
	 * @return Signal Liste der Circuits
	 */
	public  ArrayList<Signal> getSignalList() {
		return signalList;
	}

}
