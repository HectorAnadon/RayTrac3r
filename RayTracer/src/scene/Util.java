package scene;

import java.util.Arrays;

import javax.vecmath.*;

import objects.Triangle;

/**
 * 
 * For mathematical operations purpose
 *
 */
public class Util {
	
	/**
	 * 
	 * @param matrix
	 * @param vector
	 * @return matrix multiply vector
	 */
	public static Vector4d MultiplyVectorAndMatrix(Matrix4d matrix, Vector4d vector) {
		double[] res = new double[4];
		double[] oldValues = new double[4];
		oldValues[0] = vector.x;
		oldValues[1] = vector.y;
		oldValues[2] = vector.z;
		oldValues[3] = vector.w;
		
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				res[i] += matrix.getElement(j,i) * oldValues[j];
			}
		}
		return new Vector4d(res);
	}

	/**
	 * 
	 * @param vector
	 * @return normalized vector
	 */
	public static double Norm(Vector3d vector) {
		return Math.sqrt(Math.pow(vector.x, 2.0) + Math.pow(vector.y, 2.0) + Math.pow(vector.z, 2.0));
	}
	

	/**
	 * Multiplica componente a componente dos vectores, dejando el resultado en el primer vector.
	 * 
	 * @param v1 Vector a multiplicar, y lugar en donde se dejara el resultado.
	 * @param v2 Segundo operando.
	 */
	public static void multiplyVectors(Vector3d v1, Vector3d v2) {
		v1.x = v1.x * v2.x;
		v1.y = v1.y * v2.y;
		v1.z = v1.z * v2.z;
	}
	
	/**
	 * 
	 * @param v1 vector3d
	 * @param v2 vector3d
	 * @return v1 vectorial product v2
	 */
	public static Vector3d vectorialProduct (Vector3d v1, Vector3d v2) {
		return new Vector3d (v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x * v2.z,
				v1.x*v2.y - v1.y*v2.x);
	}

	/**
	 * 
	 * @param g vector3d
	 * @return inverse g
	 */
	public static Vector3d inverse(Vector3d g) {
		return new Vector3d(-g.x, -g.y, -g.z);
	}

	/**
	 * 
	 * @param g vector3d
	 * @param norm double
	 * @return divide g by norm
	 */
	public static Vector3d divide(Vector3d g, double norm) {
		return new Vector3d(g.x/norm, g.y/norm, g.z/norm);
	}

	/**
	 * 
	 * @param v1 vector3d
	 * @param v2 vector3d
	 * @return Producto escalar entre dos Vector3d
	 */
	public static double dotProduct(Vector3d v1, Vector3d v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z; 
	}
	
	/**
	 * 
	 * @param v1 vector3d
	 * @param v2 vector3d
	 * @return v1 - v2
	 */
	public static Vector3d substract(Vector3d v1, Vector3d v2) {
		return new Vector3d(v1.x-v2.x, 
				v1.y-v2.y, v1.z-v2.z);
	}
	
	/**
	 * 
	 * @param v1 vector3d
	 * @param v2 vector3d
	 * @return v1 + v2
	 */
	public static Vector3d add(Vector3d v1, Vector3d v2) {
		return new Vector3d(v1.x+v2.x, 
				v1.y+v2.y, v1.z+v2.z);
	}
	
	/**
	 * 
	 * @param v1 vector3d
	 * @param d scalar
	 * @return d scalar multiply v1
	 */
	public static Vector3d dotScalar(Vector3d v1, double d) {
		return new Vector3d(v1.x*d, 
				v1.y*d, v1.z*d);
	}

	/**
	 * 
	 * @param v1 vector3d
	 * @param v2 vector3d
	 * @return Distance between two points 
	 */
	public static double distance(Vector3d v1, Vector3d v2) {
		return Math.sqrt(Math.pow(v1.x-v2.x, 2.0) + Math.pow(v1.y-v2.y, 2.0) + 
				Math.pow(v1.z-v2.z, 2.0));
	}

}

