package scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import objects.Plane;
import objects.Shape;
import objects.Sphere;
import objects.Triangle;

public class Scene {
	
	static ArrayList<Shape> objects = new ArrayList<Shape>();
	
	private static final int numPixelX = 500;
	private static final int numPixelY = 500;
	private static double ambientalLightI = 0.5;
	
	
	private static BufferedImage image = new BufferedImage(numPixelX, numPixelY, BufferedImage.TYPE_INT_RGB);
	
	
	public static void main (String[] args) {
		
		Vector3d ew = new Vector3d(-5,0,10);
		Camera c = new Camera(ew, new Vector3d(-1, 0 , 2), 
				new Vector3d(0,1,1), new Vector3d(0,0,0));
		
		//Light light = new Light(new Vector3d(0,0,10), new Vector3d(0, 0 , 2));
		Light light = new Light(new Vector3d(-10,0,5), new Vector3d(2, 0 , 0));
		//objects.add(new Plane(new Vector3d(-9,0,5), new Vector3d(-9,0,5), 1.0));
		Light[] lights = {light};
		
		double distanceScreen = -3;
		System.out.println("Screen distance: " + distanceScreen + "\n\n");
		Screen s = new Screen(c, distanceScreen, numPixelX, numPixelY, 10, 10);


		
		/*System.out.println("Triangle distance: " + 5 + "\n\n");
		
		objects.add(new Triangle(new Vector3d(-1,0,5), new Vector3d(0,1,5), new Vector3d(1,0,5), 1.0));
		objects.add(new Triangle(new Vector3d(-5,0,5), new Vector3d(-2,1,5), new Vector3d(-1,0,5), 1.0));
		//System.out.println("Sphere distance: " + 4 + "\n\n");
		objects.add(new Sphere(new Vector3d(0,3,5), 1, 1));*/
		objects.add(new Plane(new Vector3d(0,0,-20), new Vector3d(15,0,-20), 1.0));
		objects.add(new Sphere(new Vector3d(-3,0,5), 3, 1));
		objects.add(new Sphere(new Vector3d(-7,0,5), 0.4, 1));

		
		for (int i=numPixelY/2; i>(-numPixelY/2); i--) {
			for (int j=numPixelY/2; j>(-numPixelY/2); j--) {
				ArrayList<Vector4d> points = s.getWorldScreenCoordinates(i, j);
				for (Vector4d v:points) {	// Intersect ray with each objects
					
					//Ray from eye to pixel
					Ray r = new Ray(c.getEw(), new Vector3d(v.x,v.y,v.z));
					
					Shape object = null;
					double minDistance = Double.POSITIVE_INFINITY;
					for (Shape obj:objects) {
						Ray rReflected = obj.intersection(r);
						
						if (rReflected != null) {
							double distance = Util.distance(ew, rReflected.position);
							 if (distance < minDistance) {
								 object = obj;
								 minDistance = distance; // update min distance
							 }
							//System.out.println(i + "  -  " + j);
							// TO DO:
								// 1. Calculate intersection between light and intersection point
								// 2. Calculate intersection between other objects with the reflected ray
						}
					}
							
							
					//Only for one object:
					if (object != null) {
						//Ray reflected from object
						Ray rReflected = object.intersection(r);
						//get ambiental light
						Color imgColor = object.getColor(ambientalLightI);
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
											Util.distance(rReflected2.position, l.getPosition())
											< Util.distance(rReflected.position, l.getPosition())) {
										/*System.out.println("position object: " + rReflected.position);
										System.out.println("position in the middle:" + rReflected2.position);
										System.out.println("position light: "+l.getPosition());
										*/
										intersects = true;
									}
								}
							}
							if(!intersects) {
								Color provColor = object.getColor(l.getIntensity(),rLight);
								imgColor = normalizeColor(imgColor, provColor);
							}
						}
						image.setRGB(-i+numPixelX/2,numPixelY/2-j, imgColor.getRGB());
					}
				}
			}
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
}
