package scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import objects.Shape;
import objects.Triangle;

public class Scene {
	
	static ArrayList<Shape> objects = new ArrayList<Shape>();
	
	private static final int numPixelX = 2000;
	private static final int numPixelY = 2000;
	
	private static BufferedImage image = new BufferedImage(numPixelX, numPixelY, BufferedImage.TYPE_INT_RGB);
	
	
	public static void main (String[] args) {
		Camera c = new Camera(new Vector3d(0,0,100), new Vector3d(0, 0 , 1), 
				new Vector3d(0,1,1), new Vector3d(0,0,0));
		
		Screen s = new Screen(c, 1, numPixelX, numPixelY, 2000, 2000);

		objects.add(new Triangle(new Vector3d(-1,0,5), new Vector3d(0,1,5), new Vector3d(1,0,5), 1.0));
		
		
		for (int i=0; i<numPixelX; i++) {
			for (int j=0; j<numPixelX; j++) {
				ArrayList<Vector4d> points = s.getWorldScreenCoordinates(i, j);
				for (Vector4d v:points) {	// Intersect ray with each objects
					
					Ray r = new Ray(c.getEw(), new Vector3d(v.x,v.y,v.z));
					
					for (Shape obj:objects) {
						Ray rReflected = obj.intersection(r);
						
						if (rReflected != null) {
							// TO DO:
								// 1. Calculate intersection between light and intersection point
								// 2. Calculate intersection between other objects with the reflected ray
							
							image.setRGB(j,i, obj.getColor().getRGB());
							
						}
						else {
							//System.out.println("No intersecta");
							image.setRGB(j,i, Color.black.getRGB());
						}
						
					}
					
					
				}
				
				
			}
		}
		
		Render render = new Render(image);
		
	}
}
