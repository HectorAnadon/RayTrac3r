package objects;

import java.awt.Color;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import scene.Ray;

public abstract class Shape {
	
	protected int r;
	protected int g;
	protected int b;

	protected double kd = 0.7;		// Diffuse coefficient
	protected double ks = 0.3;		// Specular coefficient
	
	public double kr = 0;		// reflection coefficient
	public double kn = 1;		// Refraction coefficient

	public double opaque = 1.0;

	
	public abstract void transformation (Matrix4d trans);

	
	//Returns the reflected ray or null if does not intersect
	public abstract Ray intersection (Ray vector);
	
	public abstract Color getColor(double i);
	
	public abstract Color getColor(double i, Ray l);
	
	public abstract Color getColor(double i, Ray l, Ray rLight, Ray vision);
	
	public void setKr(double kr) {
		this.kr = kr;
	}
	
	public void setKn(double kn) {
		this.kn = kn;
	}
	
	public void setOpaque(double opaque) {
		this.opaque = opaque;
	}
	
	public abstract Ray getRefraction (Ray vector, Vector3d intersection);

}
