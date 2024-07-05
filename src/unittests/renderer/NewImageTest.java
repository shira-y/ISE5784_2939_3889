
package unittests.renderer;


import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Test class for creating an our image with 3 objects and new effects
 * 
 */
public class NewImageTest {

	/** Scene for the test */
	private final Scene scene = new Scene("our scane test");

	/** Camera builder for the test */
	private final Camera.Builder cameraBuilder = Camera.getBuilder()
			.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setRayTracer(new SimpleRayTracer(scene));

	@Test
    public void trianglesTransparentSphere() {
    
       scene.geometries.add(
                            new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                                         new Point(75, 75, -150))
                               .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                            new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                               .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                            new Sphere(new Point(0, 0, -50), 40d)
                               .setEmission(new Color(0, 0, 100)).setMaterial(new Material().setKd(0.2).setKs(0.2)
                               .setShininess(15).setKt(0.6)));

     	
		scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(-1, -1, -4))
		    	.setKl(4E-5).setKq(2E-7));
		scene.lights.add(new PointLight(new Color(0, 0, 800), new Point(-80, 80, 0)).setKl(0.00001).setKq(0.000005));
		Camera camera = cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000).setVpSize(200, 200)
				.setImageWriter(new ImageWriter("our image", 500, 500)).build();

		camera.renderImage();
		camera.writeToImage();
	}
}