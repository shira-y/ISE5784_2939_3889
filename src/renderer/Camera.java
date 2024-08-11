package renderer;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import primitives.*;
import renderer.PixelManager.Pixel;

import static primitives.Util.*;

/**
 * Represents a camera in 3D space, which is used to render images of scenes by
 * casting rays through a view plane. The {@code Camera} class provides methods
 * to set up the camera's position, orientation, and view plane parameters. It
 * also includes support for multi-threaded rendering and adaptive
 * super-sampling to improve image quality and performance.
 * 
 * <p>
 * This class is designed using the Builder design pattern, allowing the camera
 * to be constructed with various optional settings.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * Camera camera = Camera.getBuilder().setLocation(new Point(0, 0, 0))
 * 		.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(1000)
 * 		.setImageWriter(new ImageWriter("image", 1000, 1000)).setRayTracer(new RayTracerBasic(scene))
 * 		.setSuperSampling(500).setAdaptive(true).build();
 * camera.renderImage();
 * camera.writeToImage();
 * }</pre>
 * 
 */
public class Camera implements Cloneable {
	/**
	 * The position of the camera.
	 */
	private Point p0;

	/**
	 * The vector pointing in the direction the camera is looking.
	 */
	private Vector vTo;

	/**
	 * The vector pointing upwards from the camera's perspective.
	 */
	private Vector vUp;

	/**
	 * The vector pointing to the right from the camera's perspective.
	 */
	private Vector vRight;

	/**
	 * The width of the view plane.
	 */
	private double width = 0.0;

	/**
	 * The height of the view plane.
	 */
	private double height = 0.0;

	/**
	 * The distance from the camera to the view plane.
	 */
	private double distance = 0.0;

	/**
	 * Image writer for rendering the image.
	 */
	private ImageWriter imageWriter;

	/**
	 * Ray tracer for generating rays and tracing them in the scene.
	 */
	private RayTracerBase rayTracer;

	/**
	 * Pixel manager for supporting:
	 * <ul>
	 * <li>multi-threading</li>
	 * <li>debug print of progress percentage in the console window/tab</li>
	 * </ul>
	 */
	private PixelManager pixelManager;
	/**
	 * Interval for printing progress updates.
	 */
	private double printInterval = 0;
	/**
	 * Number of threads to use for multi-threaded rendering.
	 */
	private int threadsCount = 0;
	/**
	 * Number of spare threads to leave unused when using all available cores.
	 */
	private final int SPARE_THREADS = 2;

	/**
	 * Flag for enabling or disabling adaptive super-sampling.
	 */
	private boolean adaptive;

	/**
	 * Private constructor to prevent direct instantiation.
	 */
	private Camera() {
	}

	/**
	 * Gets the location of the camera.
	 * 
	 * @return the location of the camera.
	 */
	public Point getP0() {
		return p0;
	}

	/**
	 * Gets the upward vector of the camera.
	 * 
	 * @return the upward vector of the camera.
	 */
	public Vector getVUp() {
		return vUp;
	}

	/**
	 * Gets the forward vector of the camera.
	 * 
	 * @return the forward vector of the camera.
	 */
	public Vector getVTo() {
		return vTo;
	}

	/**
	 * Gets the rightward vector of the camera.
	 * 
	 * @return the rightward vector of the camera.
	 */
	public Vector getVRight() {
		return vRight;
	}

	/**
	 * Gets the width of the view plane.
	 * 
	 * @return the width of the view plane.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Gets the height of the view plane.
	 * 
	 * @return the height of the view plane.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Gets the distance from the camera to the view plane.
	 * 
	 * @return the distance from the camera to the view plane.
	 */
	public double getDistance() {
		return distance;
	}

	
	/**
	 * Returns a new {@link Camera.Builder} instance for constructing a
	 * {@code Camera}.
	 * 
	 * @return a new Builder instance for constructing a Camera.
	 */
	public static Builder getBuilder() {
		return new Builder();
	}

	/**
	 * Static nested Builder class for constructing {@code Camera} instances.
	 */
	public static class Builder {

		/**
		 * The Camera instance being built.
		 */
		private final Camera camera = new Camera();

		/**
		 * Error message for missing rendering data.
		 */
		private static final String MISSING_RENDERING_DATA = "Missing rendering data";

		/**
		 * The name of the Camera class.
		 */
		private static final String CAMERA_CLASS_NAME = Camera.class.getName();

		/**
		 * Field name for the camera's location (p0).
		 */
		private static final String LOCATION_FIELD = "Location (p0)";

		/**
		 * Field name for the camera's direction vectors (vTo and vUp).
		 */
		private static final String DIRECTION_FIELD = "Direction vectors (vTo and vUp)";

		/**
		 * Field name for the view plane size (width and height).
		 */
		private static final String VP_SIZE_FIELD = "View plane size (width and height)";

