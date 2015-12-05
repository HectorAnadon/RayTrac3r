package scene;

import javax.vecmath.*;

public class Light {
	private Vector3d position;
	private double intensity = 1;
	
	public Light (Vector3d position) {
		this.position = position;
	}
	
	public Vector3d getPosition() {
		return position;
	}
	
	public double getIntensity() {
		return intensity;
	}

	public void setIntensity(double i) {
		this.intensity = i;
	}
	
	public void setPosition(Vector3d position) {
		this.position = position;
	}

	public void transform(Matrix4d transformationMatrix) {
		Vector4d aux = new Vector4d(position.x, position.y, position
				.z, 1);
		aux = Util.MultiplyVectorAndMatrix(transformationMatrix, aux);
		position.set(aux.x, aux.y, aux.z);
	}
	
}
