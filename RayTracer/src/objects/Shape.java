package objects;

import javax.vecmath.Matrix4d;

import scene.Ray;

public abstract class Shape {
	
	double opaque = 1.0;
	int r = 255;
	int g = 0;
	int b = 0;
	
	
	public abstract void transformation (Matrix4d trans);

	
	//Returns the reflected ray or null if does not intersect
	public abstract Ray intersection (Ray vector);

}
