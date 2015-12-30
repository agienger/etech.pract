package gatter;

public class And extends Gatter {

	public And(int anzahl, int wTime) {
		super(anzahl, wTime);
	}

	/**
	 * Diese Methode berechnet das Ausgangssignal des Nand Gatters In einer
	 * Schleife pruefen wir die Werte der Eingangssignale, falls eines davon
	 * false ist, so geben wir false zurueck. Ansonsten geben wir true zurueck.
	 * 
	 * @return output value
	 */
	public boolean calculateOutputValue() {
		for (int i = 0; i < getInputSignalArray().length; i++) {
			if (!getInputSignalArray()[i].getValue()) {
				return false;
			}
		}
		return true;
	}

}
