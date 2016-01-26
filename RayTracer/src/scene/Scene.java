package scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import objects.Model;
import objects.Plane;
import objects.Shape;
import objects.Sphere;
import objects.Triangle;

/**
 * Scene object
 *
 */
public class Scene {
	
	private static ArrayList<Shape> objects = new ArrayList<Shape>();
	private static ArrayList<Light> lights = new ArrayList<Light>();
	private static Vector3d ew;
	private static Camera c;
	private static double distanceScreen;
	private static Screen s;

	private static int numPixelX = 1024;
	private static int numPixelY = 512;
	private static double ambientalLightI = 0.05;
	private static final int NUM_REFLECTED = 5;
	private static final int NUM_REFRACTED = 3;
	private static final int NUM_ALIASING = 15;
	private static final boolean ALIASING = true;
	
	private static boolean SAVE_IMAGE = true;
	private static final String NAME_IMAGE = "buenaGrande.jpg";
	
	
	private static BufferedImage image = new BufferedImage(numPixelX, numPixelY, BufferedImage.TYPE_INT_RGB);
	
	/**
	 * Start the ray tracer
	 * @param args
	 * @throws IOException
	 */
	public static void main (String[] args) throws IOException {
				
		scene4();

		int progress = 1;
		int currentProgress = 1;
		boolean showProgress = false;
		
		for (int i=numPixelX/2; i>(-numPixelX/2); i--) {
			
			// Shows the render progress
			if (showProgress) {
				System.out.println((currentProgress + 1) + " %");
				showProgress = false;
			}
			if (((int) progress*100/numPixelX) != currentProgress) {
				currentProgress = progress*100/numPixelX;
				showProgress = true;
			}
			
			
			for (int j=numPixelY/2; j>(-numPixelY/2); j--) {
				//ArrayList<Vector4d> points = s.getWorldScreenCoordinates(i, j);
				
				ArrayList<Vector4d> points;
				if (ALIASING) {
					points = s.getWorldScreenCoordinatesAntiAliasing(i, j, NUM_ALIASING);
				} else {
					points = s.getWorldScreenCoordinates(i, j);	
				}
				
				int red = 0;
				int green = 0;
				int blue = 0;
				
				for (Vector4d v:points) { // For each points in the pixel (AntiAliasing)	
					
					//Ray from eye to pixel
					Ray r = new Ray(c.getEw(), new Vector3d(v.x,v.y,v.z),1);
					
					ArrayList<Shape> toIgnore = new ArrayList<Shape>();
					Color currentColor = RayTracer.traceRay(r,objects, lights, ambientalLightI, ew,
							NUM_REFLECTED, NUM_REFRACTED, NUM_REFLECTED, toIgnore);
					if (currentColor != null) {
						red += currentColor.getRed();
						green += currentColor.getGreen();
						blue += currentColor.getBlue();
					}
					
					
				}
				if (ALIASING) {
					image.setRGB(-i+numPixelX/2,numPixelY/2-j, new Color(red/NUM_ALIASING,green/NUM_ALIASING,blue/NUM_ALIASING).getRGB());
				} else {
					image.setRGB(-i+numPixelX/2,numPixelY/2-j, new Color(red,green,blue).getRGB());	
				}
			}
			
			progress ++;
		}
		
		if (SAVE_IMAGE) {
			File outputfile = new File(NAME_IMAGE);
			ImageIO.write(image, "jpg", outputfile);
			Render render = new Render(image);
		}
		else {
			Render render = new Render(image);
		}
		
	}	
	
