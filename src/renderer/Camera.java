package renderer;

import java.util.MissingResourceException;

import primitives.*;


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
     * Private constructor to prevent direct instantiation.
     */
    private Camera() {
    }

    /**
     * getter of p0
     * @return the location of the camera
     */
    public Point getP0() {
        return p0;
    }

    /**
     * getter of vUp
     * @return the upward vector of the camera
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * getter of vTo
     * @return the forward vector of the camera
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * getter of vRight
     * @return the rightward vector of the camera
     */
    public Vector getVRight() {
        return vRight;
    }

    /**
     * getter of width
     * @return the width of the view plane
     */
    public double getWidth() {
        return width;
    }

    /**
     * getter of height
     * @return the height of the view plane
     */
    public double getHeight() {
        return height;
    }

    /**
     * getter of distance
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
         * @throws IllegalArgumentException if vectors are null, equal, or not perpendicular
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
            
            if (camera.width == 0.0 || camera.height == 0.0) 
                throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, VP_SIZE_FIELD);
            
            if (camera.distance == 0.0) 
                throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, VP_DISTANCE_FIELD);
            
            if (camera.imageWriter == null) 
                throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, IMAGE_WRITER_FIELD);
            
            if (camera.rayTracer==null) 
                throw new MissingResourceException(MISSING_RENDERING_DATA, CAMERA_CLASS_NAME, RAY_TRACER_FIELD);
            
            
         // Validate the values of the fields
            if (camera.width <= 0) 
                throw new IllegalStateException("Width must be positive");
  
            if (camera.height <= 0) 
                throw new IllegalStateException("Height must be positive");
            
            if (camera.distance <= 0) 
                throw new IllegalStateException("Distance must be positive");
            
            if (camera.vTo.equals(camera.vUp)) 
                throw new IllegalArgumentException("Direction vectors cannot be the same");
            
            if (camera.vTo.dotProduct(camera.vUp) != 0)
                throw new IllegalArgumentException("Direction vectors must be perpendicular");
 
            

            // Calculate and set the vRight vector if not already set
            if (camera.vRight == null) {
                camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            }
            // Return a clone of the camera
            return (Camera) camera.clone();
        }

    }
    
    /**
     * Method for rendering image 
     */
    public void renderImage() {
        throw new UnsupportedOperationException("Rendering image is not supported yet");
    }
    
    /**
     * Method for creating grid lines and print grid
     * @param color    The color of the grid lines.
     * @param interval The spacing between grid lines.
     * @throws IllegalArgumentException if the interval is not positive.
     */
    public void printGrid(Color color, int interval) {
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
    }
    
    /**
     * Writes the image data to the image file using the appropriate method from the image writer.
     * This method should be invoked with caution as it directly interacts with the underlying image writer.
     * 
     * @throws IllegalStateException if the image writer is not initialized.
     */
    public void writeToImage() {
        // Check if the image writer is initialized
        if (imageWriter == null) {
            throw new IllegalStateException("Image writer is not initialized");
        }
        
        // Delegate the image writing process to the appropriate method of the image writer
        imageWriter.writeToImage();
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
