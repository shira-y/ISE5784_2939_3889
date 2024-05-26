package primitives;

/**
 * Ray class is used for the set of points on a line that are on one side of a
 * given point on the line called the head of the fund. Defined by point and
 * direction/
 */
public class Ray {
	/**
	 * head the head point of the ray direction the direction vector of the ray
	 */
	private final Point head;
	private final Vector direction;

	/**
	 * A constructor with a point and a direction vector
	 * 
	 * @param head      the head point of the ray
	 * @param direction the direction vector of the ray
	 */
	public Ray(Point head, Vector direction) {
		this.head = head;
		this.direction = direction.normalize();
	}
	
	public Vector getDirection() {
		return direction;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (obj instanceof Ray other) && this.head.equals(other.head) && this.getDirection().equals(other.getDirection());

	}

	@Override
	public String toString() {
		return "Ray{" + head + getDirection() + '}';
	}
//
//	public Point getPoint(int i) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public Point getHead() {
		// TODO Auto-generated method stub
		return head;
	}
	


	

}
