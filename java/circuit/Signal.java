package circuit;

import gatter.Gatter;

import java.util.ArrayList;

import verify.Result;

/**
 * @author Lion Gienger
 *
 */
public class Signal {
	
	/**
	 * Maximale Änderungen eines Signals während der Einschwingphase
	 */
	private static final int COUNTER_PRE_MAX = 10;
	/**
	 * Name des Signals
	 */
	private String signalName;
	/**
	 * Wert des Signals
	 */
	private boolean signalValue;
	/**
	 * Liste der Gatter, in welches dieses Signal mündet.
	 */
	private ArrayList<Gatter> gatterList;
	/**
	 * Zähler der angibt, wie oft das Signal in der Einschwungphase geändert wurde
	 */
	private int setCounterPre = 0;
	/**
	 * Art des Signals (Input, Output, Inner)
	 */
	private SignalKind signalKind;
	/**
	 * Statische Liste aller Signale
	 */
	private static ArrayList<Signal> signalList;
	
	
	/**
	 * Der Names des Signals wird gesetzt und 
	 * die GatterListe des Signals wird initialisiert.
	 * @param name Name des Signals
	 */
	public Signal(String name) {
		setName(name);
		gatterList = new ArrayList<Gatter>();
	}
	
	/**
	 * Der Names und die Art des Signals werden gesetzt und 
	 * die GatterListe des Signals wird initialisiert.
	 * @param name Name des Signals
	 * @param kind Art des Signals
	 */
	public Signal(String name , SignalKind kind){
		this(name);
		setSignalKind(kind);
	}

	/**
	 * Gibt den Signalwert zurück.
	 * @return der Wert des Signals
	 */
	public boolean getValue() {
		return signalValue;
	}

	/**
	 * Gibt den Signalnamen zurück.
	 * @return der Name des Signals
	 */
	public String getName() {
		return signalName;
	}

	/**
	 * Setzt den Namen des Signals
	 * @param signalName Name des Signals
	 * 
	 */
	public void setName(String signalName) {
		this.signalName = signalName;
	}

	/**
	 * Wird beim Schaltungsaufbau aus dem Gatter gerufen. 
	 * Fügt das Gatter der Gatterliste des Signals hinzu.
	 * 
	 * @param gatter Das Gatter das zur Gatterliste zugefügt wird.
	 */
	public void connectTo(Gatter gatter) {
		gatterList.add(gatter);
	}

	/**
	 * Setzt den aktuellen Wert des Signals. Wir unterscheiden 2 Fälle:
	 * <br> 1. In der Einschwingphase ({@code Event.getEventQueue().getRunTime() == 0}) wird sofort für alle
	 * angeschlossenen Gatter der Output des Gatters neu berechnet. Wegenn möglicher Rückkopplungen 
	 * merken wir uns, wie oft das Signal bereits geändert wurde. Wenn die Anzahl der Änderungen 
	 * {@code COUNTER_PRE_MAX} übersteigt werden die Gatter nicht mehr geändert.
	 * <br> 2. Wenn die EventQueue bereits gestartet ist, werden die Signale nicht sofort weitergeschaltet, 
	 * sondern die Gatter werden über die {@see circuit.Event.#propagate()} 
	 * Methode für jedes neue Event neu berechnet.
	 * 
	 * @param value Wert des Signals
	 */
	public void setValue(boolean value) {
		signalValue = value;

		if ((Event.getEventQueue().getRunTime() == 0) && setCounterPre < COUNTER_PRE_MAX) {
			setCounterPre++;
			for (Gatter gatter : gatterList) {
				gatter.setOutputValue();
			}
		}
		int time = Event.getEventQueue().getRunTime();
		if (time > 0) {
			SignalKind kind = Signal.getSignalFromList(signalList,this.getName()).getSignalKind();
			if (kind.equals(SignalKind.INPUT) || kind.equals(SignalKind.OUTPUT)) {
				Result.storeCurrentState(time, signalList);
			}
		}
	}

	/**
	 * @return Gatterliste des Signals
	 */
	public ArrayList<Gatter> getGatterList() {
		return gatterList;
	}

	/**
	 * @return Art des Signals (Inner, Input, Output)
	 */
	public SignalKind getSignalKind() {
		return signalKind;
	}

	/**
	 * @param signalKind Art des Signals (Inner, Input, Output)
	 */
	public void setSignalKind(SignalKind signalKind) {
		this.signalKind = signalKind;
	}

	/**
	 * Hilfsmethode, sucht ein Signal aus der Signal Liste und gibt dieses zurück.
	 * @param signalList Liste der Signale
	 * @param sigName Name des Signals
	 * @return Signal aus der Signalliste, falls gefunden, ansonsten {@code null}
	 */
	public static Signal getSignalFromList(ArrayList<Signal> signalList, String sigName) {
		for (Signal signal : signalList) {
			if (signal.getName().equals(sigName)) {
				return signal;
			}
		}
		return null;
	}
	
	/**
	 * @param signalList List der Signales
	 */
	public static void setSignalList(ArrayList<Signal> signalList) {
		Signal.signalList = signalList;
	}
	

	

	

	
}