	/**
	 * Triangle refraction + reflexion
	 */
	public static void scene1() {
		ew = new Vector3d(-4,0,0);
		c = new Camera(ew, new Vector3d(-1,0,0), 
				new Vector3d(-1,1,0), new Vector3d(0,0,0));
		distanceScreen = -3;
		s = new Screen(c, distanceScreen, numPixelX, numPixelY, 10, 10);
		
		Light light = new Light(new Vector3d(0,-2,0));
		lights.add(light);

		Plane p = new Plane(new Vector3d(0,0,20), new Vector3d(0,0,-1), 1.0, new Color(255,255,255));
		p.setKr(0.8);
		objects.add(p);
		
		Plane p2 = new Plane(new Vector3d(0,-10,0), new Vector3d(0,1,0), 1.0, new Color(0,0,255));
		p2.setKr(0);
//				objects.add(p2);
		
		Plane p3 = new Plane(new Vector3d(50,0,0), new Vector3d(-1,0,0), 1.0, new Color(255,150,0));
		p3.setKr(0);
		p3.setOpaque(1);
		objects.add(p3);
		
		Plane p4 = new Plane(new Vector3d(0,0,-20), new Vector3d(0,0,1), 1.0, new Color(255,0,0));
		p4.setKr(0);
		objects.add(p4);

		Sphere sphere = new Sphere(new Vector3d(0,0,0), 5, 1, new Color(0,200,200));
		sphere.transformation(new Matrix4d(1,0,0,0,
				0,1,0,0,
				0,0,1,0,
				7,0,0,1));
		sphere.setKr(0);
		sphere.setOpaque(0.3);
		sphere.setKn(1.3);
		objects.add(sphere);
		
		Sphere sphere2 = new Sphere(new Vector3d(7,5,0), 5, 1, new Color(0,255,0));
		sphere2.setKr(0.9);
		objects.add(sphere2);
		
		Triangle t2= new Triangle(new Vector3d(13,-20,-20), new Vector3d(10,-20,20), new Vector3d(60,0,0), 1.0, new Color(0,0,255));
		t2.setOpaque(0.2);
		t2.setKn(1);
		t2.setKr(0.5);
		objects.add(t2);
	}
	
	/**
	 * Pistachio scene
	 */
	public static void scene2() {
		ew = new Vector3d(-3.5,1.3,0);
		c = new Camera(ew, new Vector3d(-1,0,0), 
				new Vector3d(-1,1,0), new Vector3d(0,0,0));
		distanceScreen = -2;
		s = new Screen(c, distanceScreen, numPixelX, numPixelY, 10, 10);
		
		Light light = new Light(new Vector3d(-5,0,0));
		lights.add(light);
		
		double angle = 250;
		Matrix4d mat = new Matrix4d(Math.cos(angle),0,-Math.sin(angle),0,
									0,1,0,0,
									Math.sin(angle),0,Math.cos(angle),0,
									0,0,0,0.6);
		Model m = new Model("objects/Pistacho/pistachio.obj", "objects/Pistacho/pistachio_diff2v3.jpg", mat);
		objects.addAll(m.createTriangles());
		
		Sphere sphere = new Sphere(new Vector3d(2,0,0), 5, 1, new Color(0,0,200));
		sphere.setKr(0.9);
//		objects.add(sphere);
		
		Plane p1 = new Plane(new Vector3d(0,-3.7,0), new Vector3d(-1,2,1), 1.0, new Color(255,255,255));
		p1.setKr(0.9);
		objects.add(p1);
		
		Plane p2 = new Plane(new Vector3d(0,-3.7,0), new Vector3d(-1,2,-1), 1.0, new Color(255,0,255));
		p2.setKr(0.9);
		objects.add(p2);
		
		Plane p3 = new Plane(new Vector3d(0,6.3,0), new Vector3d(-1,-2,1), 1.0, new Color(0,255,255));
		p3.setKr(0.9);
		objects.add(p3);
		
		Plane p4 = new Plane(new Vector3d(0,6.3,0), new Vector3d(-1,-2,-1), 1.0, new Color(255,255,0));
		p4.setKr(0.9);
		objects.add(p4);

		Plane p5 = new Plane(new Vector3d(5,0,0), new Vector3d(-1,0,0), 1.0, new Color(255,255,255));
		p5.setKr(0);
//		objects.add(p5);
		
		
	}
	
