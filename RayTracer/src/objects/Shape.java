package objects;

import java.awt.Color;

import javax.vecmath.Matrix4d;

import scene.Ray;

public abstract class Shape {
	
	double opaque = 1.0;
	private static int r = 255;
	private static int g = 0;
	private static int b = 0;
	
	
	public abstract void transformation (Matrix4d trans);

	
	//Returns the reflected ray or null if does not intersect
	public abstract Ray intersection (Ray vector);
	
	public static Color getColor() {
		return new Color(r,g,b);
	}
 
}
