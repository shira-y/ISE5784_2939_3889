package unittests.renderer;

import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;

/**
 * Test class for creating a scene with a chessboard and pawns.
 */
public class MP1 {

	/**
	 * Test method for rendering a chessboard scene with pawns and various light
	 * sources.
	 */
	@Test
	public void ChessboardTest() {
		Scene scene = new Scene("Chessboard Scene");

		// camera
		Vector towardsBoard = new Vector(1, -0.25, 1).normalize();
		Vector upVector = new Vector(0, 1, 0);
		Vector rightVector = towardsBoard.crossProduct(upVector).normalize();
		Vector trueUpVector = rightVector.crossProduct(towardsBoard).normalize();
//		Camera.Builder camera = Camera.getBuilder().setLocation(new Point(-55, 8, -55))
//				.setDirection(towardsBoard, trueUpVector).setVpDistance(150).setVpSize(80, 80);
		 Camera.Builder camera = Camera.getBuilder().setLocation(new Point(-55, 8, -55))
		            .setDirection(new Vector(1, -0.25, 1).normalize(), new Vector(0, 1, 0)).setVpDistance(150).setVpSize(80, 80);


		Material shinyMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(100);

		// chessboard
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Color squareColor = (i + j) % 2 == 0 ? new Color(139, 69, 19) : new Color(169, 169, 169);
				double height = 0;
				scene.geometries.add(new Triangle(new Point(-40 + i * 10, height, -40 + j * 10),
						new Point(-30 + i * 10, height, -40 + j * 10), new Point(-30 + i * 10, height, -30 + j * 10))
						.setEmission(squareColor).setMaterial(shinyMaterial),
						new Triangle(new Point(-40 + i * 10, height, -40 + j * 10),
								new Point(-30 + i * 10, height, -30 + j * 10),
								new Point(-40 + i * 10, height, -30 + j * 10)).setEmission(squareColor)
								.setMaterial(shinyMaterial));
			}
		}

		// A red standing pawn
		double pawnBaseY = 0.2;
		Point baseCenter = new Point(-35, pawnBaseY, -35);
		scene.geometries.add(new Sphere(baseCenter, 2).setEmission(new Color(255, 0, 0)).setMaterial(shinyMaterial),
				new Sphere(baseCenter.add(new Vector(0, 3, 0)), 1.5).setEmission(new Color(255, 0, 0))
						.setMaterial(shinyMaterial),
				new Sphere(baseCenter.add(new Vector(0, 5, 0)), 1).setEmission(new Color(255, 0, 0))
						.setMaterial(shinyMaterial));

		// A black fallen pawn
		Point fallenBaseCenter = new Point(-45, pawnBaseY, -35);
		scene.geometries.add(
				new Sphere(fallenBaseCenter.add(new Vector(0, 1, 0)), 2).setEmission(new Color(0, 0, 0))
						.setMaterial(shinyMaterial),
				new Sphere(fallenBaseCenter.add(new Vector(0, 1, 0).add(new Vector(2, 0, 0))), 1.5)
						.setEmission(new Color(0, 0, 0)).setMaterial(shinyMaterial),
				new Sphere(fallenBaseCenter.add(new Vector(0, 1, 0).add(new Vector(4, 0, 0))), 1)
						.setEmission(new Color(0, 0, 0)).setMaterial(shinyMaterial));

		 // Ambient light and light sources setup
	    scene.lights.add(new DirectionalLight(new Color(200, 200, 200), new Vector(1, -1, 0)));
	    scene.lights.add(new AreaLight(new Color(50, 50, 50), new Point(-20, 50, -35), 10, 10, new Vector(1, 0, 0), new Vector(0, 0, 1)).setKl(0.00001).setKq(0.000005));
	    scene.lights.add(new SpotLight(new Color(0, 0, 255), new Point(-30, 30, -35), new Vector(1, -1, 0)).setKl(0.00001).setKq(0.000005));

	    ImageWriter imageWriter = new ImageWriter("chessboard", 1000, 1000);
	    Camera camera1 = camera.setImageWriter(imageWriter).setRayTracer(new SimpleRayTracer(scene).setSoftShadow(true)).build();

	    camera1.renderImage();
	    camera1.writeToImage();


//		SoftShadowsRayTracer rayTracer = new SoftShadowsRayTracer(scene);
//
//        // Render without soft shadows (1x1 rays)
//        rayTracer.getSettings().setNumSampleRays(1);
//        ImageWriter imageWriter = new ImageWriter("chessboard_hard_shadows", 1000, 1000);
//        Camera camera1 = camera.setImageWriter(imageWriter).setRayTracer(rayTracer).build();
//        camera1.renderImage();
//        camera1.writeToImage();
//
//        // Render with soft shadows (e.g., 9x9 = 81 rays)
//        rayTracer.getSettings().setNumSampleRays(81);
//        ImageWriter imageWriter2 = new ImageWriter("chessboard_soft_shadows", 1000, 1000);
//        Camera camera2 = camera.setImageWriter(imageWriter2).setRayTracer(rayTracer).build();
//        camera2.renderImage();
//        camera2.writeToImage();

	}

}