		/**
		 * Field name for the view plane distance (distance).
		 */
		private static final String VP_DISTANCE_FIELD = "View plane distance (distance)";

		/**
		 * Field name for the image writer.
		 */
		private static final String IMAGE_WRITER_FIELD = "Image writer (imageWriter)";

		/**
		 * Field name for the ray tracer.
		 */
		private static final String RAY_TRACER_FIELD = "ray tracer (rayTracer)";

		/**
		 * Sets the location of the camera.
		 *
		 * @param p0 the location point.
		 * @return this Builder instance.
		 * @throws IllegalArgumentException if p0 is null.
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
		 * @param vTo the forward vector.
		 * @param vUp the upward vector.
		 * @return this Builder instance.
		 * @throws IllegalArgumentException if vectors are null, equal, or not
		 *                                  perpendicular.
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
		 * @param width  the width of the view plane.
		 * @param height the height of the view plane.
		 * @return this Builder instance.
		 * @throws IllegalArgumentException if width or height are non-positive.
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
		 * @param distance the distance from the camera to the view plane.
		 * @return this Builder instance.
		 * @throws IllegalArgumentException if distance is non-positive.
		 */
		public Builder setVpDistance(double distance) {
			if (distance <= 0) {
				throw new IllegalArgumentException("Distance must be positive");
			}
			camera.distance = distance;
			return this;
		}

		/**
		 * Sets the image writer for the camera.
		 * 
		 * @param imageWriter The image writer to be set.
		 * @return The Builder object for chaining method calls.
		 */
		public Builder setImageWriter(ImageWriter imageWriter) {
			camera.imageWriter = imageWriter;
			return this;
		}

		/**
		 * Sets the ray tracer for the camera.
		 * 
		 * @param rayTracer The ray tracer to be set.
		 * @return The Builder object for chaining method calls.
		 */
		public Builder setRayTracer(RayTracerBase rayTracer) {
			camera.rayTracer = rayTracer;
			return this;
		}

		/**
		 * Sets the adaptive super-sampling flag for the camera.
		 *
		 * @param adaptive the adaptive flag to be set.
		 * @return this Builder instance.
		 */
		public Builder setAdaptive(boolean adaptive) {
			camera.adaptive = adaptive;
			return this;
		}

		/**
		 * Builds and returns the {@code Camera} instance.
		 *
		 * @return the constructed {@code Camera} instance.
		 * @throws MissingResourceException if any required field is missing.
		 */
		public Camera build() {
			if (camera.p0 == null)
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, LOCATION_FIELD);

			if (camera.vTo == null || camera.vUp == null)
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, DIRECTION_FIELD);
			if (camera.vTo.equals(camera.vUp))
				throw new IllegalArgumentException("Direction vectors cannot be the same");
			if (!isZero(camera.vTo.dotProduct(camera.vUp)))
				throw new IllegalArgumentException("Direction vectors must be perpendicular");

			if (camera.vRight == null)
				camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

