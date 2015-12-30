package gatter;

public class Or extends Gatter {

	public Or(int anzahl, int wTime) {
		super(anzahl, wTime);
	}
	
	@Override
	public boolean calculateOutputValue() {
		for (int i = 0; i < getInputSignalArray().length; i++) {
			if (getInputSignalArray()[i].getValue()) {
				return true;
			}
		}
		return false;
	}

}
