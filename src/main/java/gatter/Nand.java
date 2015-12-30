package gatter;

/**
 * Nand Gatter
 * 
 * @author Lion Gienger
 */
/**
 * @author Lion Gienger August 2015
 *
 */
public class Nand extends Gatter{


	/**
	 * Konstruktor der Klasse Nand, initialisiert ein Signal Array mit anzahl
	 * Feldern und Wartezeit wTime
	 * 
	 * @param anzahl
	 */
	public Nand(int anzahl, int wTime) {
		super(anzahl, wTime);
	}

	/**
	 * Diese Methode berechnet das Ausgangssignal des Nand Gatters In einer
	 * Schleife pruefen wir die Werte der Eingangssignale, falls eines davon
	 * false ist, so geben wir true zurueck. Ansonsten geben wir false zurueck.,
	 * 
	 * @return output value
	 */
	
	public boolean calculateOutputValue() {
		for (int i = 0; i < getInputSignalArray().length; i++) {
			if (!getInputSignalArray()[i].getValue()) {
				return true;
			}
		}
		return false;
	}

	

}
