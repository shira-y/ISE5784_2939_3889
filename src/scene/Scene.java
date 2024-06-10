package scene;

import primitives.Color;
import lighting.AmbientLight;
import geometries.Geometries;

/**
 * Class representing a 3D scene
 */
public class Scene {

	// Public fields as per the PDS requirement
	/**
	 * The name of the scene. This is a unique identifier for the scene.
	 */
	public String name;

	/**
	 * The background color of the scene. Default value is black.
	 */
	public Color background = Color.BLACK;

	/**
	 * The ambient light of the scene. Default value is no ambient light.
	 */
	public AmbientLight ambientLight = AmbientLight.NONE;

	/**
	 * The geometries present in the scene. This represents all the 3D objects
	 * contained within the scene.
	 */
	public Geometries geometries = new Geometries();

	/**
	 * Constructor that initializes the scene with a name
	 * 
	 * @param name the name of the scene
	 */
	public Scene(String name) {
		this.name = name;

	}

	/**
	 * Sets the background color of the scene
	 * 
	 * @param background the background color
	 * @return the current Scene object
	 */
	public Scene setBackground(Color background) {
		this.background = background;
		return this;
	}

	/**
	 * Sets the ambient light of the scene
	 * 
	 * @param ambientLight the ambient light
	 * @return the current Scene object
	 */
	public Scene setAmbientLight(AmbientLight ambientLight) {
		this.ambientLight = ambientLight;
		return this;
	}

	/**
	 * Sets the geometries of the scene
	 * 
	 * @param geometries the geometries
	 * @return the current Scene object
	 */
	public Scene setGeometries(Geometries geometries) {
		this.geometries = geometries;
		return this;
	}
}
