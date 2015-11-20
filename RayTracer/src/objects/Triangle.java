package objects;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import scene.Ray;
import scene.Util;

public class Triangle extends Shape{
	
	private Vector3d p1;
	private Vector3d p2;
	private Vector3d p3;
	private Vector3d n;
	
	public Triangle(Vector3d p1, Vector3d p2, Vector3d p3, double opaque) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.opaque = opaque;
		n = Util.vectorialProduct(Util.substract(p2,p1), Util.substract(p3,p1));
	}
	
	public void transformation (Matrix4d trans) {
		Vector4d p1Prov = new Vector4d(p1.x, p1.y, p1.z, 1);
		p1Prov = Util.MultiplyVectorAndMatrix(trans, p1Prov);
		p1 = new Vector3d(p1Prov.x/p1Prov.w, p1Prov.y/p1Prov.w, p1Prov.z/p1Prov.w);
		Vector4d p2Prov = new Vector4d(p2.x, p2.y, p2.z, 1);
		p2Prov = Util.MultiplyVectorAndMatrix(trans, p2Prov);
		p2 = new Vector3d(p2Prov.x/p2Prov.w, p2Prov.y/p2Prov.w, p2Prov.z/p2Prov.w);
		Vector4d p3Prov = new Vector4d(p3.x, p3.y, p3.z, 1);
		p3Prov = Util.MultiplyVectorAndMatrix(trans, p3Prov);
		p3 = new Vector3d(p3Prov.x/p3Prov.w, p3Prov.y/p3Prov.w, p3Prov.z/p3Prov.w);
	}
	
	//Returns the reflected ray or null if does not intersect
	public Ray intersection (Ray ray) {
		if (Util.dotProduct(ray.direction, n) == 0) {
			//System.out.println("No intersectionwith plane");
			if (Util.dotProduct(Util.substract(p1, ray.position),
				n) == 0) {
				//line contained in the tringle
				//Todo: where?? I wouldn�t do this
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
			Vector3d intersection = Util.add(new Vector3d(d*ray.direction.x, d*ray.direction.y, d*ray.direction.z)
					, ray.position);
			//Check if intersects inside the triangle
			if (Util.contains(intersection, this)) {
				//Todo: modify direction of ray given reflexion, opacity and object normal
				return new Ray(intersection, ray.direction);
			} else {
				return null;
			}
		}
	}
	
	/*public Ray intersection (Ray ray) {
		double dn = Util.dotProduct(ray.direction, n);
		
		if (dn == 0) {	// No intersection
			return null;
		}
		else if (dn > 0) {	
			
		}
		else {
			
		}
			
	}*/
	
	public Vector3d getP1() {
		return p1;
	}
	public Vector3d getP2() {
		return p2;
	}
	public Vector3d getP3() {
		return p3;
	}
}
