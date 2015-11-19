package objects;

import javax.vecmath.Matrix4d;

import scene.Ray;

public class Shape {
	
	double opaque = 1.0;
	
	public void transformation (Matrix4d trans) {

	}
	
	//Returns the reflected ray or null if does not intersect
	public Ray intersection (Ray vector) {
		return null;
	}

}