	/**
	 * Dinosour scene
	 */
	public static void scene3() {
		ew = new Vector3d(-230,-5,50);
		c = new Camera(ew, new Vector3d(-1,-0.0,-0.0), 
				new Vector3d(-1,0.5,0), new Vector3d(0,0,0));
		distanceScreen = -20;
		s = new Screen(c, distanceScreen, numPixelX, numPixelY, 10, 10);
		
		Light light = new Light(new Vector3d(-200,5,40));
		lights.add(light);
		
		Matrix4d mat = new Matrix4d(0.906,0,-0.422,0,
									0,1,0,0,
									0.422,0,0.906,0,
									0,0,0,1);
		Model m = new Model("objects/dinosaur_OBJ/dinosaur.obj", "", mat);
		m.setKr(0.2);
		objects.addAll(m.createTriangles());
		
		Plane p1 = new Plane(new Vector3d(0,-21.07,0), new Vector3d(0,1,0), 1.0, new Color(0,200,0));
		p1.setKr(0.2);
		objects.add(p1);

		
	}

	/**
	 * Exam scene
	 */
	public static void scene4() {
		
		ew = new Vector3d(40,10,30);
		c = new Camera(ew, new Vector3d(1,0,1), 
				new Vector3d(1,0.5,1), new Vector3d(0,0,0));
		distanceScreen = -5;
		s = new Screen(c, distanceScreen, numPixelX, numPixelY, 20, 10);
		
		Light light = new Light(new Vector3d(80,40,10));
		Light light2 = new Light(new Vector3d(40,40,80));
		light.setIntensity(0.9);
		light2.setIntensity(0.4);
		lights.add(light);
		lights.add(light2);

		// XZ	-> Yellow
		Plane p1 = new Plane(new Vector3d(0,0,0), new Vector3d(0,0.884769,0), 1.0, new Color(255,255,0));
		objects.add(p1);
		
		// XY	-> Pink
		Plane p2 = new Plane(new Vector3d(0,0,0), new Vector3d(0,0,1), 1.0, new Color(255,0,255));
		objects.add(p2);
		
		// YZ	-> Blue
		Plane p3 = new Plane(new Vector3d(0,0,0), new Vector3d(1,0,0), 1.0, new Color(0,255,255));
		objects.add(p3);
		

		Sphere sphere2 = new Sphere(new Vector3d(18,15,8), 14, 1, new Color(0,255,0));
		sphere2.setKr(1);
		objects.add(sphere2);

		Sphere sphere3 = new Sphere(new Vector3d(30,4,8), 8, 1, new Color(0,255,0));
		objects.add(sphere3);
		
		Sphere sphere4 = new Sphere(new Vector3d(15,4,18), 8, 1, new Color(255,0,0));
		sphere4.setKs(0);
		objects.add(sphere4);

		Sphere sphere5 = new Sphere(new Vector3d(23,4,18), 8, 1, new Color(0,255,0));
		sphere5.setKn(1.3);
		sphere5.setOpaque(0.5);
		objects.add(sphere5);
		
		
		Triangle t1 = new Triangle(new Vector3d(2,20,25),new Vector3d(25,20,2),new Vector3d(10,22,30),1, new Color(200,0,0));
		Triangle t2 = new Triangle(new Vector3d(10,22,30),new Vector3d(30,22,10),new Vector3d(25,20,2),1, new Color(200,0,0));
		t1.setKr(1);
		t2.setKr(1);
		objects.add(t1);
		objects.add(t2);
		
		
		double angle = 250;
		Matrix4d mat = new Matrix4d(Math.cos(angle),0,-Math.sin(angle),0,
									0,1,0,0,
									Math.sin(angle),0,Math.cos(angle),0,
									2,0,4.8,0.2);
		Model m = new Model("objects/Pistacho/pistachio.obj", "objects/Pistacho/pistachio_diff2v3.jpg", mat);
		objects.addAll(m.createTriangles());
	}
	
}
