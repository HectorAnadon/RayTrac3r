package objects;

import java.awt.Color;
import java.util.Arrays;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import scene.Ray;
import scene.Util;

/**
 * Triangle Shape
 */
public class Triangle extends Shape{
	
	private Vector3d p1;
	private Vector3d p2;
	private Vector3d p3;
	private Vector3d n;
	
	/**
	 * @param p1	Point 1
	 * @param p2	Point 2
	 * @param p3	Point 3
	 * @param opaque	Opacity
	 * @param c	Color
	 */
	public Triangle(Vector3d p1, Vector3d p2, Vector3d p3, double opaque, Color c) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.opaque = opaque;
		r = c.getRed();
		g = c.getGreen();
		b = c.getBlue();
		n = Util.vectorialProduct(Util.substract(p2,p1), Util.substract(p3,p1));
	}
	
	/**
	 * Transform the Plane with the transformation matrix
	 * @param trans	Transformation matrix
	 */
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
		n = Util.vectorialProduct(Util.substract(p2,p1), Util.substract(p3,p1));
	}
	
	/**
	 * @param ray Incoming ray
	 * @return Return the reflected ray or null if does not intersect
	 */
	public Ray intersection (Ray ray) {
		Vector3d normal = new Vector3d(n);
		double dn = Util.dotProduct(ray.direction, normal);
		if (dn == 0) {
			return null;
		} else {
			if (dn>0){
				normal = Util.inverse(normal);
				dn = Util.dotProduct(ray.direction, normal);
			}
			double d = Util.dotProduct(Util.substract(p1, ray.position),
					normal)/dn;
			Vector3d intersection = ray.getPoint(d);
			
			//Check if intersects inside the triangle
			if (dn < 0 && contains(intersection)) {
				double escalar = 2*Util.dotProduct(ray.direction, normal);
				Vector3d direction = Util.substract(ray.direction, Util.dotScalar(normal, escalar));
				return new Ray(intersection, Util.inverse(direction), getIntensity(ray), true);
			} else {
				return null;
			}
		}
	}
	
	/**
	 * @param ray Incoming ray
	 * @param intersection Intersection point
	 * @return Return the refracted ray
	 */
	public Ray getRefraction (Ray ray, Vector3d intersection) {
		Vector3d normal = new Vector3d(n);
		Vector3d dir = new Vector3d(ray.direction);
		double nDotI = Util.dotProduct(normal, dir);
		if (kn == 1) {
			return new Ray(ray.position, ray.direction, getRefractedIntensity(ray),true);
		}
		double square = 1 - (Math.pow(1/kn, 2) * (1 - Math.pow(nDotI, 2)));
		if (square >= 0) {
			double t = (1/kn * nDotI) - Math.sqrt(square);
			normal.scale(t);
			dir.scale(1/kn);
			normal.sub(dir);
			return new Ray(intersection, normal, getRefractedIntensity(ray), true);
		} else {
			return intersection(ray);
		}
	}
	
	/**
	 * @param ray	Incoming ray
	 * @return Refracted ray's intensity
	 */
	private double getRefractedIntensity(Ray ray) {
		return (ray.intensity*(1-opaque));
	}

	/**
	 * @param ray	Incoming ray
	 * @return reflected ray's intensity
	 */
	private double getIntensity(Ray ray) {
		return (ray.intensity*kr);
	}

	/**
	 * @param intersection Intersection point
	 * @return	True if the Triangle contains the intersection point
	 */
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
	
	/**
	 * @return point 1
	 */
	public Vector3d getP1() {
		return p1;
	}
	/**
	 * @return point 2
	 */
	public Vector3d getP2() {
		return p2;
	}
	/**
	 * @return point 3
	 */
	public Vector3d getP3() {
		return p3;
	}
	
	/**
	 * @param double	Intensity
	 * @return Ambiental color
	 */
	public Color getColor(double i) {
		return new Color((int) (i*kd*r*opaque),(int) (i*kd*g*opaque),(int) (i*kd*b*opaque));
	}
	
	/**
	 * @param double	Intensity
	 * @param l	Incoming ray
	 * @param lightI	Light's intensity
	 * @return Difusse color
	 */
	public Color getColor(double i, Ray l, double lightI) {
		Double cos = Util.dotProduct(l.direction, n)/
				(Util.Norm(l.direction)*Util.Norm(n));
		if (cos > 0) {
			return new Color((int) (cos*i*lightI*kd*r*opaque),(int) (cos*i*lightI*kd*g*opaque),(int) (cos*i*lightI*kd*b*opaque));
		} else {
			Vector3d normal = Util.inverse(n);
			cos = Util.dotProduct(l.direction, normal)/
					(Util.Norm(l.direction)*Util.Norm(normal));
			if (cos > 0) {
				return new Color((int) (cos*i*lightI*kd*r*opaque),(int) (cos*i*lightI*kd*g*opaque),(int) (cos*i*lightI*kd*b*opaque));
			}
			return new Color(0,0,0);
		}
	}
	
	/**
	 * @param double	Intensity
	 * @param l	Incoming ray
	 * @param rLight	Ray form the light
	 * @param vision	Incoming ray
	 * @param lightI	Light's intensity
	 * @return Specular color
	 */
	public Color getColor(double i, Ray l, Ray rLight, Ray vision, double lightI) {
		int n = 150;
		Double cos = Math.pow(Util.dotProduct(rLight.direction, vision.direction)/
				(Util.Norm(rLight.direction)*Util.Norm(vision.direction)), n);
		if (cos > 0) {
			return new Color((int) (cos*i*lightI*ks*r*opaque),(int) (cos*i*lightI*ks*g*opaque),(int) (cos*i*lightI*ks*b*opaque));
		} 
		return new Color(0,0,0);
	}
	
}
