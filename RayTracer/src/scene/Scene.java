package scene;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Scene {
	
	static Object[] objects;
	
	public Scene() {
		
	}
	
	public static void add(Object obj) {
		//objects.add(obj);
	}
	
	public static void main (String[] args) {
		Camera c = new Camera(new Point3d(0,0,5), new Vector3d(0,0,-5), new Vector3d(0,1,0));
	}
}
