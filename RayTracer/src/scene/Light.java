package scene;

import javax.vecmath.*;

/**
 * Light object
 *
 */
public class Light {
	private Vector3d position;
	private double intensity = 1;
	
	/**
	 * 
	 * @param position Set light position
	 */
	public Light (Vector3d position) {
		this.position = position;
	}
	
	/**
	 * 
	 * @return Light position
	 */
	public Vector3d getPosition() {
		return position;
	}
	
	/**
	 * 
	 * @return Light intensity
	 */
	public double getIntensity() {
		return intensity;
	}

	/**
	 * 
	 * @param i Set light intensitiy
	 */
	public void setIntensity(double i) {
		this.intensity = i;
	}
	
	/**
	 * 
	 * @param position Set light position
	 */
	public void setPosition(Vector3d position) {
		this.position = position;
	}

	/**
	 * 
	 * @param transformationMatrix Transform light position
	 */
	public void transform(Matrix4d transformationMatrix) {
		Vector4d aux = new Vector4d(position.x, position.y, position
				.z, 1);
		aux = Util.MultiplyVectorAndMatrix(transformationMatrix, aux);
		position.set(aux.x, aux.y, aux.z);
	}
	
}
