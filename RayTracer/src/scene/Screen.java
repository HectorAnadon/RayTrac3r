package scene;

import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.*;

public class Screen {

	private Camera c;
	private static double distance;
	private int numRow, numCol;
	private double hight, width;
	private static Matrix4d M;
	private static Random r;

	private static double Au, Av;

	public Screen(Camera c, double distance, int numRow, int numCol, double height, double width) {
		this.c = c;
		this.distance = distance;
		this.numRow = numRow;
		this.numCol = numCol;
		this.hight = height;
		this.width = width;

		Au = width / (numCol - 1);
		Av = height / (numRow - 1);
		
		r = new Random();

		double[] m = { c.getU().x, c.getU().y, c.getU().z, 0, c.getV().x, c.getV().y, c.getV().z, 0, c.getW().x,
				c.getW().y, c.getW().z, 0, c.getEw().x, c.getEw().y, c.getEw().z, 1 };
		this.M = new Matrix4d(m);
	}

	/**
	 * Gets the world coordinates from the pixel position in the screen
	 */
	public static ArrayList<Vector4d> getWorldScreenCoordinates(int x, int y) {
		//System.out.println("(" + x + "," + y + ")");
		Vector3d Pc = new Vector3d(x, y, -distance);
		Util.multiplyVectors(Pc, new Vector3d(Au, Av, 1));
		//System.out.println("Pc   " + Pc);

		Vector4d Pw = new Vector4d(Pc.x, Pc.y, Pc.z, 1);
		ArrayList<Vector4d> points = new ArrayList<Vector4d>();
		//System.out.println(M);
		points.add(Util.MultiplyVectorAndMatrix(M, Pw));
		//System.out.println("Pw   " + Util.MultiplyVectorAndMatrix(M, Pw));
		return points;
	}
	
	
	
	public static ArrayList<Vector4d> getWorldScreenCoordinatesAntiAliasing(int x, int y, int num) {
		ArrayList<Vector4d> points = new ArrayList<Vector4d>();
		
		for (int i=0; i<num; i++) {
			Vector3d Pc = new Vector3d(x, y, -distance);
			double rU = r.nextDouble() - 0.5;
			double rV = r.nextDouble() - 0.5;
			Util.multiplyVectors(Pc, new Vector3d(Au, Av, 1));
			Pc = Util.add(Pc, new Vector3d(rU*Au, rV*Av, 0));

			Vector4d Pw = new Vector4d(Pc.x, Pc.y, Pc.z, 1);
			points.add(Util.MultiplyVectorAndMatrix(M, Pw));
		}
		
		return points; 
	}
	
}
