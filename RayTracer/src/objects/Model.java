package objects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;


/**
 * Represent a complex object formed by triangles and optionally textures 
 */

public class Model {

	private ArrayList<Shape> triangles;
	private ArrayList<Vector2d> pxTextures;
	private BufferedImage textures;
	private int numPixelsX;
	private int numPixelsY;
	private Matrix4d transformation;
	private boolean isTextures;
	
	private double kr = 0;		// reflection coefficient
	private double kn = 1;		// refraction coefficient
	private double opaque = 1.0;
	
	private Color color = new Color(255,255,255);
	private String imgPath;

	/**
	 * Creates a new Model
	 * @param imgPath Triangles
	 * @param texturesPath	Textures
	 */
	public Model(String imgPath, String texturesPath) {

		triangles = new ArrayList<Shape>();
		this.imgPath = imgPath;
		if(texturesPath.equals("")) {
			isTextures = false;
		}
		else {
			isTextures = true;
			try {
				textures = ImageIO.read(new File(texturesPath));
			} catch (IOException e) {
				System.out.println("Imagen chunga");
				e.printStackTrace();
			}
			numPixelsX = textures.getWidth();
			numPixelsY = textures.getHeight();
		}

	}
	
	/**
	 * Creates a new Model modified with the transformation matrix
	 * @param imgPath	Triangles
	 * @param texturesPath	Textures
	 * @param transformation	Transformation matrix
	 */
	public Model(String imgPath, String texturesPath, Matrix4d transformation) {

		triangles = new ArrayList<Shape>();
		this.imgPath = imgPath;
		this.transformation = transformation;
		if(texturesPath.equals("")) {
			isTextures = false;
		}
		else {
			isTextures = true;
			try {
				textures = ImageIO.read(new File(texturesPath));
				numPixelsX = textures.getWidth();
				numPixelsY = textures.getHeight();
			} catch (IOException e) {
				System.out.println("Imagen chunga");
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * @return triangles
	 */
	public ArrayList<Shape> getTriangles() {
		return triangles;
	}

	/**
	 * @return ArrayList with triangles who compose the image
	 */
	public ArrayList<Shape> createTriangles() {
		Scanner s;
		try {
			s = new Scanner(new File(imgPath));

			Scanner sLine = null;
			ArrayList<Vector3d> vertex = new ArrayList<Vector3d>();
			pxTextures = new ArrayList<Vector2d>();

			int i = 0;
			while (s.hasNextLine()) {
				String line = s.nextLine();

				if (isTextures && line.startsWith("vt ")) { // Load textures
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

						// Guardar las 3 coordenadas de color para cada triángulo
					
					// Gets the color from the x vertex
					
					Triangle t1;
					Triangle t2;
					if (isTextures) {
						Color c1 = getMeanColor(getTextureColor(Integer.parseInt(x[1])-1), getTextureColor(Integer.parseInt(y[1])-1), getTextureColor(Integer.parseInt(z[1])-1));
						Color c2 = getMeanColor(getTextureColor(Integer.parseInt(x[1])-1), getTextureColor(Integer.parseInt(z[1])-1), getTextureColor(Integer.parseInt(w[1])-1));
						
						t1 = new Triangle(vertex.get((int) p1.x - 1), vertex.get((int) p1.y - 1),
								vertex.get((int) p1.z - 1), 1, c1);
						t2 = new Triangle(vertex.get((int) p2.x - 1), vertex.get((int) p2.y - 1),
								vertex.get((int) p2.z - 1), 1, c2);
					}
					else {
						t1 = new Triangle(vertex.get((int) p1.x - 1), vertex.get((int) p1.y - 1),
								vertex.get((int) p1.z - 1), 1, new Color(255,255,255));
						t2 = new Triangle(vertex.get((int) p2.x - 1), vertex.get((int) p2.y - 1),
								vertex.get((int) p2.z - 1), 1, new Color(255,255,255));
					}
					
					
					if (transformation != null) {
						t1.transformation(transformation);
						t2.transformation(transformation);
					}
					triangles.add(t1);
					triangles.add(t2);
				}
			}
			
			for (Shape t1:triangles) {
				t1.setKn(kn);
				t1.setKr(kr);
				t1.setOpaque(opaque);
			}

			System.out.println("vertex: " + vertex.size());
			System.out.println("colors " + pxTextures.size());
			System.out.println("Triangles " + triangles.size());

			Double minX = Double.POSITIVE_INFINITY;
			Double minY = Double.POSITIVE_INFINITY;
			Double minZ = Double.POSITIVE_INFINITY;
			Double maxX = Double.NEGATIVE_INFINITY;
			Double maxY = Double.NEGATIVE_INFINITY;
			Double maxZ = Double.NEGATIVE_INFINITY;
			for (Vector3d v:vertex) {
				if (v.x > maxX) {
					maxX = v.x;
				}
				if (v.y > maxY) {
					maxY = v.y;
				}
				if (v.y > maxZ) {
					maxZ = v.z;
				}
				if (v.x < minX) {
					minX = v.x;
				}
				if (v.x < minY) {
					minY = v.y;
				}
				if (v.x < minY) {
					minZ = v.z;
				}
			}
			System.out.println("Min: " + minX + " " + minY + " " + minZ + " " );
			System.out.println("Max: " + maxX + " " + maxY + " " + maxZ + " " );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return triangles;
	}
	
	/**
	 * @param c1	Color1
	 * @param c2	Color2
	 * @param c3	Color3
	 * @return	Average Color between the parameters
	 */
	private Color getMeanColor(Color c1, Color c2, Color c3) {
		return new Color((c1.getRed()+c2.getRed()+c3.getRed())/3,(c1.getGreen()+c2.getGreen()+c3.getGreen())/3, (c1.getBlue()+c2.getBlue()+c3.getBlue())/3);
	}

	/**
	 * @param x	Texture's number
	 * @return	The color for the specified texture data 'x'
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

	/**
	 * @param kr Reflection coefficient 
	 */
	public void setKr(double kr) {
		this.kr = kr;
	}

	/**
	 * @param kn	Material coefficient
	 */
	public void setKn(double kn) {
		this.kn = kn;
	}

	/**
	 * @param color	Color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	

}