			if (camera.width == 0.0 || camera.height == 0.0)
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, VP_SIZE_FIELD);

			if (alignZero(camera.width) <= 0)
				throw new IllegalStateException("Width must be positive");
			if (alignZero(camera.height) <= 0)
				throw new IllegalStateException("Height must be positive");
			if (camera.distance == 0.0)
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, VP_DISTANCE_FIELD);
			if (alignZero(camera.distance) <= 0)
				throw new IllegalStateException("Distance must be positive");

			if (camera.imageWriter == null)
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, IMAGE_WRITER_FIELD);
			if (camera.rayTracer == null)
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, RAY_TRACER_FIELD);

			try {
				return (Camera) camera.clone();
			} catch (CloneNotSupportedException e) {
				throw new AssertionError("Camera cloning is not supported", e); // Should not happen
			}
		}
	}

	/**
	 * Sets the number of threads to use for multi-threaded rendering.
	 * <p>
	 * If the value is -2, the camera will use all available cores minus a few spare
	 * threads. If the value is -1 or greater, the camera will use the specified
	 * number of threads.
	 * </p>
	 * 
	 * @param threads the number of threads to use.
	 * @return this Camera instance.
	 * @throws IllegalArgumentException if the number of threads is less than -2.
	 */
	public Camera setMultithreading(int threads) {
		if (threads < -2)
			throw new IllegalArgumentException("Multithreading must be -2 or higher");
		if (threads >= -1)
			threadsCount = threads;
		else { // == -2
			int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
			threadsCount = cores <= 2 ? 1 : cores;
		}
		return this;
	}

	/**
	 * Sets the interval for printing debug progress updates during rendering.
	 * 
	 * @param interval the time interval in seconds.
	 * @return this Camera instance.
	 */
	public Camera setDebugPrint(double interval) {
		printInterval = interval;
		return this;
	}

	/**
	 * Casts a ray through the center of a given pixel, computes the color by
	 * tracing the ray, and colors the pixel.
	 *
	 * @param nX  number of pixels in the x direction.
	 * @param nY  number of pixels in the y direction.
	 * @param col pixel's column number.
	 * @param row pixel's row number.
	 */
	private void castRay(int nX, int nY, int col, int row) {
		imageWriter.writePixel(col, row, rayTracer.traceRay(constructRay(nX, nY, col, row)));
		pixelManager.pixelDone();
	}

	/**
	 * Draws a grid on the image with the specified interval and color.
	 * 
	 * @param color    The color of the grid lines.
	 * @param interval The spacing between grid lines.
	 * @return this Camera instance.
	 * @throws IllegalArgumentException if the interval is not positive.
	 */
	public Camera printGrid(int interval, Color color) {
		if (interval <= 0)
			throw new IllegalArgumentException("Interval must be positive");

		int width = imageWriter.getNx();
		int height = imageWriter.getNy();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x % interval == 0 || y % interval == 0)
					imageWriter.writePixel(x, y, color);
			}
		}
		return this;
	}

	/**
	 * Writes the image data to the image file using the appropriate method from the
	 * image writer.
	 * 
	 * @return this Camera instance.
	 * @throws IllegalStateException if the image writer is not initialized.
	 */
	public Camera writeToImage() {
		imageWriter.writeToImage();
		return this;
	}

	/**
	 * Renders the image by casting rays through each pixel in the view plane and
	 * tracing them through the scene.
	 * 
	 * <p>
	 * If multi-threading is enabled, the rendering will be performed using multiple
	 * threads. If adaptive super-sampling is enabled, the camera will use adaptive
	 * techniques to optimize rendering quality and performance.
	 * </p>
	 * 
	 * @return this Camera instance.
	 * @throws MissingResourceException if required resources such as the image
	 *                                  writer or ray tracer are not initialized.
	 */
	public Camera renderImage() {
		// Check for missing resources
		if (imageWriter == null) {
			throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
		}
		if (rayTracer == null) {
			throw new MissingResourceException("missing resource", RayTracerBase.class.getName(), "");
		}

		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();

		pixelManager = new PixelManager(nY, nX, printInterval);

		// Multi-threaded rendering
		if (threadsCount != 0) {
			var threads = new LinkedList<Thread>(); // List of threads
			while (threadsCount-- > 0) { // Add appropriate number of threads
				threads.add(new Thread(() -> {
					Pixel pixel;
					while ((pixel = pixelManager.nextPixel()) != null)
						// Cast ray through pixel and color it
						castRay(nX, nY, pixel.col(), pixel.row());
				}));
			}
			// Start all threads
			for (var thread : threads)
				thread.start();
			// Wait until all threads have finished
			try {
				for (var thread : threads)
					thread.join();
			} catch (InterruptedException ignore) {
			}
		} else if (!adaptive) {
			// Non-adaptive rendering
			for (int i = 0; i < nX; i++) {
				for (int j = 0; j < nY; j++) {
					castRay(nX, nY, j, i);
				}
			}
		} else {
			// Adaptive super-sampling rendering
			for (int i = 0; i < nX; i++) {
				for (int j = 0; j < nY; j++) {
					List<Ray> rays;
					Ray centerRay = constructRay(nX, nY, j, i);
					rays = List.of(centerRay);
					Color color = rayTracer.adaptiveTraceRays(rays);
					imageWriter.writePixel(j, i, color);
				}
			}
		}

		return this;
	}

	/**
	 * Constructs a ray through the center of a pixel.
	 *
	 * @param nX number of columns.
	 * @param nY number of rows.
	 * @param j  index of column pixel.
	 * @param i  index of row pixel.
	 * @return Ray from the camera's {@code p0} to the view plane at (i,j).
	 */
	 public Ray constructRay(int nX, int nY, int j, int i) {
	        Point pIJ = getPixelCenter(nX, nY, j, i);
	        return new Ray(p0, pIJ.subtract(p0).normalize());
	    }


	/**
	 * Helper function to find the center of a pixel.
	 *
	 * @param nX number of columns.
	 * @param nY number of rows.
	 * @param j  index of column pixel.
	 * @param i  index of row pixel.
	 * @return the center of the pixel.
	 */
	private Point getPixelCenter(int nX, int nY, int j, int i) {
		// Calculate image center
		Point pC = this.p0.add(this.vTo.scale(this.distance));

		// Ratio (pixel width and height)
		double Ry = this.height / (double) nY;
		double Rx = this.width / (double) nX;

		// Calculate pixel center
		Point Pij = pC;
		double Yi = -((double) i - (double) (nY - 1) / 2.0D) * Ry;
		double Xj = ((double) j - (double) (nX - 1) / 2.0D) * Rx;

		if (!Util.isZero(Yi)) {
			Pij = pC.add(this.vUp.scale(Yi));
		}

		if (!Util.isZero(Xj)) {
			Pij = Pij.add(this.vRight.scale(Xj));
		}

		return Pij;
	}
}
