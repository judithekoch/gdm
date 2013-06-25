import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
     Opens an image window and adds a panel below the image
 */
public class Uebung5_s0540101 implements PlugIn {

	ImagePlus imp; // ImagePlus object
	private int[] origPixels;
	private int width;
	private int height;
	

	String[] items = { "Original", "Weichzeichner", "Hochpassgefiltertes Bild", "Verstärkte Kanten" };

	public static void main(String args[]) {

		IJ.open("C:\\Users\\Max\\Documents\\HTW\\GRDL\\sail.jpg");
		//IJ.open("Z:/Pictures/Beispielbilder/orchid.jpg");

		Uebung5_s0540101 pw = new Uebung5_s0540101();
		pw.imp = IJ.getImage();
		pw.run("");
	}

	public void run(String arg) {
		if (imp == null)
			imp = WindowManager.getCurrentImage();
		if (imp == null) {
			return;
		}
		CustomCanvas cc = new CustomCanvas(imp);

		storePixelValues(imp.getProcessor());

		new CustomWindow(imp, cc);
	}

	private void storePixelValues(ImageProcessor ip) {
		width = ip.getWidth();
		height = ip.getHeight();

		origPixels = ((int[]) ip.getPixels()).clone();
	}

	class CustomCanvas extends ImageCanvas {

		CustomCanvas(ImagePlus imp) {
			super(imp);
		}

	} // CustomCanvas inner class

	class CustomWindow extends ImageWindow implements ItemListener {

		private String method;

		CustomWindow(ImagePlus imp, ImageCanvas ic) {
			super(imp, ic);
			addPanel();
		}

		void addPanel() {
			//JPanel panel = new JPanel();
			Panel panel = new Panel();

			JComboBox cb = new JComboBox(items);
			panel.add(cb);
			cb.addItemListener(this);

			add(panel);
			pack();
		}

		public void itemStateChanged(ItemEvent evt) {

			// Get the affected item
			Object item = evt.getItem();

			if (evt.getStateChange() == ItemEvent.SELECTED) {
				System.out.println("Selected: " + item.toString());
				method = item.toString();
				changePixelValues(imp.getProcessor());
				imp.updateAndDraw();
			}

		}

		private void changePixelValues(ImageProcessor ip) {

			// Array zum Zurückschreiben der Pixelwerte
			int[] pixels = (int[]) ip.getPixels();

			if (method.equals("Original")) {

				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int pos = y * width + x;

						pixels[pos] = origPixels[pos];
					}
				}
			}

			if (method.equals("Weichzeichner")) {

				pixels = (weich(pixels));
				

			}

			if (method.equals("Hochpassgefiltertes Bild")) {
				int[] weich = weich(pixels);

				pixels = hochpass(pixels,weich);
				

			}
			
			if (method.equals("Verstärkte Kanten")) {
				
					int[] weich = weich(pixels);
				
					int[] hochPass = hochpass(pixels, weich);
				
					pixels = starkeKanten(pixels, hochPass);

			}
			
			
			
		}
		
		private int[] starkeKanten(int[] intArray, int[] intArray2)
		{
			int[] pixels = intArray;
			for (int y = 0; y < height; y++) 
			{
				for (int x = 0; x < width; x++) 
				{
					int pos = y * width + x;
			
			int argb = origPixels[pos]; // Lesen der Originalwerte 
			int r = (argb >> 16) & 0xff;
			int g = (argb >> 8) & 0xff;
			int b = argb & 0xff;
			
			int argb2 = intArray2[pos];
			int r2 = (argb2 >> 16) & 0xff;
			int g2 = (argb2 >> 8) & 0xff;
			int b2 = argb2 & 0xff;
			
			int rn = r + r2 - 128;
			int gn = g + g2 - 128;
			int bn = b + b2 - 128;

			if (rn < 0)
				rn = 0;
			else if (rn > 255)
				rn = 255;
			if (gn < 0)
				gn = 0;
			else if (gn > 255)
				gn = 255;
			if (bn < 0)
				bn = 0;
			else if (bn > 255)
				bn = 255;

			pixels[pos] = (0xFF << 24) | (rn << 16) | (gn << 8) | bn;
			
				}
			}
			
			return null;
		}

		private int[] hochpass(int[] intArray, int[] intArray2) {
			int[] pixels = intArray;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pos = y * width + x;

					int argb = origPixels[pos]; // Lesen der Originalwerte 
					int r = (argb >> 16) & 0xff;
					int g = (argb >> 8) & 0xff;
					int b = argb & 0xff;

					int argb2 = intArray2[pos];
					int r2 = (argb2 >> 16) & 0xff;
					int g2 = (argb2 >> 8) & 0xff;
					int b2 = argb2 & 0xff;

					int rn = r - r2 + 128;
					int gn = g - g2 + 128;
					int bn = b - b2 + 128;

					if (rn < 0)
						rn = 0;
					else if (rn > 255)
						rn = 255;
					if (gn < 0)
						gn = 0;
					else if (gn > 255)
						gn = 255;
					if (bn < 0)
						bn = 0;
					else if (bn > 255)
						bn = 255;

					pixels[pos] = (0xFF << 24) | (rn << 16) | (gn << 8) | bn;
				}
			}
			return pixels;
		}

		private int[] weich(int[] intArray) {

			int[] pixels = intArray;

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pos = y * width + x;
					 

					int rn = 0;
					int gn = 0;
					int bn = 0;
					int origX = x;
					int origY = y;

					int count = 0;

					// the two for-loops get all the values in a 3x3 square based on pos as the middle
					for (int xN = -1; xN <= 1; xN++) {

						for (int yN = 0 - width; yN <= width; yN = yN + width) {
							// help variables to set back the yN and xN to original after the fi statements
							int yNNew = yN;
							int xNNew = xN;

							// if statements for the pixels at the border, setting the value to the pixelvalue of the opposite site

							if (xN == -1 && x == 0)
								xN = width - 1;
							if (xN == 1 && x == width - 1)
								xN = -1 * (width - 1);
							if (yN == -1 * width && y == 0)
								yN = (height - 1) * (width);
							if (yN == width && y == height - 1)
								xN = -1 * (height - 1) * width;

							int posNew = pos + xN + yN;

							rn += (origPixels[posNew] >> 16) & 0xff;
							gn += (origPixels[posNew] >> 8) & 0xff;
							bn += (origPixels[posNew]) & 0xff;

							count++;

							xN = xNNew;
							yN = yNNew;
						}
					}

					rn = rn / count;
					gn = gn / count;
					bn = bn / count;

					if (rn < 0)
						rn = 0;
					else if (rn > 255)
						rn = 255;
					if (gn < 0)
						gn = 0;
					else if (gn > 255)
						gn = 255;
					if (bn < 0)
						bn = 0;
					else if (bn > 255)
						bn = 255;

					pixels[pos] = (0xFF << 24) | (rn << 16) | (gn << 8) | bn;

					y = origY;
					x = origX;

				}
			}
			return pixels;
		}

	} // CustomWindow inner class
}