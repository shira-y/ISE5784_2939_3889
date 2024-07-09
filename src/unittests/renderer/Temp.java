package unittests.renderer;

import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;

public class Temp {

	@Test
	public void TempTest() {
		Scene scene = new Scene("temp Scene");

//		Camera.Builder camera = Camera.getBuilder().setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//				.setLocation(new Point(0, 50, 350)).setVpDistance(500).setVpSize(200, 200);
	   Camera.Builder camera = Camera.getBuilder().setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
				.setLocation(new Point(0, 0, 100)).setVpDistance(1000).setVpSize(200, 200)
				.setRayTracer(new SimpleRayTracer(scene));

	// Define the points
       Point p1 = new Point(0, 3, 0);    // Top point
       Point p2 = new Point(-2, 2, 0);   // Upper left
       Point p3 = new Point(2, 2, 0);    // Upper right
       Point p4 = new Point(-3, 0, 0);   // Middle left
       Point p5 = new Point(3, 0, 0);    // Middle right
       Point p6 = new Point(-1, 1, 0);   // Upper middle left
       Point p7 = new Point(1, 1, 0);    // Upper middle right
       Point p8 = new Point(0, -3, 0);   // Bottom point
       Point p9 = new Point(-2, 0, 0);   // Middle left inner
       Point p10 = new Point(2, 0, 0);   // Middle right inner
       Point p11 = new Point(-1, -1, 0); // Lower middle left
       Point p12 = new Point(1, -1, 0);  // Lower middle right


//       // Add the triangles to the scene
//       scene.geometries.add(new Triangle(p1, p2, p6).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7)));  // Upper left triangle
//       scene.geometries.add(new Triangle(p1, p6, p7).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7)));  // Upper middle triangle
//       scene.geometries.add(new Triangle(p1, p7, p3).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7)));  // Upper right triangle
//       scene.geometries.add(new Triangle(p2, p4, p9).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7)));  // Left upper triangle
//       scene.geometries.add(new Triangle(p2, p9, p6).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7)));  // Left middle triangle
//       scene.geometries.add(new Triangle(p3, p7, p10).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7))); // Right upper triangle
//       scene.geometries.add(new Triangle(p3, p10, p5).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7))); // Right middle triangle
//       scene.geometries.add(new Triangle(p4, p9, p11).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7))); // Left lower triangle
//       scene.geometries.add(new Triangle(p9, p11, p8).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7))); // Left bottom triangle
//       scene.geometries.add(new Triangle(p9, p8, p10).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7))); // Bottom middle triangle
//       scene.geometries.add(new Triangle(p10, p12, p8).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7))); // Right bottom triangle
//       scene.geometries.add(new Triangle(p10, p5, p12).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7))); // Right lower triangle
//       scene.geometries.add(new Triangle(p6, p9, p7).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7)));  // Middle inner left triangle
//       scene.geometries.add(new Triangle(p7, p9, p10).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7))); // Middle inner right triangle


		
		  // Add the triangles to the scene
       scene.geometries.add(new Triangle(p1, p2, p6).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setKt(0.6)));
       scene.geometries.add(new Triangle(p1, p6, p7).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKd(0.2).setKs(0.8).setKt(0.6)));
       scene.geometries.add(new Triangle(p1, p7, p3).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7)));
       scene.geometries.add(new Triangle(p2, p4, p6).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKd(0.2).setKs(0.8).setKt(0.6)));
       scene.geometries.add(new Triangle(p6, p4, p8).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.7)));
       scene.geometries.add(new Triangle(p6, p8, p7).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKd(0.2).setKs(0.8).setKt(0.6)));
       scene.geometries.add(new Triangle(p7, p8, p5).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.8)));
       scene.geometries.add(new Triangle(p7, p5, p3).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setShininess(60).setKt(0.6)));
       scene.geometries.add(new Triangle(p2, p3, p4).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setKt(0.6)));
       scene.geometries.add(new Triangle(p3, p5, p4).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setKt(0.6)));
       scene.geometries.add(new Triangle(p4, p8, p5).setEmission(new Color(200, 200, 255)).setMaterial(new Material().setKs(0.8).setKt(0.7)));

       // Add lights to the scene
       scene.lights.add(new SpotLight(new Color(1000, 600, 600), new Point(100, 100, 100), new Vector(-1, -1, -2))
               .setKl(0.0004).setKq(0.0000006));
//       scene.lights.add(new PointLight(new Color(500, 300, 300), new Point(-100, 100, 100))
//               .setKl(0.0004).setKq(0.0000006));
//       scene.lights.add(new DirectionalLight(new Color(300, 300, 300), new Vector(1, -1, -1)));


//        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
//		scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point(40, 40, 115), new Vector(-1, -1, -4)) //
//				.setKl(4E-4).setKq(2E-5));
       

		Camera camera2 = camera.setImageWriter(new ImageWriter("dimond", 600, 600)).build();
		camera2.renderImage();
		camera2.writeToImage();
		
	}
}
