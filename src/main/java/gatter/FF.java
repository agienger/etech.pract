package gatter;

public class FF extends FlipFlop {

	private boolean previousTakt;

	public FF(int wTime) {
		super(wTime);
		previousTakt = false;
	}

	@Override
	protected  boolean isFlipFlopActive() {
		boolean risingEdge = (previousTakt == false && this.getTakt() == true);
		previousTakt = this.getTakt();
		return risingEdge;
	}

}
