package objects;

import java.awt.Color;

import javax.vecmath.Matrix4d;

import scene.Ray;

public abstract class Shape {
	
	double opaque = 1.0;
	
	
	
	public abstract void transformation (Matrix4d trans);

	
	//Returns the reflected ray or null if does not intersect
	public abstract Ray intersection (Ray vector);
	
	public abstract Color getColor(double i);
	
	public abstract Color getColor(double i, Ray l);
	
	public abstract Color getColor(double i, Ray rLight, Ray vision);
}
