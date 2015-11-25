package objects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

public class Model {

	private ArrayList<Shape> triangles;
	private ArrayList<Vector2d> pxTextures;
	private BufferedImage textures;
	private int numPixelsX;
	private int numPixelsY;

	public Model(String imgPath, String texturesPath) {

		triangles = new ArrayList<Shape>();
		try {
			textures = ImageIO.read(new File(texturesPath));
		} catch (IOException e) {
			System.out.println("Imagen chunga");
			e.printStackTrace();
		}

		numPixelsX = textures.getWidth();
		numPixelsY = textures.getHeight();

		createTriangles(imgPath);
	}

	public ArrayList<Shape> getTriangles() {
		return triangles;
	}

	public void createTriangles(String imgPath) {
		Scanner s;
		try {
			s = new Scanner(new File(imgPath));

			Scanner sLine = null;
			ArrayList<Vector3d> vertex = new ArrayList<Vector3d>();
			pxTextures = new ArrayList<Vector2d>();

			int i = 0;
			while (s.hasNextLine()) {
				String line = s.nextLine();

				if (line.startsWith("vt ")) { // Load textures
					sLine = new Scanner(line);
					sLine.next();

					Vector2d p = new Vector2d(Double.parseDouble(sLine.next()), Double.parseDouble(sLine.next()));
					pxTextures.add(p);
				} else if (line.startsWith("v ")) { // Load vertex
					sLine = new Scanner(line);
					sLine.next();

					Vector3d p = new Vector3d(Double.parseDouble(sLine.next()), Double.parseDouble(sLine.next()),
							Double.parseDouble(sLine.next()));
					vertex.add(p);
				} else if (line.startsWith("f ")) { // Load faces
					sLine = new Scanner(line);
					sLine.next();

					String[] x = sLine.next().split("/");
					String[] y = sLine.next().split("/");
					String[] z = sLine.next().split("/");
					String[] w = sLine.next().split("/");

					Vector3d p1 = new Vector3d(Double.parseDouble(x[0]), Double.parseDouble(y[0]),
							Double.parseDouble(z[0]));
					Vector3d p2 = new Vector3d(Double.parseDouble(x[0]), Double.parseDouble(z[0]),
							Double.parseDouble(w[0]));

//					int p1Color = (Integer.parseInt(x[1]) + Integer.parseInt(y[1]) + Integer.parseInt(z[1]))/3;
//					int p2Color = (Integer.parseInt(x[1]) + Integer.parseInt(z[1]) + Integer.parseInt(w[1]))/3;

					// TODO: obtener el color en función del punto de intersección del rayo con el triángulo
						// Guardar las 3 coordenadas de color para cada triángulo
					
					// Gets the color from the x vertex
					triangles.add(new Triangle(vertex.get((int) p1.x - 1), vertex.get((int) p1.y - 1),
							vertex.get((int) p1.z - 1), 1, getTextureColor(Integer.parseInt(x[1]))));
					triangles.add(new Triangle(vertex.get((int) p2.x - 1), vertex.get((int) p2.y - 1),
							vertex.get((int) p2.z - 1), 1, getTextureColor(Integer.parseInt(x[1]))));
				}
			}

			System.out.println("vertex: " + vertex.size());
			System.out.println("colors " + pxTextures.size());
			System.out.println("Triangles " + triangles.size());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the color for the specified texture data 'x'
	 */
	private Color getTextureColor(int x) {

		Vector2d p = pxTextures.get(x);
		double px = numPixelsX * p.x;
		double py = (int) numPixelsY * p.y;

		int color = textures.getRGB((int) px, (int) py);
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color & 0xFF);

		return new Color(r, g, b);
	}

}
