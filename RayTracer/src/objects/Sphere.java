package objects;

import java.awt.Color;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import scene.Ray;
import scene.Util;

public class Sphere extends Shape{
	
	private Vector3d c;
	private double rad;
	
	public Sphere(Vector3d c, double rad, double opaque, Color color) {
		this.c = c;
		this.rad = rad;
		this.opaque = opaque;
		r = color.getRed();
		g = color.getGreen();
		b = color.getBlue();
	}
	
	public void transformation (Matrix4d trans) {
		Vector4d p1Prov = new Vector4d(c.x, c.y, c.z, 1);
		p1Prov = Util.MultiplyVectorAndMatrix(trans, p1Prov);
		c = new Vector3d(p1Prov.x/p1Prov.w, p1Prov.y/p1Prov.w, p1Prov.z/p1Prov.w);
		//modify r?
	}
	
	//Returns the reflected ray or null if does not intersect
	public Ray intersection (Ray ray) {
		
		double A = Util.dotProduct(ray.direction, ray.direction);
		double B = Util.dotProduct(Util.substract(ray.position, c), ray.direction);
		double C = Util.dotProduct(Util.substract(ray.position, c), Util.substract(ray.position, c)) - rad;
		
		double D = B*B - A*C;

		/*System.out.println("A  " + A);
		System.out.println("B  " + B);
		System.out.println("C  " + C);
		System.out.println("D  " + C);*/
		
		if (D < 0) {		// No intersection
			return null;
		}
		else if (D == 0) {	// One intersection
			//Intersection point?
			return ray;
		}
		else {				// Two intersections
			
			
			double lambda1 = (-2*B + Math.sqrt(4*B*B - 4*A*C))/(2*A);
			double lambda2 = (-2*B - Math.sqrt(4*B*B - 4*A*C))/(2*A);
			
			//System.out.println("Lambdas:   " + lambda1 + "  " + lambda2);
			
			Vector3d pixel1 = ray.getPoint(lambda1);
			Vector3d pixel2 = ray.getPoint(lambda2);
			
			//System.out.println("Esfera1 -> " + pixel1);
			//System.out.println("Esfera2 -> " + pixel2);
			
			if (lambda1 < 0 && lambda2 < 0) {	// Out of view
				return null;
			}
			else if (lambda1 > 0 && lambda2 < 0) {	// Return lambda1
				Vector3d intersection = ray.getPoint(lambda1);
				Vector3d direction = direction(ray, intersection);
//				direction.normalize();
				return new Ray(intersection, direction);
			} 
			else if (lambda1 > lambda2 && lambda2 > 0) {	// Return lambda2
				Vector3d intersection = ray.getPoint(lambda2);
				Vector3d direction = direction(ray, intersection);
//				direction.normalize();
				return new Ray(intersection, direction);
			} 
			else if (lambda1 < 0 && lambda2 > 0) {	// Return lambda2
				Vector3d intersection = ray.getPoint(lambda2);
				Vector3d direction = direction(ray, intersection);
//				direction.normalize();
				return new Ray(intersection, direction);
			} 
			else {							// Return lambda1
				Vector3d intersection = ray.getPoint(lambda1);
				Vector3d direction = direction(ray, intersection);
//				direction.normalize();
				return new Ray(intersection, direction);
			}
			
		}
		
	}
	
	private Vector3d direction(Ray ray, Vector3d intersection) {
		Vector3d n = Util.substract(intersection, c);
		double escalar = 2*Util.dotProduct(ray.direction, n);
		return Util.substract(ray.direction, Util.dotScalar(n, escalar));
	}

	public Color getColor(double i) {
		return new Color((int) (i*kd*r),(int) (i*kd*g),(int) (i*kd*b));
	}
	
	public Color getColor(double i, Ray l) {
		Vector3d n = Util.substract(l.position, c);
		Double cos = Util.dotProduct(l.direction, n)/
				(Util.Norm(l.direction)*Util.Norm(n));
		if (cos > 0) {
			return new Color((int) (cos*i*kd*r),(int) (cos*i*kd*g),(int) (cos*i*kd*b));
		} else {
			return new Color(0,0,0);
		}
		
	}
	
	public Color getColor(double i, Ray l, Ray rLight, Ray vision) {
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
				return new Color((int) (cos*i*ks*r),(int) (cos*i*ks*g),(int) (cos*i*ks*b));
			}
		}
		return new Color(0,0,0);
		
	}

	
	public Ray getReflectedRay(Ray originRay, Vector3d intersection){
		Vector3d V = new Vector3d(originRay.direction);
		Vector3d N = Util.substract(intersection, c);
		
		// R = V-2(V*N)N
		Vector3d R = Util.substract(V, Util.dotScalar(N, 2*Util.dotProduct(V, N)));
		return new Ray(intersection, R);
	}
	
	public void setKr(double kr) {
		this.kr = kr;
	}
}
