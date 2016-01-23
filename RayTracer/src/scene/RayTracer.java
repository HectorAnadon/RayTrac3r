package scene;

import java.awt.Color;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import objects.Shape;

/**
 * RayTracer object
 *
 */
public class RayTracer {
	
	/**
	 * 
	 * @param imgColor previous color
	 * @param provColor new color
	 * @return A normalized Color adding imgColor and provColor
	 */
	private static Color normalizeColor(Color imgColor, Color provColor) {
		int r = imgColor.getRed() + provColor.getRed();
		int g = imgColor.getGreen() + provColor.getGreen();
		int b = imgColor.getBlue() + provColor.getBlue();
		int max = Math.max(r, Math.max(g, b));
		if (max > 255) {
			r = (255*r)/max;
			g = (255*g)/max;
			b = (255*b)/max;
		}
		return new Color(r,g,b);
	}
	
	/**
	 * 
	 * @param r Ray
	 * @param objects Objects in the scene
	 * @param lights Lights in the scene
	 * @param ambientalLightI Intensity of ambientalLight
	 * @param ew Camera position
	 * @param raysReflectedReaming Rays remaining for reflection
	 * @param raysRefractedReaming Rays remaining for refraction
	 * @param maxReflected Maximum rays reflected
	 * @param toIgnore Objects you shouldn´t consider in the scene
	 * @return A color given by r ray
	 */
	public static Color traceRay(Ray r,ArrayList<Shape> objects, ArrayList<Light> lights, double ambientalLightI, Vector3d ew,
			int raysReflectedReaming, int raysRefractedReaming, int maxReflected, ArrayList<Shape> toIgnore) {
		Shape object = null;
		double minDistance = Double.POSITIVE_INFINITY;
		Ray rReflected = null;
		
		Color imgColor = new Color(0,0,0);
		Color reflectedColor = null;
		Color refractedColor = null;
		
		for (Shape obj:objects) {	// Intersect ray with each objects
			if (!toIgnore.contains(obj)) {
				Ray currentReflected = obj.intersection(r);
				
				if (currentReflected != null) {
					double distance = Util.distance(ew, currentReflected.position);
					 if (distance < minDistance) {
						 object = obj;
						 minDistance = distance; // update min distance
						 rReflected = currentReflected;
					 }
					//System.out.println(i + "  -  " + j);
					// TO DO:
						// 1. Calculate intersection between light and intersection point
						// 2. Calculate intersection between other objects with the reflected ray
				}
			}
		}
				
		//Only for one object:
		// AMBIANTAL LIGHT + DIFUSSE
		if (object != null) {
			//get ambiental light
			if (raysReflectedReaming == maxReflected) {
				imgColor = object.getColor(ambientalLightI);
			}
			
			for (Light l:lights) {
				//Ray from object to light
				Ray rLight = new Ray(rReflected.position, l.getPosition(),1);
				boolean intersects = false;
				double shadowOpacity = 0.0;
				for (Shape obj2:objects) {
					if (!obj2.equals(object)){
						//Ray from object in the middle
						Ray rReflected2 = obj2.intersection(rLight);
						//sometimes intersections it shouldnt
						if (rReflected2 != null &&
								(between(rReflected2.position, l.getPosition(),rReflected.position))) {
							/*System.out.println("position object: " + rReflected.position);
							System.out.println("position in the middle:" + rReflected2.position);
							System.out.println("position light: "+l.getPosition());
							*/
							intersects = true;
							if (obj2.opaque > shadowOpacity) {
								shadowOpacity += obj2.opaque;
							}
						}
					}
				}
				if(!intersects) {		// Calculate color without shadow
					toIgnore.add(object);
					Color difusa = object.getColor(r.intensity,rLight,l.getIntensity());
					imgColor = normalizeColor(imgColor, difusa);
					Ray rLightReflected = object.intersection(rLight);
					if (rLightReflected != null) {
						Color especular = object.getColor(r.intensity,rLight,rLightReflected,r, l.getIntensity());
						imgColor = normalizeColor(imgColor, especular);
						//Test especular
						//imgColor =object.getColor(l.getIntensity(),rLight,rLightReflected,r);
					} else{
						//Test especular
						//imgColor = new Color(0,0,0);
					}
					//Test difuso
					//imgColor =object.getColor(l.getIntensity(),rLight);
				} else {
					if (shadowOpacity < 1.0) {
						Color shadow = object.getColor(r.intensity*(1-shadowOpacity),rLight, l.getIntensity());
						imgColor = normalizeColor(imgColor, shadow);
					}
				}
			}		// End of lights
			

			if (raysRefractedReaming > 0 && object.opaque < 1) {
				refractedColor = traceRay(object.getRefraction(r, rReflected.position), objects, lights, ambientalLightI, ew,
						raysReflectedReaming, raysRefractedReaming -1, maxReflected, toIgnore);
				if (refractedColor != null) {
					refractedColor = new Color((int) (refractedColor.getRed()), 
							(int) (refractedColor.getGreen()), (int) (refractedColor.getBlue()));
					imgColor = normalizeColor(imgColor, refractedColor);
				}
				else {
					raysRefractedReaming = 0;
				}
			}
			
			// Color reflected
			if (raysReflectedReaming > 0 && object.kr > 0) {
//				reflectedColor = traceRay(rReflected, raysReaming - 1);
				rReflected.inverseDirection();
				reflectedColor = traceRay(new Ray(rReflected.position, rReflected.direction,rReflected.intensity), 
						objects, lights, ambientalLightI, ew, raysReflectedReaming - 1, 
						raysRefractedReaming,maxReflected, toIgnore);
				if (reflectedColor != null) {
					reflectedColor = new Color((int) (object.kr*reflectedColor.getRed()), 
							(int) (object.kr*reflectedColor.getGreen()), (int) (object.kr*reflectedColor.getBlue()));
					imgColor = normalizeColor(imgColor, reflectedColor);
				}
				else {
					raysReflectedReaming = 0;
				}
			}
		
			
		}	// End of object != null
		
		return imgColor;

	}

	/**
	 * 
	 * @param b point
	 * @param a point
	 * @param c point
	 * @return True if b is between a and c
	 */
	private static boolean between(Vector3d b, Vector3d a, Vector3d c) {
		if (Math.floor((Util.distance(a, b) + Util.distance(c, b))*100)/100 == Math.floor(Util.distance(a, c)*100)/100)
		    return true;
		return false;
	}

}
