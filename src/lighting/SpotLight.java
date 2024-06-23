package lighting;

import primitives.*;

public class SpotLight extends PointLight {
	
	 private final Vector direction;
	 /**
     *
     * @param intensity
     * @param position
     * @param direction
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }
  
//	    /**
//	     * Set constant attenuation
//	     * @param d
//	     * @return
//	     */
//	    public SpotLight setKc(double d) {
//	    	PointLight.setKc(d);
//	        return this;
//	    }
//
//	    /**
//	     *set linear attenuation factor
//	     * @param d
//	     * @return
//	     */
//	    public SpotLight setKl(double d) {
//	        this.kL = d;
//	        return this;
//	    }
//
//	    /***
//	     * set quadratic attenuation factor
//	     * @param d
//	     * @return
//	     */
//	    public SpotLight setKq(double d) {
//	        this.kQ = d;
//	        return this;
//	    }
}
