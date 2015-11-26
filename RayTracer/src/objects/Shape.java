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
	protected double ks = 0.3;
	
	protected double kr = 0.3;		// Specular reflection coefficient
	protected double kt = ks;		// Specular transmission coefficient

	protected double opaque = 1.0;

	
	public abstract void transformation (Matrix4d trans);

	
	//Returns the reflected ray or null if does not intersect
	public abstract Ray intersection (Ray vector);
	
	public abstract Color getColor(double i);
	
	public abstract Color getColor(double i, Ray l);
	
	public abstract Color getColor(double i, Ray l, Ray rLight, Ray vision);
	
	public abstract Ray getReflectedRay(Ray originRay, Vector3d intersection);

}
