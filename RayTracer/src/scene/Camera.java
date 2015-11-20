package scene;

import javax.vecmath.*;

public class Camera {

	private Vector3d ew;
	private Vector3d g;
	private Vector3d up;
	private Vector3d u;
	private Vector3d v;
	private Vector3d w;
	private Matrix4d M;
	private Matrix4d Mwc;
	private Vector3d d;

	public Camera(Vector3d ew, Vector3d g, Vector3d up, Vector3d d) {
		this.d = d;
		this.ew = ew;
		this.g = g;
		this.up = up;
		w = Util.divide(Util.inverse(g), Util.Norm(g));
		u = Util.divide(Util.vectorialProduct(up, w), Util.Norm(Util.vectorialProduct(up, w)));
		v = Util.vectorialProduct(w, u);
		//System.out.println(w + "\n" + u + "\n" + v);
		M = new Matrix4d(u.x, u.y, u.z, 0, v.x, v.y, v.z, 0, w.x, w.y, w.z, 0, ew.x, ew.y, ew.z, 1);
		//Mwc = new Matrix4d(u.x, v.x, w.x, 0, u.y, v.y, w.y, 0, u.z, v.z, w.z, 0, d.x, d.y, d.z, 1);
		
		
		System.out.println("Centro cámara: " + ew);
	}

	public Vector3d getU() {
		return u;
	}

	public Vector3d getV() {
		return v;
	}

	public Vector3d getW() {
		return w;
	}

	public Vector3d getEw() {
		return ew;
	}

}
