package scene;

import javax.vecmath.*;

public class Camera {
	
	private Point3d ew;
	private Vector3d g;
	private Vector3d up;
	private Vector3d u;
	private Vector3d v;
	private Vector3d w;
	
	public Camera (Point3d ew, Vector3d g, Vector3d up) {
		this.ew = ew;
		this.g = g;
		this.up = up;
		w = Util.divide(Util.inverse(g), Util.Norm(g));
		System.out.println(w);
	}

}
