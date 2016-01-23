package scene;

import javax.vecmath.Vector3d;

/**
 * Ray Object
 *
 */
public class Ray {

	public Vector3d position;

	public Vector3d direction;

	public double intensity = 1;
	
	/**
	 * 
	 * @param origin Origin position
	 * @param direction Direction Vector
	 * @param intensity
	 */
	public Ray(Vector3d origin, Vector3d direction, double intensity) {
		this.position = origin;
		this.intensity = intensity;
		this.direction = Util.substract(direction, origin);
	}
	
	/**
	 * 
	 * @param origin Origin position
	 * @param direction Direction Vector
	 * @param intensity
	 * @param isDirection
	 */
	public Ray(Vector3d origin, Vector3d direction, double intensity, boolean isDirection) {
		this.position = origin;
		this.intensity = intensity;
		this.direction = direction;
	}

	/**
	 *  Prints for debugging purpose
	 */
	public String toString() {
		return "position: " + position + ", direction: " + direction;
	}
	
	/**
	 * 
	 * @param lambda position of the ray
	 * @return Point of the ray
	 */
	public Vector3d getPoint(double lambda) {
		return Util.add(position, Util.dotScalar(direction, lambda));
	}
	
	/**
	 * Invert ray direction
	 */
	public void inverseDirection() {
		direction = Util.inverse(direction);
	}
}
