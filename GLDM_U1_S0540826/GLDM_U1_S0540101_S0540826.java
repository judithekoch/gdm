import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.NewImage;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

//erste Uebung (elementare Bilderzeugung)

public class GLDM_U1_S0540101_S0540826 implements PlugIn {

	final static String[] choices = { "Schwarzes Bild", "Gelbes Bild",
			"Schwarz/Weiss Verlauf",
			"Horiz. Schwarz/Rot vert. Schwarz/Blau Verlauf",
			"Italienische Fahne", "Bahamische Fahne", "Japanische Fahne",
			"Japanische Fahne mit weichen Kanten", "Streifenmuster" };

	private String choice;

	public static void main(String args[]) {
		ImageJ ij = new ImageJ(); // neue ImageJ Instanz starten und anzeigen
		ij.exitWhenQuitting(true);

		GLDM_U1_S0540101_S0540826 imageGeneration = new GLDM_U1_S0540101_S0540826();
		imageGeneration.run("");
	}

	public void run(String arg) {

		int width = 566; // Breite
		int height = 400; // Hoehe

		// RGB-Bild erzeugen
		ImagePlus imagePlus = NewImage.createRGBImage("GLDM_U1", width, height,
				1, NewImage.FILL_BLACK);
		ImageProcessor ip = imagePlus.getProcessor();

		// Arrays fuer den Zugriff auf die Pixelwerte
		int[] pixels = (int[]) ip.getPixels();

		dialog();

		// //////////////////////////////////////////////////////////////
		// Hier bitte Ihre Aenderungen / Erweiterungen

		if (choice.equals("Schwarzes Bild")) {

			// Schleife ueber die y-Werte
			for (int y = 0; y < height; y++) {
				// Schleife ueber die x-Werte
				for (int x = 0; x < width; x++) {
					int pos = y * width + x; // Arrayposition bestimmen

					int r = 0;
					int g = 0;
					int b = 0;

					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;
				}
			}
		}

		if (choice.equals("Gelbes Bild")) {

			// loop over every pixel on y axis
			for (int y = 0; y < height; y++) {
				// loop over every pixel on x axis
				for (int x = 0; x < width; x++) {
					int pos = y * width + x; // getting the position at the
												// given time

					int r = 255; // r and g to max will make yellow
					int g = 255;
					int b = 0;

					// returning values into the pixel

					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;

				}

			}

		}

		if (choice.equals("Schwarz/Weiss Verlauf")) {

			// loop over every pixel on y axis
			for (int y = 0; y < height; y++) {
				// loop over every pixel on x axis
				for (int x = 0; x < width; x++) {
					int pos = y * width + x; // getting the position at the
												// given time

					int r = 255 * y / (height - 1);// times 255 and divided by width
													// to stretch on the whole
													// screen("increasing n value")
					int g = 255 * y / (height - 1);
					int b = 255 * y / (height - 1);

					// returning values into the pixel

					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;

				}

			}

		}

		if (choice.equals("Horiz. Schwarz/Rot vert. Schwarz/Blau Verlauf")) {

			// loop over every pixel on y axis
			for (int y = 0; y < height; y++) {
				// loop over every pixel on x axis
				for (int x = 0; x < width; x++) {
					int pos = y * width + x; // getting the position at the
												// given time

					int r = 255 * x / (width - 1);// x is horizontal so increase red
													// value here
					int g = 0;
					int b = 255 * y / (height - 1); // y is vertical so increase
													// blue here

					// returning values into the pixel

					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;

				}

			}

		}

		if (choice.equals("Italienische Fahne")) {

			// loop over every pixel on y axis
			for (int y = 0; y < height; y++) {
				// loop over every pixel on x axis
				for (int x = 0; x < width; x++) {
					int pos = y * width + x; // getting the position at the
												// given time
					int third = width / 3;
					int r = 0;
					int g = 0;
					int b = 0;
					if (x <= third) { // the third that has to become green
						r = 0;
						g = 255;
						b = 0;

					}

					if (x > third && x < third * 2) {// the third that has to be
														// white
						r = 255;
						g = 255;
						b = 255;
					}

					if (x >= third * 2) {// the third that has to be red
						r = 255;
						g = 0;
						b = 0;
					}

					// returning values into the pixel

					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;

				}

			}

		}

		if (choice.equals("Bahamische Fahne")) {

			// loop over every pixel on y axis
			for (int y = 0; y < height; y++) {
				// loop over every pixel on x axis
				for (int x = 0; x < width; x++) {
					int pos = y * width + x; // getting the position at the
					// given time

					int thirdVert = height / 3;
					int r = 127;
					int g = 255;
					int b = 212;

					if (y > thirdVert && y < thirdVert * 2) {// the third that
																// has to be
																// yellow
						r = 255;
						g = 255;
						b = 0;
					}

					// length of the lines of halfed triangle line length
					double seitenLaengenKlein = height / 2;
					// for the upper triangle
					if (y <= seitenLaengenKlein) {

						if (y > x) {
							r = 0;
							g = 0;
							b = 0;

						}

					}
					// for the lower triangle
					if (y >= seitenLaengenKlein) {

						if (x + y <= height) {
							r = 0;
							g = 0;
							b = 0;

						}

					}

					// returning values into the pixel

					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;

				}

			}

		}

		if (choice.equals("Japanische Fahne")) {

			// loop over every pixel on y axis
			for (int y = 0; y < height; y++) {
				// loop over every pixel on x axis
				for (int x = 0; x < width; x++) {
					int pos = y * width + x; // getting the position at the
					// given time

					// diameter of red circle
					int circRad = height * 3 / 5 / 2;
					int midWidth = width / 2;
					int midHeight = height / 2;
					int r = 255;
					int g = 255;
					int b = 255;

					if ((circRad * circRad) > ((x - midWidth) * (x - midWidth) + (y - midHeight)
							* (y - midHeight))) {

						g = 0;
						b = 0;

					}

					// returning values into the pixel

					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;

				}

			}

		}

		if (choice.equals("Japanische Fahne mit weichen Kanten")) {
			int count =1;
			// loop over every pixel on y axis
			for (int y = 0; y < height; y++) {
				// loop over every pixel on x axis
				for (int x = 0; x < width; x++) {
					int pos = y * width + x; // getting the position at the
					// given time

					

					

				
					// diameter of red circle
					int circRad = height * 3 / 5 / 2;
					// 65/100 to be a bit bigger than 3/5 circle
					int circSoft = height * 65 / 100 / 2;
					int midWidth = width / 2;
					int midHeight = height / 2;
					
					int r=0;
					int g=0;
					int b =0;
					
					int xNew = (x-midWidth)*(x-midWidth);
					int yNew = (y-midHeight)*(y-midHeight);
					 
					
					double distance = Math.sqrt(xNew+yNew);

					
					
					r= 255;
					g = ((int)distance-circRad)*(255/((int)Math.sqrt(circSoft*circSoft)-(int)Math.sqrt(circRad*circRad)));
					b = g;
				
					
					
					if ((xNew)+ (yNew) < (circRad * circRad)) {
						r = 255;
						g = 0;
						b = 0;
					}

					else if ((xNew)+ (yNew) > (circSoft * circSoft)) {
						r = 255;
						g = 255;
						b = 255;

					
					}

						
						
					

					// returning values into the pixel

					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;

				
				
			}
			}
			/*	// loop over every pixel on y axis
				for (int y = 0; y < height; y++) {
					// loop over every pixel on x axis
					for (int x = 0; x < width; x++) {
						int pos = y * width + x; // getting the position at the
						// given time

						// check to stay inside the boundaries of the picture
						if (x < width - 1 && y < height - 1 && x > 1 && y > 1) {

							int count = 0;
							int rMean = 0x00000000;
							int rMask = 0x00FF0000;
							int gMean = 0x00000000;
							int gMask = 0x0000FF00;
							int bMean = 0x00000000;
							int bMask = 0x000000FF;

							//topRow of Pixel

							for (int i = 1; i >= -1; i--) {
								count++;
								int rMeanTop = (pixels[((y - 1) * width + x + i)] & rMask) >> 16;
								gMean += (pixels[(y - 1) * width + x + i] & gMask) >> 8;
								bMean += (pixels[(y - 1) * width + x + i] & bMask);
								rMean += rMeanTop;
							} // row of pixel

							for (int i = 1; i >= -1; i--) {
								count++;

								rMean += (pixels[(y) * width + x + i] & rMask) >> 16;
								gMean += (pixels[(y) * width + x + i] & gMask) >> 8;
								bMean += (pixels[(y) * width + x + i] & bMask);
							}

							// botRow of pixel

							for (int i = 1; i >= -1; i--) {
								count++;
								rMean += (pixels[(y + 1) * width + x + i] & rMask) >> 16;
								gMean += (pixels[(y + 1) * width + x + i] & gMask) >> 8;
								bMean += (pixels[(y + 1) * width + x + i] & bMask);

							}

							// assigning the pixel the mean value of the surrounding
							// pixels

							pixels[pos] = 0xFF000000 | ((rMean / count) << 16)
									| ((gMean / count) << 8) | (bMean / count);
						}
					}
				}*/
		}

		if (choice.equals("Streifenmuster")) {
			int count = 0;

			// loop over every pixel on y axis
			for (int y = 0; y < height; y++) {
				// loop over every pixel on x axis
				for (int x = 0; x < width; x++) {
					int pos = y * width + x; // getting the position at the
					// given time

					int eighth = width / 8;

					int r;
					int g;
					int b;

					while (x >= count * eighth)
						count++;
					while (x <= count * eighth)
						count--;

					if (count % 2 != 0) {
						r = 0;
						g = 0;
						b = 0;

					} else {
						r = 255;
						g = 255;
						b = 255;
					}

					// returning values into the pixel

					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) | b;

				}

			}
		}

		// //////////////////////////////////////////////////////////////////

		// neues Bild anzeigen
		imagePlus.show();
		imagePlus.updateAndDraw();
	}

	private void dialog() {
		// Dialog fuer Auswahl der Bilderzeugung
		GenericDialog gd = new GenericDialog("Bildart");

		gd.addChoice("Bildtyp", choices, choices[0]);

		gd.showDialog(); // generiere Eingabefenster

		choice = gd.getNextChoice(); // Auswahl uebernehmen

		if (gd.wasCanceled())
			System.exit(0);
	}
}
