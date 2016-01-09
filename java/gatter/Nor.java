package gatter;

/**
 * Nor Gatter
 * 
 * @author Lion Gienger
 */
/**
 * @author Lion Gienger August 2015
 *
 */
public class Nor extends Gatter{


	/**
	 * Konstruktor der Klasse Nor, initialisiert ein Signal Array mit anzahl
	 * Feldern und Wartezeit wTime
	 * 
	 * @param anzahl
	 */
	public Nor(int anzahl, int wTime) {
		super(anzahl, wTime);
	}

	/**
	 * Diese Methode berechnet das Ausgangssignal des Nor Gatters In einer
	 * Schleife pruefen wir die Werte der Eingangssignale, falls eines davon
	 * true ist, so geben wir false zurueck. Ansonsten geben wir true zurueck.,
	 * 
	 * @return output value
	 */
	
	public boolean calculateOutputValue() {
		for (int i = 0; i < getInputSignalArray().length; i++) {
			if (getInputSignalArray()[i].getValue()) {
				return false;
			}
		}
		return true;
	}

	

}
