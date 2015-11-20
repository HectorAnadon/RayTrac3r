package scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import objects.Plane;
import objects.Shape;
import objects.Triangle;

public class Scene {
	
	static ArrayList<Shape> objects = new ArrayList<Shape>();
	
	private static final int numPixelX = 200;
	private static final int numPixelY = 200;
	
	private static BufferedImage image = new BufferedImage(numPixelX, numPixelY, BufferedImage.TYPE_INT_RGB);
	
	
	public static void main (String[] args) {
		Camera c = new Camera(new Vector3d(0,0,30), new Vector3d(0, 0 , 2), 
				new Vector3d(0,1,1), new Vector3d(0,0,0));
		
		Screen s = new Screen(c, 20, numPixelX, numPixelY, 20, 20);

		objects.add(new Plane(new Vector3d(0,0,0), new Vector3d(0,0,1), 1.0));
		objects.add(new Triangle(new Vector3d(-1,0,5), new Vector3d(0,1,5), new Vector3d(1,0,5), 1.0));
		objects.add(new Triangle(new Vector3d(-2,0,5), new Vector3d(-2,1,5), new Vector3d(-1,0,5), 1.0));
		
		
		for (int i=-numPixelX/2; i<numPixelX/2; i++) {
			for (int j=-numPixelY/2; j<numPixelY/2; j++) {
				ArrayList<Vector4d> points = s.getWorldScreenCoordinates(i, j);
				for (Vector4d v:points) {	// Intersect ray with each objects
					
					Ray r = new Ray(c.getEw(), new Vector3d(v.x,v.y,v.z));
					
					for (Shape obj:objects) {
						Ray rReflected = obj.intersection(r);
						
						if (rReflected != null) {
							// TO DO:
								// 1. Calculate intersection between light and intersection point
								// 2. Calculate intersection between other objects with the reflected ray
							
							image.setRGB(i+numPixelX/2,j+numPixelY/2, obj.getColor().getRGB());
							
						}
						/*else {
							//System.out.println("No intersecta");
							image.setRGB(i+numPixelX/2,j+numPixelY/2, Color.black.getRGB());
						}*/
						
					}
					
					
				}
				
				
			}
		}
		
		Render render = new Render(image);
		
	}
}
