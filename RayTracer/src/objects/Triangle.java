package objects;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import scene.Ray;
import scene.Util;

public class Triangle extends Shape{
	
	private Point3d p1;
	private Point3d p2;
	private Point3d p3;
	private Vector3d n;
	
	public Triangle(Point3d p1, Point3d p2, Point3d p3, double opaque) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.opaque = opaque;
		n = Util.vectorialProduct(Util.substractPoints(p2,p1), Util.substractPoints(p3,p1));
	}
	
	public void transformation (Matrix4d trans) {
		Vector4d p1Prov = new Vector4d(p1.x, p1.y, p1.z, 1);
		p1Prov = Util.MultiplyVectorAndMatrix(trans, p1Prov);
		p1 = new Point3d(p1Prov.x/p1Prov.w, p1Prov.y/p1Prov.w, p1Prov.z/p1Prov.w);
		Vector4d p2Prov = new Vector4d(p2.x, p2.y, p2.z, 1);
		p2Prov = Util.MultiplyVectorAndMatrix(trans, p2Prov);
		p2 = new Point3d(p2Prov.x/p2Prov.w, p2Prov.y/p2Prov.w, p2Prov.z/p2Prov.w);
		Vector4d p3Prov = new Vector4d(p3.x, p3.y, p3.z, 1);
		p3Prov = Util.MultiplyVectorAndMatrix(trans, p3Prov);
		p3 = new Point3d(p3Prov.x/p3Prov.w, p3Prov.y/p3Prov.w, p3Prov.z/p3Prov.w);
	}
	
	//Returns the reflected ray or null if does not intersect
	public Ray intersection (Ray ray) {
		if (Util.dotProduct(ray.direction, n) == 0) {
			if (Util.dotProduct(Util.substract(p1, ray.position),
				n) == 0) {
				//line contained in the tringle
				//Todo: where?? I wouldn´t do this
				return ray;
			} else {
				return null;
			}
		} else {
			if (Util.dotProduct(ray.direction, n) > 0) {
				n = Util.inverse(n);
			}		
			double d = Util.dotProduct(Util.substract(p1, ray.position),
					n)/Util.dotProduct(ray.direction, n);
			Vector3d intersection = Util.add(new Point3d(d*ray.direction.x, d*ray.direction.y, d*ray.direction.z)
					, ray.position);
			//Check if intersects inside the triangle
			if (Util.contains(intersection, this)) {
				//Todo: modify direction of ray given reflexion and opacity
				return new Ray(intersection, ray.direction);
			} else {
				return null;
			}
		}
	}
	
	public Point3d getP1() {
		return p1;
	}
	public Point3d getP2() {
		return p2;
	}
	public Point3d getP3() {
		return p3;
	}
}
