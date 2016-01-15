package gatter;

public class Latch extends FlipFlop {

	public Latch(int wTime) {
		super(wTime);
	}

	/*
	 * 
	 * @see teil3.FlipFlop#calculateNewOutput() Beim Latch wird nur dann ein
	 * output Signal gesetzt, wenn der Takt auf true steht
	 */
	@Override
	protected boolean isFlipFlopActive() {
		return this.getTakt();
	}
}
