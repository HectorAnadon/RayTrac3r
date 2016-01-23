package scene;

import javax.vecmath.*;

/**
 * Camera object
 *
 */
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

	/**
	 * 
	 * @param ew position
	 * @param g direction vector
	 * @param up up vector
	 * @param d distance to screen
	 */
	public Camera(Vector3d ew, Vector3d g, Vector3d up, Vector3d d) {
		this.d = d;
		this.ew = ew;
		this.g = g;
		this.up = up;
		w = Util.divide(Util.inverse(g), Util.Norm(g));
		u = Util.divide(Util.vectorialProduct(up, w), Util.Norm(Util.vectorialProduct(up, w)));
		v = Util.vectorialProduct(w, u);
		M = new Matrix4d(u.x, u.y, u.z, 0, v.x, v.y, v.z, 0, w.x, w.y, w.z, 0, ew.x, ew.y, ew.z, 1);
		
		
		System.out.println("Centro cámara: " + ew);
	}

	/**
	 * 
	 * @return vector u
	 */
	public Vector3d getU() {
		return u;
	}

	/**
	 * 
	 * @return vector v
	 */
	public Vector3d getV() {
		return v;
	}

	/**
	 * 
	 * @return vector w
	 */
	public Vector3d getW() {
		return w;
	}

	/**
	 * 
	 * @return ew vector
	 */
	public Vector3d getEw() {
		return ew;
	}

}
