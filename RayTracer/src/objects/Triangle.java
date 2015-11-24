package objects;

import java.awt.Color;
import java.util.Arrays;

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
	private static int r;
	private static int g;
	private static int b;
	private double kd = 0.7;
	private double ks = 0.5;
	
	public Triangle(Vector3d p1, Vector3d p2, Vector3d p3, double opaque) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.opaque = opaque;
		r = 255;
		g = 59;
		b = 0;
		n = Util.vectorialProduct(Util.substract(p2,p1), Util.substract(p3,p1));
		System.out.println("normal " +n);
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
			
			//Check if intersects inside the triangle
			if (contains(intersection)) {
				//Todo: modify direction of ray given reflexion, opacity and object normal
				double escalar = 2*Util.dotProduct(ray.direction, n);
				Vector3d direction = Util.substract(ray.direction, Util.dotScalar(n, escalar));
				return new Ray(intersection, direction);
			} else {
				return null;
			}
		}
	}


	public boolean contains(Vector3d intersection) {
		double s1 = Util.dotProduct(Util.vectorialProduct(Util.substract(p2, p1), Util.substract(intersection, p1)), n);
		double s2 = Util.dotProduct(Util.vectorialProduct(Util.substract(p3, p2), Util.substract(intersection, p2)), n);
		double s3 = Util.dotProduct(Util.vectorialProduct(Util.substract(p1, p3), Util.substract(intersection, p3)), n);
	
		if ((s1>0 && s2>0 && s3>0) || (s1<0 && s2<0 && s3<0)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public Vector3d getP1() {
		return p1;
	}
	public Vector3d getP2() {
		return p2;
	}
	public Vector3d getP3() {
		return p3;
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
}
