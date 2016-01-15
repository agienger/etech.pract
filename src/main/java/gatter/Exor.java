package gatter;

public class Exor extends Gatter {

	public Exor(int anzahl, int wTime) {
		super(anzahl, wTime);
	}
	
	/**
	 * 
	 * @see gatter.Gatter#calculateOutputValue()
	 * Der Ausgang ist genau dann logisch {@code true} ist, wenn an einer ungeraden Anzahl von 
	 * Eingängen {@code true}
	 * anliegt und an den restlichen {@code false}. 
	 */
	@Override
	public boolean calculateOutputValue() {
		int trueCounter = 0;
		for (int i = 0; i < this.getInputSignalArray().length; i++) {
			if(this.getInputSignalArray()[i].getValue() == true) {
				trueCounter++;
			}
		}
		
		return (trueCounter % 2 != 0);
	}

}