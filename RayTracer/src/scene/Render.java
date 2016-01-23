package scene;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * For rendering purpose
 *
 */
public class Render extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Create Render object
	 */
	public Render() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * 
	 * @param bi Info for rendering
	 */
	public Render(BufferedImage bi) {
		this();
		Dimension d = new Dimension(bi.getWidth(), bi.getHeight()); 
		setSize(d);
		setResizable(false);
		ImageIcon image = new ImageIcon(bi);
		add(new JLabel(image));
		setVisible(true);
	}

}