package gatter;

public class Buf extends Gatter {

	
	public Buf(int wTime) {
		super(1, wTime);
	}

	@Override
	public boolean calculateOutputValue() {
		return getInputSignalArray()[0].getValue();
	}

}