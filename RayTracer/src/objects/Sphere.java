package objects;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import scene.Ray;
import scene.Util;

public class Sphere extends Shape{
	
	private Vector3d p1;
	private double r;
	
	public Sphere(Vector3d p1, double r, double opaque) {
		this.p1 = p1;
		this.r = r;
		this.opaque = opaque;
	}
	
	public void transformation (Matrix4d trans) {
		Vector4d p1Prov = new Vector4d(p1.x, p1.y, p1.z, 1);
		p1Prov = Util.MultiplyVectorAndMatrix(trans, p1Prov);
		p1 = new Vector3d(p1Prov.x/p1Prov.w, p1Prov.y/p1Prov.w, p1Prov.z/p1Prov.w);
		//modify r?
	}
	
	//Returns the reflected ray or null if does not intersect
	public Ray intersection (Ray vector) {
		//Todo:
		return null;
	}

}
