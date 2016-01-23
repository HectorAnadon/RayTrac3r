package objects;

import java.awt.Color;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import scene.Ray;
import scene.Util;

/**
 * Plane Shape
 */
public class Plane extends Shape{
	
	private Vector3d p1;
	private Vector3d n;
	
	/**
	 * @param p1	Point in the plane
	 * @param n	Plane's normal
	 * @param opaque	Opacity
	 * @param c	Color
	 */
	public Plane(Vector3d p1, Vector3d n, double opaque, Color c) {
		this.p1 = p1;
		this.n = n;
		this.opaque = opaque;
		g = c.getGreen();
		r = c.getRed();
		b = c.getBlue();
		
		kr = 0;
	}
	
	/**
	 * Transform the Plane with the transformation matrix
	 * @param trans	Transformation matrix
	 */
	public void transformation (Matrix4d trans) {
		Vector4d p1Prov = new Vector4d(p1.x, p1.y, p1.z, 1);
		p1Prov = Util.MultiplyVectorAndMatrix(trans, p1Prov);
		p1 = new Vector3d(p1Prov.x/p1Prov.w, p1Prov.y/p1Prov.w, p1Prov.z/p1Prov.w);
		Vector4d nProv = new Vector4d(n.x, n.y, n.z, 1);
		nProv = Util.MultiplyVectorAndMatrix(trans, nProv);
		n = new Vector3d(nProv.x/nProv.w, nProv.y/nProv.w,nProv.z/nProv.w);
	}
	
	/**
	 * @param ray Incoming ray
	 * @return Return the reflected ray or null if does not intersect
	 */
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
				Ray rf = new Ray(intersection, Util.inverse(direction), getIntensity(ray));
				return rf;
			}
			return null;
			
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
		Vector3d normal = n;
		Double cos = Util.dotProduct(l.direction, normal)/
				(Util.Norm(l.direction)*Util.Norm(normal));
		if (cos > 0) {
			return new Color((int) (cos*i*lightI*kd*r*opaque),(int) (cos*i*lightI*kd*g*opaque),(int) (cos*i*lightI*kd*b*opaque));
		} else {
			normal = Util.inverse(n);
			 cos = Util.dotProduct(l.direction, normal)/
					(Util.Norm(l.direction)*Util.Norm(normal));
			if (cos > 0) {
				return new Color((int) (cos*i*lightI*kd*r*opaque),(int) (cos*i*lightI*kd*g*opaque),(int) (cos*i*lightI*kd*b*opaque));
			} else {
				return new Color(0,0,0);
			}
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
		int n = 100;
		Double cos = Math.pow(Util.dotProduct(rLight.direction, vision.direction)/
				(Util.Norm(rLight.direction)*Util.Norm(vision.direction)), n);
		if (cos > 0) {
			return new Color((int) (cos*i*lightI*ks*r*opaque),(int) (cos*i*lightI*ks*g*opaque),(int) (cos*i*lightI*ks*b*opaque));
		} 
		return new Color(0,0,0);
		
	}

}
