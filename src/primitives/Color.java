package primitives;

/**
 * Wrapper class for java.jwt.Color The constructors operate with any
 * non-negative RGB values. The colors are maintained without upper limit of
 * 255. Some additional operations are added that are useful for manipulating
 * light's colors
 * 
 * @author Dan Zilberstein
 */
public class Color {
	/**
	 * The internal fields maintain RGB components as double numbers from 0 to
	 * whatever...
	 */
	private final Double3 rgb;

	/** Black color = (0,0,0) */
	public static final Color BLACK = new Color();

	/** Default constructor - to generate Black Color (privately) */
	private Color() {
		rgb = Double3.ZERO;
	}

	/**
	 * Constructor to generate a color according to RGB components Each component in
	 * range 0..255 (for printed white color) or more [for lights]
	 * 
	 * @param r Red component
	 * @param g Green component
	 * @param b Blue component
	 */
	public Color(double r, double g, double b) {
		if (r < 0 || g < 0 || b < 0)
			throw new IllegalArgumentException("Negative color component is illegal");
		rgb = new Double3(r, g, b);
	}

	/**
	 * Constructor to generate a color according to RGB components Each component in
	 * range 0..255 (for printed white color) or more [for lights]
	 * 
	 * @param rgb triad of Red/Green/Blue components
	 */
	private Color(Double3 rgb) {
		if (rgb.d1 < 0 || rgb.d2 < 0 || rgb.d3 < 0)
			throw new IllegalArgumentException("Negative color component is illegal");
		this.rgb = rgb;
	}

	/**
	 * Constructor on base of java.awt.Color object
	 * 
	 * @param other java.awt.Color's source object
	 */
	public Color(java.awt.Color other) {
		rgb = new Double3(other.getRed(), other.getGreen(), other.getBlue());
	}

	/**
	 * Color getter - returns the color after converting it into java.awt.Color
	 * object During the conversion any component bigger than 255 is set to 255
	 * 
	 * @return java.awt.Color object based on this Color RGB components
	 */
	public java.awt.Color getColor() {
		int ir = (int) rgb.d1;
		int ig = (int) rgb.d2;
		int ib = (int) rgb.d3;
		return new java.awt.Color(ir > 255 ? 255 : ir, ig > 255 ? 255 : ig, ib > 255 ? 255 : ib);
	}

	/**
	 * Operation of adding this and one or more other colors (by component)
	 * 
	 * @param colors one or more other colors to add
	 * @return new Color object which is a result of the operation
	 */
	public Color add(Color... colors) {
		double rr = rgb.d1;
		double rg = rgb.d2;
		double rb = rgb.d3;
		for (Color c : colors) {
			rr += c.rgb.d1;
			rg += c.rgb.d2;
			rb += c.rgb.d3;
		}
		return new Color(rr, rg, rb);
	}

	/**
	 * Scale the color by a scalar triad per rgb
	 * 
	 * @param k scale factor per rgb
	 * @return new Color object which is the result of the operation
	 */
	public Color scale(Double3 k) {
		if (k.d1 < 0.0 || k.d2 < 0.0 || k.d3 < 0.0)
			throw new IllegalArgumentException("Can't scale a color by a negative number");
		return new Color(rgb.product(k));
	}

	/**
	 * Scale the color by a scalar
	 * 
	 * @param k scale factor
	 * @return new Color object which is the result of the operation
	 */
	public Color scale(double k) {
		if (k < 0.0)
			throw new IllegalArgumentException("Can't scale a color by a negative number");
		return new Color(rgb.scale(k));
	}

	/**
	 * Scale the color by (1 / reduction factor)
	 * 
	 * @param k reduction factor
	 * @return new Color object which is the result of the operation
	 */
	public Color reduce(int k) {
		if (k < 1)
			throw new IllegalArgumentException("Can't scale a color by a by a number lower than 1");
		return new Color(rgb.reduce(k));
	}
    /**
     * This function compares colors and returns a boolean variable whether they are equal or not
     * @param color the color to compare
     * @return true if the colors equals, otherwise false
     */
    public boolean isColorsEqual(Color color) {
        
        Point p1 = new Point(rgb);
        Point p2 = new Point(color.rgb);
        double c1 = Math.sqrt((this.rgb.d1) * (this.rgb.d1) + (this.rgb.d2) * (this.rgb.d2) + (this.rgb.d3) * (this.rgb.d3));
        double c2 = Math.sqrt((color.rgb.d1) * (color.rgb.d1) + (color.rgb.d2) * (color.rgb.d2) + (color.rgb.d3) * (color.rgb.d3));
        double avg = (c1 + c2) / 2d;
        return !(p1.distance(p2) > 0.25 * avg);
    }
    public boolean almostEquals(Color other) {
        java.awt.Color thisColor = this.getColor();
        java.awt.Color otherColor = other.getColor();
        if ((thisColor.getBlue() + 10 == otherColor.getBlue() || thisColor.getBlue() == otherColor.getBlue() + 10) &&
                (thisColor.getRed() + 10 == otherColor.getRed() || thisColor.getRed() == otherColor.getRed() + 10) &&
                (thisColor.getGreen() + 10 == otherColor.getGreen() || thisColor.getGreen() == otherColor.getGreen() + 10)) {
            return true;
        }
        return false;
    }
	@Override
	public String toString() {
		return "rgb:" + rgb;
	}
}
