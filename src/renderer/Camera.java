package renderer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;



import primitives.*;
import renderer.PixelManager.Pixel;

import static primitives.Util.*;

/**
 * Represents a camera in 3D space.
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
	 * Image writer for rendering the image
	 */
	private ImageWriter imageWriter;

	/**
	 * Ray tracer for generating rays and tracing them in the scene
	 */
	private RayTracerBase rayTracer;

	/**
	 * Pixel manager for supporting:
	 * <ul>
	 * <li>multi-threading</li>
	 * <li>debug print of progress percentage in Console window/tab</li>
	 * <ul>
	 */
	private PixelManager pixelManager;
	/**
	 * printInterval field
	 */
	private double printInterval = 0;
	/**
	 * Number of threads
	 */
	private int threadsCount = 0;
	/**
	 * Spare threads if trying to use all the cores
	 */
	private final int SPARE_THREADS = 2;
    private int superSampling = 0;

	private boolean adaptive;

    private Point centerPoint;
	/**
	 * Private constructor to prevent direct instantiation.
	 */

	private Camera() {
	}

	/**
	 * getter of p0
	 * 
	 * @return the location of the camera
	 */
	public Point getP0() {
		return p0;
	}

	/**
	 * getter of vUp
	 * 
	 * @return the upward vector of the camera
	 */
	public Vector getVUp() {
		return vUp;
	}

	/**
	 * getter of vTo
	 * 
	 * @return the forward vector of the camera
	 */
	public Vector getVTo() {
		return vTo;
	}

	/**
	 * getter of vRight
	 * 
	 * @return the rightward vector of the camera
	 */
	public Vector getVRight() {
		return vRight;
	}

	/**
	 * getter of width
	 * 
	 * @return the width of the view plane
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * getter of height
	 * 
	 * @return the height of the view plane
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * getter of distance
	 * 
	 * @return the distance from the camera to the view plane
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Constructs a ray through a given pixel in the view plane.
	 *
	 * @param nX the number of horizontal pixels
	 * @param nY the number of vertical pixels
	 * @param j  the horizontal index of the pixel
	 * @param i  the vertical index of the pixel
	 * @return the constructed ray
	 */
	public Ray constructRay(int nX, int nY, int j, int i) {
		Point pc = p0.add(vTo.scale(distance));
		double rY = height / nY;
		double rX = width / nX;
		double xJ = (j - (nX - 1) / 2.0) * rX;
		double yI = -(i - (nY - 1) / 2.0) * rY;

		Point pIJ = pc;

		if (xJ != 0)
			pIJ = pIJ.add(vRight.scale(xJ));
		if (yI != 0)
			pIJ = pIJ.add(vUp.scale(yI));

		return new Ray(p0, pIJ.subtract(p0).normalize());
		
	}
  

	/**
	 * getter of Builder
	 * 
	 * @return a new Builder instance for constructing a Camera
	 */
	public static Builder getBuilder() {
		return new Builder();
	}

	/**
	 * Static nested Builder class for constructing Camera instances.
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
		 * Setter for the image writer.
		 * 
		 * @param imageWriter The image writer to be set.
		 * @return The Camera object for chaining method calls.
		 */
		public Builder setImageWriter(ImageWriter imageWriter) {
			camera.imageWriter = imageWriter;
			return this;
		}

		/**
		 * Setter for the ray tracer.
		 * 
		 * @param rayTracer The ray tracer to be set.
		 * @return The Camera object for chaining method calls.
		 */
		public Builder setRayTracer(RayTracerBase rayTracer) {
			camera.rayTracer = rayTracer;
			return this;
		}
		  /**
	     * sets the superSampling flag of the Camera.
	     *
	     * @param superSampling the SuperSampling flag and amount of rays in beam
	     * @return this Camera object
	     */
	    public Builder setSuperSampling(int superSampling) {
	        camera.superSampling = superSampling;
	        return this;
	    }

	    /**
	     * set the adaptive flag.
	     *
	     * @param adaptive the adaptive flag to be set
	     * @return the Camera object
	     */
	    public Builder setAdaptive(boolean adaptive) {
	    	camera.adaptive = adaptive;
	        return this;
	    }

		/**
		 * Builds and returns the Camera instance.
		 *
		 * @return the constructed Camera instance
		 * @throws MissingResourceException if any required field is missing
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
			// Calculate and set the vRight vector if not already set
			if (camera.vRight == null)
				camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

			if (camera.width == 0.0 || camera.height == 0.0)
				throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, VP_SIZE_FIELD);
			// Validate the values of the fields
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

			// Return a clone of the camera
			try {
				return (Camera) camera.clone();
			} catch (CloneNotSupportedException e) {
				throw new AssertionError(); // Can't happen
			}
		}

	}

	/**
	 * Method for rendering image
	 */
	public void renderImage() {
		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();

		pixelManager = new PixelManager(nY, nX, printInterval);

		if (threadsCount == 0) {

			for (int i = 0; i < nY; i++) {
				for (int j = 0; j < nX; j++) {
					castRay(nX, nY, j, i);
					
				}
			}
		} else {
			var threads = new LinkedList<Thread>(); // list of threads
			while (threadsCount-- > 0) // add appropriate number of threads
				threads.add(new Thread(() -> { // add a thread with its code
					Pixel pixel; // current pixel(row,col)
					// allocate pixel(row,col) in loop until there are no more pixels
					while ((pixel = pixelManager.nextPixel()) != null)
						// cast ray through pixel (and color it – inside castRay)
						castRay(nX, nY, pixel.col(), pixel.row());
						 
				}));
			// start all the threads
			for (var thread : threads)
				thread.start();
			// wait until all the threads have finished
			try {
				for (var thread : threads)
					thread.join();
			} catch (InterruptedException ignore) {
			}
		}
		if (!adaptive) {
            //go over all the pixels
            for (int i = 0; i < nX; i++) {
                for (int j = 0; j < nY; j++) {
                    // construct a ray through the current pixel
                    Ray rays = this.constructRayThroughCenter(nX, nY, j, i);
                    // get the  color of the point from trace ray
                    Color color = rayTracer.traceRay(rays);
                    // write the pixel color to the image
                    imageWriter.writePixel(j, i, color);
                }
            }
		}
            else
		{
			 for (int i = 0; i < nX; i++) {
	                for (int j = 0; j < nY; j++) {
	                    // construct a ray through the current pixel
	                    List<Ray> rays = this.constructRaysForPixel(nX, nY, j, i);
	                    // get the  color of the point from trace ray
	                    Color color = rayTracer.adaptiveTraceRays(rays);
	                    // write the pixel color to the image
	                    imageWriter.writePixel(j, i, color);
	                }
	            }
		}
	}
		
		/**
	     * Helper method for rendering image
	     * renders a given pixel
	     *
	     * @param nX number of columns
	     * @param nY number of rows
	     * @param j  column of the pixel
	     * @param i  row of the pixel
	     */
	    private void renderHelper(int nX, int nY, int j, int i) {
	       if (!adaptive) {
	            // construct a ray through the current pixel
	            Ray ray = this.constructRayThroughCenter(nX, nY, j, i);
	            // get the  color of the point from trace ray
	            Color color = rayTracer.traceRay(ray);
	            // write the pixel color to the image
	            imageWriter.writePixel(j, i, color);
	        }

	        else {
	            // construct a ray through the current pixel
	            List <Ray> rays = this.constructRaysForPixel(nX, nY, j, i);
	            // get the  color of the point from trace ray
	            Color color = rayTracer.adaptiveTraceRays(rays);
	            // write the pixel color to the image
	            imageWriter.writePixel(j, i, color);
	        }
	    }
	    
	    /**
	     * Constructing a list of rays through a pixel
	     *
	     * @param nX amount of columns
	     * @param nY amount of rows
	     * @param j  index of column pixel
	     * @param i  index of row pixel
	     * @return a list of 5 rays: 4 rays at the edges of the pixel and 1 ray through the center
	     */
	    public List<Ray> constructRaysForPixel(int nX, int nY, int j, int i) {
	        List<Ray> rays = new ArrayList<>();
	        Point Pij = getPixelCenter(nX, nY, j, i);

	        // Construct the center ray
	        rays.add(new Ray(this.p0, Pij.subtract(this.p0)));

	        // Construct the 4 edge rays
	        double pixelWidth = this.width / nX;
	        double pixelHeight = this.height / nY;

	        // Top-left corner
	        rays.add(constructRayThroughPoint(nX, nY, j - 0.5, i + 0.5));
	        // Top-right corner
	        rays.add(constructRayThroughPoint(nX, nY, j + 0.5, i + 0.5));
	        // Bottom-left corner
	        rays.add(constructRayThroughPoint(nX, nY, j - 0.5, i - 0.5));
	        // Bottom-right corner
	        rays.add(constructRayThroughPoint(nX, nY, j + 0.5, i - 0.5));

	        return rays;
	    }
	    
	    /**
	     * Construct a ray through the center of a pixel
	     *
	     * @param nX amount of columns
	     * @param nY amount of rows
	     * @param j  index of column pixel
	     * @param i  index of row pixel
	     * @return a ray passing through the center of the pixel
	     */
	    private Ray constructRayThroughCenter(int nX, int nY, int j, int i) {
	        Point Pc = p0.add(vTo.scale(distance)); //the center of the screen point
	        double y_sample_i = ((i - nY / 2d) * (this.height / nY)); //The pixel starting point on the y axis
	        double x_sample_j = ((j - nX / 2d) * (this.width / nX)); //The pixel starting point on the x axis
	        Point Pij = Pc; //The point at the pixel through which a beam is fired
	        //Moving the point through which a beam is fired on the x axis
	        if (!Util.isZero(x_sample_j)) {
	            Pij = Pij.add(vRight.scale(x_sample_j));
	        }
	        //Moving the point through which a beam is fired on the y axis
	        if (!Util.isZero(y_sample_i)) {
	            Pij = Pij.add(vUp.scale(-y_sample_i));
	        }
	        Vector Vij = Pij.subtract(p0);
	        return new Ray(p0, Vij); //create the ray through the center of the pixel
	    }

	    /**
	     * Construct a ray through a specific point in the pixel
	     *
	     * @param nX amount of columns
	     * @param nY amount of rows
	     * @param j  column coordinate (can be fractional)
	     * @param i  row coordinate (can be fractional)
	     * @return Ray from camera's p0 through the specified point
	     */
	    private Ray constructRayThroughPoint(int nX, int nY, double j, double i) {
	        Point pixelPoint = getPixelPoint(nX, nY, j, i);
	        Vector direction = pixelPoint.subtract(this.p0);
	        if (direction.length() == 0) {
	            // If the direction vector is zero, return null or handle it as appropriate
	            return null;
	        }
	        return new Ray(this.p0, direction);
	    }

	    /**
	     * Get a point within or on the edge of a pixel
	     *
	     * @param nX amount of columns
	     * @param nY amount of rows
	     * @param j  column coordinate (can be fractional)
	     * @param i  row coordinate (can be fractional)
	     * @return the point at the specified coordinates
	     */
	    private Point getPixelPoint(int nX, int nY, double j, double i) {
	        double rX = this.width / nX;
	        double rY = this.height / nY;
	        double xj = (j - (nX / 2.0)) * rX;
	        double yi = -(i - (nY / 2.0)) * rY;

	        Point Pc = this.p0.add(this.vTo.scale(this.distance));
	        
	        if (xj != 0) Pc = Pc.add(this.vRight.scale(xj));
	        if (yi != 0) Pc = Pc.add(this.vUp.scale(yi));

	        return Pc;
	    }
	    
	    /**
	     * Helper function to find the center of a pixel
	     *
	     * @param nX number of columns
	     * @param nY number of rows
	     * @param j  j index of pixel
	     * @param i  i index of pixel
	     * @return the center of the pixel
	     */
	    private Point getPixelCenter(int nX, int nY, int j, int i) {
	        //image center
	        Point pC = this.p0.add(this.vTo.scale(this.distance));

	        // Ratio (pixel width and height)
	        double Ry = this.height / (double) nY;
	        double Rx = this.width / (double) nX;

	        //pixel[i,j] center
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

	public Camera setDebugPrint(double interval) {
		printInterval = interval;
		return this;
	}
//	 /**
//     * Performs adaptive super-sampling by casting multiple rays through a pixel
//     * with varying sub-pixel offsets and calculates the color at that pixel.
//     *
//     * @param nX        the number of pixels along the x-axis
//     * @param nY        the number of pixels along the y-axis
//     * @param j         the pixel's x-coordinate
//     * @param i         the pixel's y-coordinate
//     * @param numOfRays the number of rays to be cast through the pixel
//     * @return the color at the pixel
//     * @throws MissingResourceException if the imageWriter or viewPlane dimensions were not set
//     */
//    private Color adaptiveSuperSampling(int nX, int nY, int j, int i, int numOfRays) {
//
//        Vector Vright = vRight;
//        Vector Vup = vUp;
//        Point cameraLocation = this.p0;
//        int numOfRaysInRowCol = (int) Math.floor(Math.sqrt(numOfRays));
//
//        // If only one ray is used, directly trace the ray through the pixel
//        if (numOfRaysInRowCol == 1) {
//            return castRay(nX, nY, j, i);
//        }
//
//        Point pIJ = getCenterOfPixel(nX, nY, j, i);
//
//        // Calculate the ratios of pixel width and height
//        double rY = alignZero(height / nY);
//        double rX = alignZero(width / nX);
//
//        double PRy = rY / numOfRaysInRowCol;
//        double PRx = rX / numOfRaysInRowCol;
//
//        // Perform recursive adaptive super sampling
//        return rayTracer.adaptiveSuperSamplingRec(pIJ, rX, rY, PRx, PRy, cameraLocation, Vright, Vup, null);
//    }
//    /**
//     * Calculates the center point of a pixel in the view plane.
//     *
//     * @param nX the number of pixels along the x-axis
//     * @param nY the number of pixels along the y-axis
//     * @param j  the pixel's x-coordinate
//     * @param i  the pixel's y-coordinate
//     * @return the center point of the pixel
//     */
//    private Point getCenterOfPixel(int nX, int nY, int j, int i) {
//
//        // calculate the ratio of the pixel by the height and by the width of the view plane
//
//        // the ratio Ry = h/Ny, the height of the pixel
//        double rY = alignZero(height / nY);
//        // the ratio Rx = w/Nx, the width of the pixel
//        double rX = alignZero(width / nX);
//
//
//        // Calculate the x-coordinate of the center point of the pixel
//        double xJ = alignZero((j - ((nX - 1d) / 2d)) * rX);
//
//        // Calculate the y-coordinate of the center point of the pixel
//        double yI = alignZero(-(i - ((nY - 1d) / 2d)) * rY);
//
//        Point pIJ = centerPoint;
//
//        // Move the center point of the pixel horizontally
//        if (!isZero(xJ)) {
//            pIJ = pIJ.add(vRight.scale(xJ));
//        }
//        // Move the center point of the pixel vertically
//        if (!isZero(yI)) {
//            pIJ = pIJ.add(vUp.scale(yI));
//        }
//
//        return pIJ;
//    }
	/**
	 * Casts a ray through the center of a given pixel, computes the color by
	 * tracing the ray, and color the pixel.
	 *
	 * @param nX number of pixels in the x direction
	 * @param nY number of pixels in the y direction
	 * @param j  x coordinate of the pixel
	 * @param i  y coordinate of the pixel
	 */
//	private void castRay(int nX, int nY, int j, int i) {
//		Ray ray = constructRay(nX, nY, j, i);
//		Color color = rayTracer.traceRay(ray);
//		imageWriter.writePixel(j, i, color);
//	}
    /**
	 * Cast ray from camera and color a pixel
	 * 
	 * @param nX  resolution on X axis (number of pixels in row)
	 * @param nY  resolution on Y axis (number of pixels in column)
	 * @param col pixel's column number (pixel index in row)
	 * @p
	 */

	private void castRay(int nX, int nY, int col, int row) {
		imageWriter.writePixel(col, row, rayTracer.traceRay(constructRay(nX, nY, col, row)));
		pixelManager.pixelDone();
	}

	/**
	 * Method for creating grid lines and print grid
	 * 
	 * @param color    The color of the grid lines.
	 * @param interval The spacing between grid lines.
	 * @return the grid
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
     * Renders a single pixel of the image.
     *
     * @param x            the x-coordinate of the pixel
     * @param y            the y-coordinate of the pixel
     * @param amountOfRays the number of rays to be cast through the pixel
     * @throws MissingResourceException if the imageWriter or viewPlane dimensions were not set
     */
//    private void renderPixel(int x, int y, int amountOfRays) {
//        Color color;
//        int nX = imageWriter.getNx();
//        int nY = imageWriter.getNy();
//
//        // without adaptive superSampling
//        if (!adaptive) {
//
//            // without softshadow
//            if (superSampling == 0) {
//                color = castRay(nX, nY, x, y);
//            }
//            // with softshadow
//            else {
//                color = castRayBeam(nX, nY, x, y);
//            }
//        }
//
//        // with adaptive superSampling
//        else {
//            color = adaptiveSuperSampling(nX, nY, x, y, amountOfRays);
//        }
//     
//        imageWriter.writePixel(x, y, color);
//        pixelManager.pixelDone();
//    }

    /**
     * Casts multiple rays through a pixel with anti-aliasing and calculates the color at that pixel.
     *
     * @param nX the number of pixels along the x-axis
     * @param nY the number of pixels along the y-axis
     * @param j  the pixel's x-coordinate
     * @param i  the pixel's y-coordinate
     * @return the color at the pixel
     */
    private Color castRayBeam(int nX, int nY, int j, int i) {
        int superSamp = superSampling;
        List<Ray> ans = new ArrayList<>();

        // Image center
        Point p = p0.add(vTo.scale(distance));

        // Ratio (pixel width & height)
        double rY = height / nY;
        double rX = width / nX;

        // Pixel[i,j] center
        double yI = -(i - ((nY - 1) / 2)) * rY;
        double xJ = (j - ((nX - 1) / 2)) * rX;

        // Distance between the start of the ray in the pixel
        double dX = (double) rX / superSamp;
        double dY = (double) rY / superSamp;

        // The first point
        double firstX = xJ + ((int) (superSamp / 2)) * dX;
        double firstY = yI + ((int) (superSamp / 2)) * dY;
        Point pIJ = p;
        if (!isZero(firstX))
            pIJ = pIJ.add(vRight.scale(firstX));
        if (!isZero(firstY))
            pIJ = pIJ.add(vUp.scale(firstY));
        Point p1 = pIJ;

        // Generate the rays for the ray beam
        for (int c = 0; c < superSamp; c++) {
            for (int b = 0; b < superSamp; b++) {
                p1 = pIJ;
                if (!isZero(c)) {
                    p1 = p1.add(vRight.scale(dX * c));
                }
                if (!isZero(b)) {
                    p1 = p1.add(vUp.scale(dY * b));
                }

                ans.add(new Ray(p0, p1.subtract(p0)));
            }
        }

        // Calculate the average color from the rays in the beam
        double r = 0, g = 0, b = 0;
        for (Ray ray : ans) {
            Color rayColor = rayTracer.traceRay(ray);
            r += rayColor.getColor().getRed();
            g += rayColor.getColor().getGreen();
            b += rayColor.getColor().getBlue();
        }
        r = r / (ans.size());
        g = g / (ans.size());
        b = b / (ans.size());

        return new Color(r, g, b);
    }
	/**
	 * Writes the image data to the image file using the appropriate method from the
	 * image writer. This method should be invoked with caution as it directly
	 * interacts with the underlying image writer.
	 * 
	 * @return the image
	 * @throws IllegalStateException if the image writer is not initialized.
	 */
	public Camera writeToImage() {
		imageWriter.writeToImage();
		return this;
	}


}
