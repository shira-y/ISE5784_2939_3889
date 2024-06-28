package scene;

import primitives.Color;
import lighting.*;

import java.util.LinkedList;
import java.util.List;

import geometries.Geometries;

/**
 * Class representing a 3D scene
 */
public class Scene {

	// Public fields as per the PDS requirement
	/**
	 * The name of the scene. This is a unique identifier for the scene.
	 */
	public final String name;

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
     * List of light sources in the scene.
     */
	public List<LightSource> lights = new LinkedList<>();

	/**
	 * Constructor that initializes the scene with a name
	 * 
	 * @param name the name of the scene
	 */
	public Scene(String name) {
		this.name = name;

	}

	/**
	 * Sets the lights of the scene
	 * 
	 * @param lights - the lights
	 * @return the current Scene object
	 */
	public Scene setLights(List<LightSource> lights) {
		this.lights = lights;
		return this;
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
