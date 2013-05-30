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
public class Uebung5_s0540101
		implements
		PlugIn {

	ImagePlus imp; // ImagePlus object
	private int[] origPixels;
	private int width;
	private int height;
	

	String[] items = { "Original", "Weichzeichner", "Hochpassgefiltertes Bild"};

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

	class CustomCanvas
			extends
			ImageCanvas {

		CustomCanvas(ImagePlus imp) {
			super(imp);
		}

	} // CustomCanvas inner class

	class CustomWindow
			extends
			ImageWindow
			implements
			ItemListener {

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

				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int pos = y * width + x;
						int argb = origPixels[pos]; // Lesen der Originalwerte 

						int r = (argb >> 16) & 0xff;
						int g = (argb >> 8) & 0xff;
						int b = argb & 0xff;

						int rn = r;
						int gn = g;
						int bn = b;

						if (x > 0 && y > 0 && x < width - 1 && y < height - 1) {
							int count = 0;

							// the two for-loops get all the values in a 3x3 square based on pos as the middle
							for (int xN = -1; xN <= 1; xN++) {

								for (int yN = 0 - width; yN <= width; yN = yN + width) {

									rn += (origPixels[pos + xN + yN] >> 16) & 0xff;
									gn += (origPixels[pos + xN + yN] >> 8) & 0xff;
									bn += (origPixels[pos + xN + yN]) & 0xff;

									count++;
								}
							}

							rn = rn / count;
							gn = gn / count;
							bn = bn / count;
						}

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
			}
			
			
			
			
			

		}

	} // CustomWindow inner class
}