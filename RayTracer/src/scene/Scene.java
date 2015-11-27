package scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

	private static final int numPixelX = 700;
	private static final int numPixelY = 700;
	private static double ambientalLightI = 0;
	
	private static final int NUM_REFLECTED = 1;
	private static final int NUM_ALIASING = 15;
	private static final boolean ALIASING = true;
	
	
	private static BufferedImage image = new BufferedImage(numPixelX, numPixelY, BufferedImage.TYPE_INT_RGB);
	
	
	public static void main (String[] args) {
		
		ew = new Vector3d(0,0,0);
		Camera c = new Camera(ew, new Vector3d(-1,0,0), 
				new Vector3d(-1,1,0), new Vector3d(0,0,0));
		
		//Light light = new Light(new Vector3d(0,0,10), new Vector3d(0, 0 , 2));
		Light light = new Light(new Vector3d(0,0,0), new Vector3d(2, 0 , 0));
		//objects.add(new Plane(new Vector3d(-9,0,5), new Vector3d(-9,0,5), 1.0));
		lights.add(light);
		
		double distanceScreen = -3;
		System.out.println("Screen distance: " + distanceScreen + "\n\n");
		Screen s = new Screen(c, distanceScreen, numPixelX, numPixelY, 10, 10);


//		objects.add(new Triangle(new Vector3d(13,-5,5), new Vector3d(10,-5,-5), new Vector3d(10,0,0), 1.0, new Color(255,255,255)));

//		objects.add(new Plane(new Vector3d(0,-5,0), new Vector3d(0,1,0), 1.0, new Color(0,200,0)));
//		objects.add(new Plane(new Vector3d(0,10,0), new Vector3d(0,-1,0), 1.0, new Color(0,200,100)));
//		objects.add(new Plane(new Vector3d(0,-10,0), new Vector3d(0,1,0), 1.0, new Color(0,0,255)));
//		objects.add(new Plane(new Vector3d(0,0,-20), new Vector3d(0,0,1), 1.0, new Color(100,100,200)));
		Plane p = new Plane(new Vector3d(0,0,20), new Vector3d(0,0,-1), 1.0, new Color(255,255,255));
		p.setKr(0.8);
		objects.add(p);
		
		Plane p2 = new Plane(new Vector3d(0,-10,0), new Vector3d(0,1,0), 1.0, new Color(0,0,255));
		p2.setKr(0);
		objects.add(p2);
		
		Plane p3 = new Plane(new Vector3d(50,0,0), new Vector3d(-1,0,0), 1.0, new Color(255,0,0));
		p3.setKr(0);
		objects.add(p3);
		
		Plane p4 = new Plane(new Vector3d(0,0,-20), new Vector3d(0,0,1), 1.0, new Color(255,0,0));
		p4.setKr(0);
		objects.add(p4);
		
		objects.add(new Sphere(new Vector3d(10,0,0), 5, 1, new Color(0,200,200)));
		
//		Sphere a = new Sphere(new Vector3d(-3,0,5), 3, 1, new Color(200,0,0));
//		Sphere b = new Sphere(new Vector3d(-7,0,5), 0.4, 1, new Color(200,200,0));
//		objects.add(a);
//		objects.add(b);
		
//		Model m = new Model("objects/Pistacho/pistachio.obj", "objects/Pistacho/pistachio_diff2v3.jpg");
//		objects.addAll(m.getTriangles());
//		objects.add(new Model("objects/Pistacho/pistachio.obj"));

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
					Ray r = new Ray(c.getEw(), new Vector3d(v.x,v.y,v.z));
					
					Color currentColor = traceRay(r, NUM_REFLECTED);
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
		
		Render render = new Render(image);
		
	}


	private static Color normalizeColor(Color imgColor, Color provColor) {
		int r = imgColor.getRed() + provColor.getRed();
		int g = imgColor.getGreen() + provColor.getGreen();
		int b = imgColor.getBlue() + provColor.getBlue();
		int max = Math.max(r, Math.max(g, b));
		if (max > 255) {
			r = (255*r)/max;
			g = (255*g)/max;
			b = (255*b)/max;
		}
		return new Color(r,g,b);
	}
	
	
	public static Color traceRay(Ray r, int raysReaming) {
		Shape object = null;
		double minDistance = Double.POSITIVE_INFINITY;
		Ray rReflected = null;
		
		Color imgColor = null;
		Color reflectedColor = null;
		Color refractedColor = null;
		
		for (Shape obj:objects) {	// Intersect ray with each objects
			Ray currentReflected = obj.intersection(r);
			
			if (currentReflected != null) {
				double distance = Util.distance(ew, currentReflected.position);
				 if (distance < minDistance) {
					 object = obj;
					 minDistance = distance; // update min distance
					 rReflected = currentReflected;
				 }
				//System.out.println(i + "  -  " + j);
				// TO DO:
					// 1. Calculate intersection between light and intersection point
					// 2. Calculate intersection between other objects with the reflected ray
			}
		}
				
		//Only for one object:
		// AMBIANTAL LIGHT + DIFUSSE
		if (object != null) {
			//get ambiental light
			imgColor = object.getColor(ambientalLightI);
			for (Light l:lights) {
				//Ray from object to light
				Ray rLight = new Ray(rReflected.position, l.getPosition());
				boolean intersects = false;
				for (Shape obj2:objects) {
					if (!obj2.equals(object)){
						//Ray from object in the middle
						Ray rReflected2 = obj2.intersection(rLight);
						//sometimes intersections it shouldnt
						if (rReflected2 != null && 
								(Util.distance(rReflected2.position, l.getPosition()))
								< Util.distance(rReflected.position, l.getPosition())) {
							/*System.out.println("position object: " + rReflected.position);
							System.out.println("position in the middle:" + rReflected2.position);
							System.out.println("position light: "+l.getPosition());
							*/
								intersects = true;								
						}
					}
				}
				if(!intersects) {		// Calculate color without shadow
					Color difusa = object.getColor(l.getIntensity(),rLight);
					imgColor = normalizeColor(imgColor, difusa);
					//TODO: error with rLightReflected with plane. I think normal should be inverse only for these case
					Ray rLightReflected = object.intersection(rLight);
					if (rLightReflected != null) {
						Color especular = object.getColor(l.getIntensity(),rLight,rLightReflected,r);
						imgColor = normalizeColor(imgColor, especular);
						//Test especular
						//imgColor =object.getColor(l.getIntensity(),rLight,rLightReflected,r);
					} else{
						//Test especular
						//imgColor = new Color(0,0,0);
					}
					//Test difuso
					//imgColor =object.getColor(l.getIntensity(),rLight);
				}
			}		// End of lights
			
			
			// Color reflected
			if (raysReaming > 0) {
//				reflectedColor = traceRay(rReflected, raysReaming - 1);
				reflectedColor = traceRay(new Ray(rReflected.position, Util.inverse(rReflected.direction)), raysReaming - 1);
				if (reflectedColor != null) {
					reflectedColor = new Color((int) (object.kr*reflectedColor.getRed()), 
							(int) (object.kr*reflectedColor.getGreen()), (int) (object.kr*reflectedColor.getBlue()));
					imgColor = normalizeColor(imgColor, reflectedColor);
				}
				else {
					raysReaming = 0;
				}
			}
		
			
		}	// End of object != null
		
		return imgColor;

	}
}
