package scene;

import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import objects.Shape;
import objects.Triangle;

public class Scene {
	
	static ArrayList<Shape> objects;
	
	private static final int numPixelX = 20;
	private static final int numPixelY = 20;
	
	
	public static void main (String[] args) {
		Camera c = new Camera(new Vector3d(0,0,10), new Vector3d(0, 0 , 1), 
				new Vector3d(0,1,1), new Vector3d(0,0,0));
		
		Screen s = new Screen(c, 4, numPixelX, numPixelY, 2, 2);

		objects.add(new Triangle(new Vector3d(-1,0,5), new Vector3d(0,1,5), new Vector3d(1,0,5), 1.0));
		
		
		for (int i=0; i<numPixelX; i++) {
			for (int j=0; j<numPixelX; j++) {
				
				ArrayList<Vector4d> points = s.getWorldScreenCoordinates(i, j);
				
				for (Vector4d v:points) {
					Ray r = new Ray(c.getEw(), new Vector3d(v.x,v.y,v.z));
				}
				
				
			}
		}
		
	}
}
