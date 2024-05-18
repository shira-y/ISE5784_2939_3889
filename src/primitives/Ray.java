package primitives;

/**
 * Ray class is used for the set of points on a line that are on one side of a
 * given point on the line called the head of the fund. Defined by point and
 * direction/
 */
public class Ray {
	private final Point head;
	private final Vector direction;

	/**
	 * A constructor with a point and a direction vector
	 * 
	 * @param head
	 * @param direction
	 */
	public Ray(Point head, Vector direction) {
		this.head = head;
		this.direction = direction.normalize();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (obj instanceof Ray other) && this.head.equals(other.head) && this.direction.equals(other.direction);

	}

	@Override
	public String toString() {
		return "Ray{" + "head=" + head + ", direction=" + direction + '}';
	}

}
