package gatter;

import circuit.Event;
import circuit.Signal;

public class FF extends FlipFlop {

	private boolean previousTakt;
	private Signal negOutput;

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
	
	@Override
	public  void setOutputValue() {
		boolean calculatedValue = calculateOutputValue();
		if (!Event.getEventQueue().isStarted()) {
			super.getOutput().setValue(calculatedValue);
			negOutput.setValue(!(calculatedValue));
			super.lastCalculatedValue = calculatedValue;
		} else 			if (calculatedValue != lastCalculatedValue) {
				lastCalculatedValue = calculatedValue;
				new Event(super.getOutput(),
						Event.getEventQueue().getRunTime() + super.getWaitTime(),
						calculatedValue);
				new Event(negOutput,
						Event.getEventQueue().getRunTime() + super.getWaitTime(),
						!(calculatedValue));
			}
		}

	public void setNegOutput(Signal negOutput) {
		this.negOutput = negOutput;
	}
}