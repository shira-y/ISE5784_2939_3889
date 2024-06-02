package renderer;

import java.util.MissingResourceException;

import primitives.*;

public class Camera implements Cloneable {
	private Point p0;
	private Vector vTo;
	private Vector vUp;
	private Vector vRight;
	private double width = 0.0;
	private double height = 0.0;
	private double distance = 0.0;

	private Camera() {

	}

	public Point getP0() {
		return p0;
	}

	public Vector getVUp() {
		return vUp;
	}

	public Vector getVTo() {
		return vTo;
	}

	public Vector getVRight() {
		return vRight;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getDistance() {
		return distance;
	}

	public Ray constructRay(int nX, int nY, int j, int i) {
		return null;
	}

	public static Builder getBuilder() {
		return new Builder();
	}

	/**
	 * Static nested Builder class
	 */
	public static class Builder {
		private final Camera camera = new Camera();

		private static final String MISSING_RENDERING_DATA = "Missing rendering data";
		private static final String CAMERA_CLASS_NAME = Camera.class.getName();
		private static final String LOCATION_FIELD = "Location (p0)";
		private static final String DIRECTION_FIELD = "Direction vectors (vTo and vUp)";
		private static final String VRIGHT_FIELD = "Right vector (vRight)";
		private static final String VP_SIZE_FIELD = "View plane size (width and height)";
		private static final String VP_DISTANCE_FIELD = "View plane distance (distance)";

		/**
		 * Sets the location of the camera.
		 * 
		 * @param p0 the location point
		 * @return this Builder instance
		 * @throws IllegalArgumentException if p0 is null
		 */
		public Builder setLocation(Point p0) {
			if (p0 == null) {
				throw new IllegalArgumentException("Location point cannot be null");
			}
			camera.p0 = p0;
			return this;
		}

		/**
		 * Sets the direction of the camera.
		 * 
		 * @param vTo the forward vector
		 * @param vUp the upward vector
		 * @return this Builder instance
		 * @throws IllegalArgumentException if vectors are null, equal, or not
		 *                                  perpendicular
		 */
		public Builder setDirection(Vector vTo, Vector vUp) {
			if (vTo == null || vUp == null) {
				throw new IllegalArgumentException("Direction vectors cannot be null");
			}
			if (vTo.equals(vUp)) {
				throw new IllegalArgumentException("Direction vectors cannot be the same");
			}
			if (vTo.dotProduct(vUp) != 0) {
				throw new IllegalArgumentException("Direction vectors must be perpendicular");
			}
			camera.vTo = vTo.normalize();
			camera.vUp = vUp.normalize();
			camera.vRight = vTo.crossProduct(vUp).normalize();
			return this;
		}

		/**
		 * Sets the size of the view plane.
		 * 
		 * @param width  the width of the view plane
		 * @param height the height of the view plane
		 * @return this Builder instance
		 * @throws IllegalArgumentException if width or height are non-positive
		 */
		public Builder setVpSize(double width, double height) {
			if (width <= 0 || height <= 0) {
				throw new IllegalArgumentException("Width and height must be positive");
			}
			camera.width = width;
			camera.height = height;
			return this;
		}

		/**
		 * Sets the distance of the view plane from the camera.
		 * 
		 * @param distance the distance from the camera to the view plane
		 * @return this Builder instance
		 * @throws IllegalArgumentException if distance is non-positive
		 */
		public Builder setVpDistance(double distance) {
			if (distance <= 0) {
				throw new IllegalArgumentException("Distance must be positive");
			}
			camera.distance = distance;
			return this;
		}

		/**
		 * Builds and returns the Camera instance.
		 * 
		 * @return the constructed Camera instance
		 * @throws MissingResourceException if any required field is missing
		 */
		public Camera build() {
			if (camera.p0 == null) {
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, LOCATION_FIELD);
			}
			if (camera.vTo == null || camera.vUp == null) {
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, DIRECTION_FIELD);
			}
			if (camera.width == 0.0 || camera.height == 0.0) {
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, VP_SIZE_FIELD);
			}
			if (camera.distance == 0.0) {
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, VP_DISTANCE_FIELD);
			}

			// Calculate and set the vRight vector if not already set
			if (camera.vRight == null) {
				camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
			}
			// Return a clone of the camera
			return (Camera) camera.clone();
		}

	}

	@Override
	protected Camera clone() {
		try {
			return (Camera) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // Can't happen
		}
	}

}
