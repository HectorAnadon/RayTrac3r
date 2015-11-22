package scene;

import javax.vecmath.*;

public class Light {
	private Vector3d position;
	private Vector3d direction;
	private double intensity = 1;
	
	public Light (Vector3d position, Vector3d direction) {
		this.position = position;
		this.direction = direction;
	}
	
	public Vector3d getPosition() {
		return position;
	}

	public Vector3d getDirection() {
		return direction;
	}
	
	public double getIntensity() {
		return intensity;
	}

	public void setPosition(Vector3d position) {
		this.position = position;
	}
	
	public void setDirection(Vector3d direction) {
		this.direction = direction;
	}

	public void transform(Matrix4d transformationMatrix) {
		Vector4d aux = new Vector4d(position.x, position.y, position
				.z, 1);
		aux = Util.MultiplyVectorAndMatrix(transformationMatrix, aux);
		position.set(aux.x, aux.y, aux.z);
	}
	
}
