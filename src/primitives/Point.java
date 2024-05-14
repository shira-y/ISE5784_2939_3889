package primitives;

public class Point {
	final Double3 xyz;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return obj instanceof Point other && xyz.equals(other.xyz);
	}

	@Override
	public String toString() {
		return "" + xyz;
	}
}