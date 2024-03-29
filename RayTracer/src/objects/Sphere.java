package objects;

import java.awt.Color;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import scene.Ray;
import scene.Util;

/**
 *Sphere Shape
 */
public class Sphere extends Shape{
	
	private Vector3d c;
	private double rad;
	
	/**
	 * @param c	Center
	 * @param rad	Radius
	 * @param opaque	Opacity
	 * @param color	Color
	 */
	public Sphere(Vector3d c, double rad, double opaque, Color color) {
		this.c = c;
		this.rad = rad;
		this.opaque = opaque;
		r = color.getRed();
		g = color.getGreen();
		b = color.getBlue();
	}
	
	/**
	 * Transform the Sphere with the transformation matrix
	 * @param trans	Transformation matrix
	 */
	public void transformation (Matrix4d trans) {
		Vector4d p1Prov = new Vector4d(c.x, c.y, c.z, 1);
		p1Prov = Util.MultiplyVectorAndMatrix(trans, p1Prov);
		c = new Vector3d(p1Prov.x/p1Prov.w, p1Prov.y/p1Prov.w, p1Prov.z/p1Prov.w);
		//modify r?
	}
	
	/**
	 * @param ray Incoming ray
	 * @return Return the reflected ray or null if does not intersect
	 */
	public Ray intersection (Ray ray) {
		
		double A = Util.dotProduct(ray.direction, ray.direction);
		double B = Util.dotProduct(Util.substract(ray.position, c), ray.direction);
		double C = Util.dotProduct(Util.substract(ray.position, c), Util.substract(ray.position, c)) - rad;
		
		double D = B*B - A*C;
		
		if (D < 0) {		// No intersection
			return null;
		}
		else if (D == 0) {	// One intersection
			return ray;
		}
		else {				// Two intersections
			
			double lambda1 = (-2*B + Math.sqrt(4*B*B - 4*A*C))/(2*A);
			double lambda2 = (-2*B - Math.sqrt(4*B*B - 4*A*C))/(2*A);
			
			Vector3d pixel1 = ray.getPoint(lambda1);
			Vector3d pixel2 = ray.getPoint(lambda2);
			
			if (lambda1 < 0 && lambda2 < 0) {	// Out of view
				return null;
			}
			else if (lambda1 > 0 && lambda2 < 0) {	// Return lambda1
				Vector3d intersection = ray.getPoint(lambda1);
				return new Ray(intersection, Util.inverse(direction(ray, intersection)), getIntensity(ray), true);
			} 
			else if (lambda1 > lambda2 && lambda2 > 0) {	// Return lambda2
				Vector3d intersection = ray.getPoint(lambda2);
				return new Ray(intersection, Util.inverse(direction(ray, intersection)), getIntensity(ray), true);
			} 
			else if (lambda1 < 0 && lambda2 > 0) {	// Return lambda2
				Vector3d intersection = ray.getPoint(lambda2);
				return new Ray(intersection, Util.inverse(direction(ray, intersection)), getIntensity(ray), true);
			} 
			else {							// Return lambda1
				Vector3d intersection = ray.getPoint(lambda1);
				return new Ray(intersection, Util.inverse(direction(ray, intersection)), getIntensity(ray), true);
			}
		}
		
	}
	
	/**
	 * @param ray Incoming ray
	 * @param intersection Intersection point
	 * @return Return the refracted ray
	 */
	public Ray getRefraction (Ray ray, Vector3d intersection) {
		Vector3d normal = Util.substract(intersection, c);
		Vector3d dir = new Vector3d(ray.direction);
		double nDotI = Util.dotProduct(normal, dir);
		double square = 1 - (Math.pow(1/kn, 2) * (1 - Math.pow(nDotI, 2)));
		if (kn == 1) {
			return new Ray(ray.position, ray.direction, getRefractedIntensity(ray),true);
		}
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
	 * @param ray	Incoming ray
	 * @param intersection	Intersection point
	 * @return	Normal
	 */
	private Vector3d direction(Ray ray, Vector3d intersection) {
		Vector3d n = Util.substract(intersection, c);
		double escalar = 2*Util.dotProduct(ray.direction, n);
		return Util.substract(ray.direction, Util.dotScalar(n, escalar));
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
		Vector3d n = Util.substract(l.position, c);
		Double cos = Util.dotProduct(l.direction, n)/
				(Util.Norm(l.direction)*Util.Norm(n));
		if (cos > 0) {
			return new Color((int) (cos*i*lightI*kd*r*opaque),(int) (cos*i*lightI*kd*g*opaque),(int) (cos*i*lightI*kd*b*opaque));
		} else {
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
		Vector3d normal = Util.substract(l.position, c);
		Double cosLight = Util.dotProduct(l.direction, normal)/
				(Util.Norm(l.direction)*Util.Norm(normal));
		if (cosLight > 0) {
			int n = 200;
			Double cos = Math.pow(Util.dotProduct(rLight.direction, vision.direction)/
					(Util.Norm(rLight.direction)*Util.Norm(vision.direction)), n);
			//System.out.println(rLight.direction);
			//System.out.println(vision.direction);
			if (cos > 0) {
				return new Color((int) (cos*i*lightI*ks*r*opaque),(int) (cos*i*lightI*ks*g*opaque),(int) (cos*i*lightI*ks*b*opaque));
			}
		}
		return new Color(0,0,0);
		
	}

}
