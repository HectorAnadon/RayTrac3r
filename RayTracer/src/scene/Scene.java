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

public class Scene {
	
	private static ArrayList<Shape> objects = new ArrayList<Shape>();
	private static ArrayList<Light> lights = new ArrayList<Light>();
	private static Vector3d ew;
	private static Camera c;
	private static double distanceScreen;
	private static Screen s;

	private static final int numPixelX = 400;
	private static final int numPixelY = 400;
	private static double ambientalLightI = 0;
	private static boolean SAVE_IMAGE = false;
	private static final int NUM_REFLECTED = 0;
	private static final int NUM_REFRACTED = 0;
	private static final int NUM_ALIASING = 15;
	private static final boolean ALIASING = false;
	
	
	private static BufferedImage image = new BufferedImage(numPixelX, numPixelY, BufferedImage.TYPE_INT_RGB);
	
	
	public static void main (String[] args) throws IOException {
				
		scene1();

		int progress = 1;
		int currentProgress = 1;
		boolean showProgress = false;
		
		for (int i=numPixelY/2; i>(-numPixelY/2); i--) {
			
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
			File outputfile = new File("image1.jpg");
			ImageIO.write(image, "jpg", outputfile);
		}
		else {
			Render render = new Render(image);
		}
		
	}	
	
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
		t2.setKr(0);
		objects.add(t2);
	}
	
	
	public static void scene2() {
		ew = new Vector3d(-2,0,0);
		c = new Camera(ew, new Vector3d(-1,0,0), 
				new Vector3d(-1,1,0), new Vector3d(0,0,0));
		distanceScreen = -2;
		s = new Screen(c, distanceScreen, numPixelX, numPixelY, 10, 10);
		
		Light light = new Light(new Vector3d(-5,0,0));
		lights.add(light);
		
		Model m = new Model("objects/Pistacho/pistachio.obj", "objects/Pistacho/pistachio_diff2v3.jpg");
		objects.addAll(m.getTriangles());
		
		Sphere sphere = new Sphere(new Vector3d(2,0,0), 5, 1, new Color(0,0,200));
		sphere.setKr(0.9);
//		objects.add(sphere);
		
		Plane p1 = new Plane(new Vector3d(0,0,20), new Vector3d(0,0,-1), 1.0, new Color(255,255,255));
		p1.setKr(0);
		objects.add(p1);
		
		Plane p2 = new Plane(new Vector3d(0,0,-20), new Vector3d(0,0,1), 1.0, new Color(255,255,255));
		p2.setKr(0);
		objects.add(p2);
		
		Plane p3 = new Plane(new Vector3d(0,-20,0), new Vector3d(0,1,0), 1.0, new Color(255,255,255));
		p3.setKr(0);
		objects.add(p3);
		
		Plane p4 = new Plane(new Vector3d(0,20,0), new Vector3d(0,-1,0), 1.0, new Color(255,255,255));
		p4.setKr(0);
		objects.add(p4);

		Plane p5 = new Plane(new Vector3d(5,0,0), new Vector3d(-1,0,0), 1.0, new Color(255,255,255));
		p5.setKr(0);
		objects.add(p5);
		
		
	}

}
