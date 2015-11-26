package scene;

import javax.vecmath.Vector3d;

public class Ray {

	/** Posición en donde se origina el rayo. */
	public Vector3d position;

	/** Dirección del rayo (vector normalizado) */
	public Vector3d direction;

	public Ray(Vector3d origin, Vector3d direction) {
		//super();
		//direction.normalize();
		this.position = origin;
		//System.out.println("P1 -> " + direction);
		//System.out.println("P0 -> " + origin);
		
		this.direction = Util.substract(direction, origin);
		//this.direction = direction;
	}

	@Override
	public String toString() {
		return "position: " + position + ", direction: " + direction;
	}
	
	
	public Vector3d getPoint(double lambda) {
		return Util.add(position, Util.dotScalar(direction, lambda));
	}
	
}
