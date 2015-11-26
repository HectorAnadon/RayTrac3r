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
	
	public Plane(Vector3d p1, Vector3d n, double opaque, Color c) {
		this.p1 = p1;
		this.n = n;
		this.opaque = opaque;
		g = c.getGreen();
		r = c.getRed();
		b = c.getBlue();
		
		kr = 0;
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
		Vector3d normal = n;
		double dn = Util.dotProduct(ray.direction, normal);
		if (dn == 0) {
			return null;
		} else {
			if (dn > 0) {
//				normal = Util.inverse(n);
				dn = Util.dotProduct(ray.direction, normal);
			}
			double d = Util.dotProduct(Util.substract(p1, ray.position),
					normal)/dn;
			if (dn < 0) {
				Vector3d intersection = ray.getPoint(d);	
				double escalar = 2*Util.dotProduct(ray.direction, normal);
				Vector3d direction = Util.substract(ray.direction, Util.dotScalar(normal, escalar));
				return new Ray(intersection, direction);
			}
			return null;
			
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
	
	public Color getColor(double i, Ray l, Ray rLight, Ray vision) {
		Double cosLight = Util.dotProduct(l.direction, n)/
				(Util.Norm(l.direction)*Util.Norm(n));
		if (cosLight > 0) {
			int n = 100;
			Double cos = Math.pow(Util.dotProduct(rLight.direction, vision.direction)/
					(Util.Norm(rLight.direction)*Util.Norm(vision.direction)), n);
			if (cos > 0) {
				return new Color((int) (cos*i*ks*r),(int) (cos*i*ks*g),(int) (cos*i*ks*b));
			}
		}
		return new Color(0,0,0);
		
	}


	public Ray getReflectedRay(Ray originRay, Vector3d intersection){
		Vector3d V = new Vector3d(originRay.direction);
		Vector3d N = new Vector3d(n);
		
		// R = V-2(V*N)N
		Vector3d R = Util.substract(V, Util.dotScalar(N, 2*Util.dotProduct(V, N)));
		return new Ray(intersection, R);
	}
	
	
	public void setKr(int kr) {
		this.kr = kr;
	}
}
