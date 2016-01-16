package gatter;

public abstract class FlipFlop extends Gatter {

	
	/*
	 * Takteingang ist der erste, Dateneingang ist der zweite Eingang
	 */
	public FlipFlop(int wTime) {
		super(2, wTime);
		
	}
	
	
	protected boolean getTakt(){
		return this.getInputSignalArray()[0].getValue();
	}
	
	protected abstract boolean isFlipFlopActive();
	
	public boolean calculateOutputValue(){
		if (isFlipFlopActive()){
			return this.getInputSignalArray()[1].getValue();
		}
		return this.getLastCalculatedValue();
	}


}
