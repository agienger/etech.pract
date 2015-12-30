package gatter;

public class Not extends Gatter {
	
	public Not(int wTime) {
		super(1, wTime);
	}
	
	@Override
	public boolean calculateOutputValue() {
		return !(getInputSignalArray()[0].getValue());
	}

}