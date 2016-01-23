package objects;

import java.awt.Color;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import scene.Ray;

/**
 * 
 * Shape object
 *
 */
public abstract class Shape {

	protected int r;
	protected int g;
	protected int b;

	protected double kd = 1; // Diffuse coefficient
	protected double ks = 1; // Specular coefficient

	public double kr = 0; // reflection coefficient
	public double kn = 1; // Refraction coefficient

	public double opaque = 1.0;

	/**
	 * Transform the Shape with the transformation matrix
	 * @param trans	Transformation matrix
	 */
	public abstract void transformation(Matrix4d trans);

	/**
	 * @param vector Incoming ray
	 * @return Return the reflected ray or null if does not intersect
	 */
	public abstract Ray intersection(Ray vector);

	/**
	 * @param i	Intensity
	 * @return Ambiental color
	 */
	public abstract Color getColor(double i);

	/**
	 * @param i	Intensity
	 * @param l	Incoming ray
	 * @param lightI	Light's intensity
	 * @return Difusse color
	 */
	public abstract Color getColor(double i, Ray l, double lightI);

	/**
	 * @param i	Intensity
	 * @param l	Incoming ray
	 * @param rLight	Ray form the light
	 * @param vision	Incoming ray
	 * @param lightI	Light's intensity
	 * @return Specular color
	 */
	public abstract Color getColor(double i, Ray l, Ray rLight, Ray vision, double lightI);

	/**
	 * 
	 * @param kr Reflection coefficient
	 * Set kr
	 */
	public void setKr(double kr) {
		this.kr = kr;
	}

	/**
	 * 
	 * @param kn Refraction coefficient
	 * Set kn
	 */
	public void setKn(double kn) {
		this.kn = kn;
	}

	/**
	 * 
	 * @param opaque Opacity
	 * Set opaque
	 */
	public void setOpaque(double opaque) {
		this.opaque = opaque;
	}

	/**
	 * 
	 * @param kd Diffuse coefficient
	 * Set kd
	 */
	public void setKd(double kd) {
		this.kd = kd;
	}
	
	/**
	 * 
	 * @param ks Specular coefficient
	 * Set ks
	 */
	public void setKs(double ks) {
		this.ks = ks;
	}

	/**
	 * @param vector Incoming ray
	 * @param intersection Intersection point
	 * @return Return the refracted ray
	 */
	public abstract Ray getRefraction(Ray vector, Vector3d intersection);

}
