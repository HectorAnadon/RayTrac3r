package scene;

import javax.vecmath.Vector3d;

public class Ray {

	/** Posición en donde se origina el rayo. */
	public Vector3d position;

	/** Dirección del rayo (vector normalizado) */
	public Vector3d direction;

	public double intensity = 1;
	
	public Ray(Vector3d origin, Vector3d direction, double intensity) {
		this.position = origin;
		this.intensity = intensity;
		this.direction = Util.substract(direction, origin);
	}
	
	public Ray(Vector3d origin, Vector3d direction, double intensity, boolean isDirection) {
		this.position = origin;
		this.intensity = intensity;
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "position: " + position + ", direction: " + direction;
	}
	
	
	public Vector3d getPoint(double lambda) {
		return Util.add(position, Util.dotScalar(direction, lambda));
	}
	
	public void inverseDirection() {
		direction = Util.inverse(direction);
	}
}
