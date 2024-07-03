package primitives;

/**
 * The Material class represents the material properties of a geometry in a
 * scene, including diffusive and specular attenuation coefficients, and
 * shininess level.
 */
public class Material {
	/**
	 * Diffusive attenuation coefficient.
	 */
	public Double3 kD = Double3.ZERO;

	/**
	 * Specular attenuation coefficient.
	 */
	public Double3 kS = Double3.ZERO;

	/**
	 * Shininess level.
	 */
	public int nShininess = 0;
	public Double3 kT = Double3.ZERO;
	public Double3 kR = Double3.ZERO;

	public Material setKt(double kT) {
		this.kT = new Double3(kT);
		return this;
	}

	public Material setKr(double kR) {
		this.kR = new Double3(kR);
		return this;
	}

	public Material setKt(Double3 kT) {
		this.kT = kT;
		return this;
	}

	public Material setKr(Double3 kR) {
		this.kR = kR;
		return this;
	}

	/**
	 * Sets the diffusive attenuation factor.
	 * 
	 * @param kD The diffusive attenuation coefficient as a double.
	 * @return The Material object itself.
	 */
	public Material setKd(double kD) {
		this.kD = new Double3(kD);
		return this;
	}

	/**
	 * Sets the diffusive attenuation factor.
	 * 
	 * @param kD The diffusive attenuation coefficient as a Double3 object.
	 * @return The Material object itself.
	 */
	public Material setKd(Double3 kD) {
		this.kD = kD;
		return this;
	}

	/**
	 * Sets the specular attenuation factor.
	 * 
	 * @param kS The specular attenuation coefficient as a double.
	 * @return The Material object itself.
	 */
	public Material setKs(double kS) {
		this.kS = new Double3(kS);
		return this;
	}

	/**
	 * Sets the specular attenuation factor.
	 * 
	 * @param kS The specular attenuation coefficient as a Double3 object.
	 * @return The Material object itself.
	 */
	public Material setKs(Double3 kS) {
		this.kS = kS;
		return this;
	}

	/**
	 * Sets the shininess level of the material.
	 * 
	 * @param nShininess The shininess level as an integer.
	 * @return The Material object itself.
	 */
	public Material setShininess(int nShininess) {
		this.nShininess = nShininess;
		return this;
	}
}
