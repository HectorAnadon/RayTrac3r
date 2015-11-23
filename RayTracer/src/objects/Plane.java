package objects;

import java.awt.Color;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import scene.Ray;
import scene.Util;

public class Plane extends Shape{
	
	private Vector3d p1;
	private Vector3d n;
	private static int r;
	private static int g;
	private static int b;
	private double kd = 0.7;
	private double ks = 0.3;
	
	public Plane(Vector3d p1, Vector3d n, double opaque) {
		this.p1 = p1;
		this.n = n;
		this.opaque = opaque;
		g = 200;
		r = 0;
		b = 0;
	}
	
	public void transformation (Matrix4d trans) {
		Vector4d p1Prov = new Vector4d(p1.x, p1.y, p1.z, 1);
		p1Prov = Util.MultiplyVectorAndMatrix(trans, p1Prov);
		p1 = new Vector3d(p1Prov.x/p1Prov.w, p1Prov.y/p1Prov.w, p1Prov.z/p1Prov.w);
		Vector4d nProv = new Vector4d(n.x, n.y, n.z, 1);
		nProv = Util.MultiplyVectorAndMatrix(trans, nProv);
		n = new Vector3d(nProv.x/nProv.w, nProv.y/nProv.w,nProv.z/nProv.w);
	}
	
	//Returns the reflected ray or null if does not intersect
	public Ray intersection (Ray ray) {
		double dn = Util.dotProduct(ray.direction, n);
		if (dn == 0) {
			return null;
		} else {
			if (dn > 0) {
				n = Util.inverse(n);
				dn = Util.dotProduct(ray.direction, n);
			}
			
			double d = Util.dotProduct(Util.substract(p1, ray.position),
					n)/dn;
			if (dn < 0 && d < 0) {
				return null;
			}
			Vector3d intersection = ray.getPoint(d);	
			double escalar = 2*Util.dotProduct(ray.direction, n);
			Vector3d direction = Util.substract(ray.direction, Util.dotScalar(n, escalar));
			return new Ray(intersection, direction);
		}
	}
	
	public Color getColor(double i) {
		return new Color((int) (i*kd*r),(int) (i*kd*g),(int) (i*kd*b));
	}
	
	public Color getColor(double i, Ray l) {
		Double cos = Util.dotProduct(l.direction, n)/
				(Util.Norm(l.direction)*Util.Norm(n));
		if (cos > 0) {
			return new Color((int) (cos*i*kd*r),(int) (cos*i*kd*g),(int) (cos*i*kd*b));
		} else {
			return new Color(0,0,0);
		}
	}
	
	public Color getColor(double i, Ray rLight, Ray vision) {
		int n = 100;
		Double cos = Math.pow(Util.dotProduct(rLight.direction, vision.direction)/
				(Util.Norm(rLight.direction)*Util.Norm(vision.direction)), n);
		if (cos > 0) {
			return new Color((int) (cos*i*ks*r),(int) (cos*i*ks*g),(int) (cos*i*ks*b));
		} else {
			return new Color(0,0,0);
		}
		
	}
}
