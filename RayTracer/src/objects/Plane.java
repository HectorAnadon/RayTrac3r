package objects;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import scene.Ray;
import scene.Util;

public class Plane extends Shape{
	
	private Point3d p1;
	private Vector3d n;
	
	public Plane(Point3d p1, Vector3d n, double opaque) {
		this.p1 = p1;
		this.n = n;
		this.opaque = opaque;
	}
	
	public void transformation (Matrix4d trans) {
		Vector4d p1Prov = new Vector4d(p1.x, p1.y, p1.z, 1);
		p1Prov = Util.MultiplyVectorAndMatrix(trans, p1Prov);
		p1 = new Point3d(p1Prov.x/p1Prov.w, p1Prov.y/p1Prov.w, p1Prov.z/p1Prov.w);
		Vector4d nProv = new Vector4d(n.x, n.y, n.z, 1);
		nProv = Util.MultiplyVectorAndMatrix(trans, nProv);
		n = new Vector3d(nProv.x/nProv.w, nProv.y/nProv.w,nProv.z/nProv.w);
	}
	
	//Returns the reflected ray or null if does not intersect
	public Ray intersection (Ray ray) {
		if (Util.dotProduct(ray.direction, n) == 0) {
			if (Util.dotProduct(Util.substract(p1, ray.position),
				n) == 0) {
				//line contained in the plane
				return ray;
			} else {
				return null;
			}
		} else {
			double d = Util.dotProduct(Util.substract(p1, ray.position),
					n)/Util.dotProduct(ray.direction, n);
			Vector3d intersection = Util.add(new Point3d(d*ray.direction.x, d*ray.direction.y, d*ray.direction.z)
					, ray.position);
			//Todo: modify direction of ray given reflexion and opacity
			return new Ray(intersection, ray.direction);
		}
	}
}