package primitives;

public class Vector extends Point {

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return obj instanceof Vector other && super.equals(other);
		return false;
	}

	@Override
	public String toString() {
		return "->" + super.toString();
	}
}
