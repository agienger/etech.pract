package gatter;

public class Exor extends Gatter {

	public Exor(int anzahl, int wTime) {
		super(anzahl, wTime);
	}
	
	@Override
	public boolean calculateOutputValue() {
		int trueCounter = 0;
		for (int i = 0; i < this.getInputSignalArray().length; i++) {
			if(this.getInputSignalArray()[i].getValue() == true) {
				trueCounter++;
			}
		}
		
		return (trueCounter %2 == 1);
	}

}