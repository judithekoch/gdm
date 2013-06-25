package u6;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.NewImage;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;


public class Scale_S0540826 implements PlugInFilter {

	ImagePlus imp;
	float ratioH = 0;
	float ratioV = 0;
	float ratio = 0;
	float v;
	float h;
	int countH = 0;
	int countW = 0;

	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about"))
		{showAbout(); return DONE;}
		return DOES_RGB+NO_CHANGES;
		// kann RGB-Bilder und veraendert das Original nicht
	}

	public static void main(String args[]) {

		IJ.open("/applications/ImageJ/plugins/gdm/GLDM_S0540826/component.jpg");
		//IJ.open("Z:/Pictures/Beispielbilder/orchid.jpg");

		Scale_S0540826 pw = new Scale_S0540826();
		pw.imp = IJ.getImage();
		ImageProcessor ip = pw.imp.getProcessor();
		pw.run(ip);
	}

	public void run(ImageProcessor ip) {

		String[] dropdownmenue = {"Kopie", "Pixelwiederholung", "Bilinear"};

		GenericDialog gd = new GenericDialog("scale");
		gd.addChoice("Methode",dropdownmenue,dropdownmenue[0]);
		gd.addNumericField("Hoehe:",500,0);
		gd.addNumericField("Breite:",500,0);

		gd.showDialog();

		int methode = 0;		
		String s = gd.getNextChoice();
		if (s.equals("Kopie")) methode = 1;
		if (s.equals("Pixelwiederholung")) methode = 2;
		if (s.equals("Bilinear")) methode = 3;

		int height_n = (int)gd.getNextNumber(); // _n fuer das neue skalierte Bild
		int width_n =  (int)gd.getNextNumber();

		int width  = ip.getWidth();  // Breite bestimmen
		int height = ip.getHeight(); // Hoehe bestimmen

		ImagePlus neu = NewImage.createRGBImage("Skaliertes Bild",
				width_n, height_n, 1, NewImage.FILL_BLACK);

		ImageProcessor ip_n = neu.getProcessor();


		int[] pix = (int[])ip.getPixels();
		int[] pix_n = (int[])ip_n.getPixels();

		// Verhaeltnisse der alten zu den neuen Werten
		ratioH = (float)height_n/(float)height; 
		ratioV = (float)width_n/(float)width; 
		ratio = (ratioH>ratioV) ? ratioH : ratioV;

		// Schleife ueber das neue Bild

		for (int y_n=0; y_n<height_n; y_n++) {
			for (int x_n=0; x_n<width_n; x_n++) {
				int y = y_n;
				int x = x_n;

				if (methode == 1){ //Kopie

					if (y < height && x < width) {
						int pos_n = y_n*width_n + x_n;
						int pos  =  y  *width   + x;

						pix_n[pos_n] = pix[pos];
					}
				}

				if (methode == 2){ //Pixelwiederholung
					if (ratio >= 1 && y < height*ratio && x < width*ratio) { //Bereich begrenzen
						int pos_n = y_n*width_n + x_n;
						int pos = (int)(y_n/ratio) * width + (int)(x_n/ratio);

						pix_n[pos_n] = pix[pos];
					}
				}

				if (methode == 3){	//Interpolation				
					if (y < height*ratio && x < width*ratio) { //Bereich begrenzen
						
						// horizontaler und vertikaler Abstand vom neuen Pixel zu den Alten
						h = (float)(x_n/ratioV) % 1;
						v = (float)(y_n/ratioH) % 1;
						
						int pos_n = y_n*width_n + x_n;
						int pos = (int)(y_n/ratio) * width + (int)(x_n/ratio);
						
						int A = pix[pos];
						float rA = (A >> 16) & 0xff;
						float gA = (A >> 8) & 0xff;
						float bA = A & 0xff;
						
						int B = pix[pos + 1];
						float rB = (B >> 16) & 0xff;
						float gB = (B >> 8) & 0xff;
						float bB = B & 0xff;
						
						int C; 
						if(pos+width+1 < pix.length)
							C = pix[pos + width];
						else 
							C = 0;
						float rC = (C >> 16) & 0xff;
						float gC = (C >> 8) & 0xff;
						float bC = C & 0xff;
						
						int D;
						if(pos+width+1 < pix.length)
							D = pix[pos + width + 1];
						else
							D = 0;
						float rD = (D >> 16) & 0xff;
						float gD = (D >> 8) & 0xff;
						float bD = D & 0xff;
						
						int r = (int)(rA*(1-h)*(1-v) + rB*h*(1-v) + rC*(1-h)*v + rD*h*v); 
						int g = (int)(gA*(1-h)*(1-v) + gB*h*(1-v) + gC*(1-h)*v + gD*h*v); 
						int b = (int)(bA*(1-h)*(1-v) + bB*h*(1-v) + bC*(1-h)*v + bD*h*v); 
						
						if (r < 0)
							r = 0;
						else if (r > 255)
							r = 255;
						if (g < 0)
							g = 0;
						else if (g > 255)
							g = 255;
						if (b < 0)
							b = 0;
						else if (b > 255)
							b = 255;
						
						pix_n[pos_n] = (0xFF << 24) | (r << 16) | (g << 8) | b;
					}
				}
			}
		}

		// neues Bild anzeigen
		neu.show();
		neu.updateAndDraw();
	}

	void showAbout() {
		IJ.showMessage("");
	}
}
