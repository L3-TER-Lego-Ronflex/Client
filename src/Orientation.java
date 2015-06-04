public class Orientation {
	private int i;

	public Orientation(int i) {
		this.i = i;
	}

	public final static Orientation EAST = new Orientation(0);
	public final static Orientation NORTH = new Orientation(1);
	public final static Orientation WEST = new Orientation(2);
	public final static Orientation SOUTH = new Orientation(3);

	public Orientation rotate(boolean trigo) {
		if (trigo) {
			int i = this.i + 1;
			if (i > 3)
				return new Orientation(0);
			else
				return new Orientation(i);
		}
		else {
			int i = this.i - 1;
			if (i < 0)
				return new Orientation(3);
			else
				return new Orientation(i);
		}
	}

	@Override
	public String toString() {
		switch (this.i) {
		case 0:
			return "EAST";
		case 1:
			return "NORTH";
		case 2:
			return "WEST";
		case 3:
			return "SOUTH";
		default:
			return "Undefined";
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Orientation) {
			return this.i == ((Orientation) obj).i;
		} else {
			return super.equals(obj);
		}
	}
}
