package primitives;

public class Material {
	/**
     * kD = diffusive attenuation coefficient
     * kS = specular attenuation coefficient
     * kT = transparency coefficient
     * kR = reflection coefficient
     */
    public Double3 kD = Double3.ZERO;
    public Double3 kS = Double3.ZERO;
    public int nShininess = 0;
    
    /**
     * Sets diffusive attenuation factor.
     * @param kD double
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }
    /**
     * Sets diffusive attenuation factor.
     * @param kD Double3
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets specular attenuation factor.
     * @param kS double.
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }
    /**
     * Sets specular attenuation factor.
     * @param kS Double3
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the level of shininess.
     * @param nShininess int
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
