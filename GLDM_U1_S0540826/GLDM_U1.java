import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.NewImage;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

//erste Uebung (elementare Bilderzeugung)

public class GLDM_U1 implements PlugIn {
	
	final static String[] choices = {
		"Schwarzes Bild",
		"Gelbes Bild",
		"Schwarz/Weiss Verlauf",
		"Horiz. Schwarz/Rot vert. Schwarz/Blau Verlauf",
		"Italienische Fahne",
		"Bahamische Fahne",
		"Japanische Fahne",
		"Japanische Fahne mit weichen Kanten",
		"Streifen",
		"JF"
	};
	
	private String choice;
	
	public static void main(String args[]) {
		ImageJ ij = new ImageJ(); // neue ImageJ Instanz starten und anzeigen 
		ij.exitWhenQuitting(true);
		
		GLDM_U1 imageGeneration = new GLDM_U1();
		imageGeneration.run("");
	}
	
	public void run(String arg) {
		
		int width  = 566;  // Breite
		int height = 400;  // Hoehe
		
		// RGB-Bild erzeugen
		ImagePlus imagePlus = NewImage.createRGBImage("GLDM_U1", width, height, 1, NewImage.FILL_BLACK);
		ImageProcessor ip = imagePlus.getProcessor();
		
		// Arrays fuer den Zugriff auf die Pixelwerte
		int[] pixels = (int[])ip.getPixels();
		
		dialog();
		
		////////////////////////////////////////////////////////////////
		// Hier bitte Ihre Aenderungen / Erweiterungen
		
		if ( choice.equals("Schwarzes Bild") ) {
			
			// Schleife ueber die y-Werte
			for (int y=0; y<height; y++) {
				// Schleife ueber die x-Werte
				for (int x=0; x<width; x++) {
					int pos = y*width + x; // Arrayposition bestimmen
					
					int r = 0;
					int g = 0;
					int b = 0;
					
					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
				}
			}
		}
		
		if ( choice.equals("Gelbes Bild") ) {
			
			// Schleife ueber die y-Werte
			for (int y=0; y<height; y++) {
				// Schleife ueber die x-Werte
				for (int x=0; x<width; x++) {
					int pos = y*width + x; // Arrayposition bestimmen
					
					//gelb mischen
					int r = 255;
					int g = 255;
					int b = 0;
					
					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
				}
			}
		}
		
		if ( choice.equals("Schwarz/Weiss Verlauf") ) {
			int r = 0;
			int g = 0;
			int b = 0;			
			// Schleife ueber die y-Werte
			for (int y=0; y<height; y++) {

				// Schleife ueber die x-Werte
				for (int x=0; x<width; x++) {
					int pos = y*width + x; // Arrayposition bestimmen
					
					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
				}
				
				//nach jeder Reihe, Graustufe um 1 erhellen
				//r += 1;
				//g += 1;
				//b += 1;
				r = 255*y/(height-1);
				g = 255*y/(height-1);
				b = 255*y/(height-1);
			}
		}
		
		if ( choice.equals("Horiz. Schwarz/Rot vert. Schwarz/Blau Verlauf") ) {
			int r = 0;
			int g = 0;
			int b = 0;
			// Schleife ueber die y-Werte
			for (int y=0; y<height; y++) {
				
				// Schleife ueber die x-Werte
				for (int x=0; x<width; x++) {
					int pos = y*width + x; // Arrayposition bestimmen
					
					// Blauwert in der Vertikalen erhoehen 
					b +=1;
					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
				}
				
				// Rotwert in der Horizontalen erhoehen und blau in der neuen Zeile zuruecksetzen 
				r += 1;
				b = 0;
			}
		}
		
		if ( choice.equals("Italienische Fahne") ) {
			
			int r = 0;
			int g = 0;
			int b = 0;
			
			// Schleife ueber die y-Werte
			for (int y=0; y<height; y++) {				
				// Schleife ueber die x-Werte
				for (int x=0; x<width; x++) {
					int pos = y*width + x; // Arrayposition bestimmen
					
					// Pruefen, in welchen Farbfenster der Pixel ist
					if (x<(width/3)){
						r = 0;
						g = 255;
						b = 0;
					} else if (x<(width/3*2)){
						r = 255;
						g = 255;
						b = 255;
					} else {
						r = 255;
						g = 0;
						b = 0;
					}
					
					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
				}
			}
		}
		if ( choice.equals("Bahamische Fahne") ) {
			
			int r = 0;
			int g = 0;
			int b = 0;
			
			// Variable "zaehlt" die zu zeichnenden schwarzen Pixel 
			int black = 0;
			
			// Schleife ueber die y-Werte
			for (int y=0; y<height; y++) {
				
				// Schleife ueber die x-Werte
				for (int x=0; x<width; x++) {
					int pos = y*width + x; // Arrayposition bestimmen
					
					// Ueberprufen, ob Teil des Dreiecks
					if(x<=black) {
						r = 0;
						g = 0;
						b = 0;	
					} else if(y<(height/3)) {
						r = 0;
						g = 0;
						b = 255;
					} else if (y<(height/3*2)) {
						r = 255;
						g = 255;
						b = 0;
					} else {
						r = 0;
						g = 0;
						b = 255;
					}
					
					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
				}
				
				// Zaehler Dreieck verwalten 
				if(y < (height/2)){
					black++;
				} else {
					black--;
				}
			}
		}
		
		if ( choice.equals("Japanische Fahne") ) {
			
			int r = 255;
			int g = 255;
			int b = 255;
			
			// Schleife ueber die y-Werte
			for (int y=0; y<height; y++) {
				// Schleife ueber die x-Werte
				for (int x=0; x<width; x++) {
					int pos = y*width + x; // Arrayposition bestimmen
					
					g = 255;
					b = 255;
					
					/* Sextant
					 * if (y>(height/5) && y<(height/5*4)){
						if (x>((width/2)-red) && x<((width/2)+red)){
							r = 255;
							g = 0;
							b = 0;
						}
					}*/
					
					/* Square 
					if (y>(height/2-30) && x>(width/2-30) && y<(height/2+30) && x<(width/2+30)){
						r = 255;
						g = 0;
						b = 0;
					}*/
					
					/* Circle */
					int xMidPoint = width/2;
					int yMidPoint = height/2;
					int radius = 100;
					if ((xMidPoint-x)*(xMidPoint-x) + (yMidPoint-y)*(yMidPoint-y) < radius*radius){
						r = 255;
						g = 0;
						b = 0;
					}
					
					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
				}
			}
		}
		
		if ( choice.equals("Japanische Fahne mit weichen Kanten") ) {
			
			int r = 255;
			int g = 255;
			int b = 255;
			
			// Schleife ueber die y-Werte
			for (int y=0; y<height; y++) {
				// Schleife ueber die x-Werte
				for (int x=0; x<width; x++) {
					int pos = y*width + x; // Arrayposition bestimmen
					
					r = 255;
					g = 255;
					b = 255;
					
					int xMidPoint = width/2;
					int yMidPoint = height/2;
					int radius = 100;
					int oRadius = 120;
					if ((xMidPoint-x)*(xMidPoint-x) + (yMidPoint-y)*(yMidPoint-y) < radius*radius) {
						r = 255;
						g = 0;
						b = 0;
					}
					
					/* "Gradient" */
					 /*else if ((xMidPoint-x-1)*(xMidPoint-x-1) + (yMidPoint-y-1)*(yMidPoint-y-1) < radius*radius || (xMidPoint-x+1)*(xMidPoint-x+1) + (yMidPoint-y+1)*(yMidPoint-y+1) < radius*radius || (xMidPoint-x-1)*(xMidPoint-x-1) + (yMidPoint-y+1)*(yMidPoint-y+1) < radius*radius || (xMidPoint-x+1)*(xMidPoint-x+1) + (yMidPoint-y-1)*(yMidPoint-y-1) < radius*radius) {
					 
						r = 255;
						g = 50;
						b = 50;
					} else if ((xMidPoint-x-2)*(xMidPoint-x-2) + (yMidPoint-y-2)*(yMidPoint-y-2) < radius*radius || (xMidPoint-x+2)*(xMidPoint-x+2) + (yMidPoint-y+2)*(yMidPoint-y+2) < radius*radius || (xMidPoint-x-2)*(xMidPoint-x-2) + (yMidPoint-y+2)*(yMidPoint-y+2) < radius*radius || (xMidPoint-x+2)*(xMidPoint-x+2) + (yMidPoint-y-2)*(yMidPoint-y-2) < radius*radius) {
						r = 255;
						g = 100;
						b = 100;
					} else if ((xMidPoint-x-3)*(xMidPoint-x-3) + (yMidPoint-y-3)*(yMidPoint-y-3) < radius*radius || (xMidPoint-x+3)*(xMidPoint-x+3) + (yMidPoint-y+3)*(yMidPoint-y+3) < radius*radius || (xMidPoint-x-3)*(xMidPoint-x-3) + (yMidPoint-y+3)*(yMidPoint-y+3) < radius*radius || (xMidPoint-x+3)*(xMidPoint-x+3) + (yMidPoint-y-3)*(yMidPoint-y-3) < radius*radius) {
						r = 255;
						g = 150;
						b = 150;
					} else if ((xMidPoint-x-4)*(xMidPoint-x-4) + (yMidPoint-y-4)*(yMidPoint-y-4) < radius*radius || (xMidPoint-x+4)*(xMidPoint-x+4) + (yMidPoint-y+4)*(yMidPoint-y+4) < radius*radius || (xMidPoint-x-4)*(xMidPoint-x-4) + (yMidPoint-y+4)*(yMidPoint-y+4) < radius*radius || (xMidPoint-x+4)*(xMidPoint-x+4) + (yMidPoint-y-4)*(yMidPoint-y-4) < radius*radius) {
						r = 255;
						g = 200;
						b = 200;
					} else if ((xMidPoint-x-5)*(xMidPoint-x-5) + (yMidPoint-y-5)*(yMidPoint-y-5) < radius*radius || (xMidPoint-x+5)*(xMidPoint-x+5) + (yMidPoint-y+5)*(yMidPoint-y+5) < radius*radius || (xMidPoint-x-5)*(xMidPoint-x-5) + (yMidPoint-y+5)*(yMidPoint-y+5) < radius*radius || (xMidPoint-x+5)*(xMidPoint-x+5) + (yMidPoint-y-5)*(yMidPoint-y-5) < radius*radius) {
						r = 255;
						g = 250;
						b = 250;
					}*/
					
					/*else if ((xMidPoint-x)*(xMidPoint-x) + (yMidPoint-y)*(yMidPoint-y) > oRadius*oRadius){
						r = 255;
						g = 255;
						b = 255;
					} else {
						double ptRadius = Math.sqrt((xMidPoint-x)*(xMidPoint-x) + (yMidPoint-y)*(yMidPoint-y));
						double innerRadius = ;
						double outerRadius = Math.sqrt((xMidPoint-outerRadius/2)*(xMidPoint-outerRadius/2) + (yMidPoint-outerRadius/2)*(yMidPoint-outerRadius/2));
						r = 255;
						g = (int)gradient/(outerRadius)*255;
						b = (int)gradient/(outerRadius)*255;
					}*/
					
					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
				}
			}
		}
		
		if ( choice.equals("Streifen") ) {
			
			int r = 255;
			int g = 255;
			int b = 255;
			int stripe = height/8;
			int counter = 1;
			
			// Schleife ueber die y-Werte
			for (int y=0; y<height; y++) {
				
				if(y < stripe*counter || y > stripe*2*counter){
					r = 255;
					g = 255;
					b = 255;
				} else {
					r = 0;
					g = 0;
					b = 0;	
				}
								
				if (y%2==1 || y%2==0){
					counter++;
				}
				
				// Schleife ueber die x-Werte
				for (int x=0; x<width; x++) {
					int pos = y*width + x; // Arrayposition bestimmen

					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
				}
			}
		}

		if ( choice.equals("JF") ) { //von Tobi
		//private int[] _chineseGradientFlag(int height, int width, int[] pixels){
			//mx and my are the center point of height and width
			float mx = width / 2;
			float my = height / 2;
		 
			// cr is the circle radius => 25 percent of the height
			double cr = height * 0.25f;
			double gr = height * 0.30f;
		 
		 
			for (int y=0; y<height; y++) {
				// Schleife ueber die x-Werte
				for (int x=0; x<width; x++) {
					int pos = y*width + x; // Arrayposition bestimmen
		 
					int r = 255;
					int g = 255;
					int b = 255;
		 
		 
					// circle distance between points => d = sqrt((x2 - x1)^2 + (y2 - y1)^2)
					double d = Math.sqrt(Math.pow(mx - x, 2) + Math.pow(my - y, 2));				
		 
		 
		 
					if(d <= gr && d >= cr){
						//p = percent between inner and outer radius
						// if p = 1 then we have 100% r 100% g 100% b and so on... 
						float p = (float) ((d - cr) / (gr - cr));
						r = Math.round(255);				
		 
						//check if the green value is smaller than the end value of the green
						g = Math.round(255 * p);
						if(g < 30) g = 30;
		 
						// the same for the blue value
						b = Math.round(255 * p);
						if(b < 30) b = 30;
		 
					}
		 
					if((d <= cr)){
						r = 255;
						g = 30;
						b = 30;
					}
		 
					// Werte zurueckschreiben
					pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
				}
			}
		 
			//return pixels;
		}
		
		////////////////////////////////////////////////////////////////////
		
		// neues Bild anzeigen
		imagePlus.show();
		imagePlus.updateAndDraw();
	}
	
	
	private void dialog() {
		// Dialog fuer Auswahl der Bilderzeugung
		GenericDialog gd = new GenericDialog("Bildart");
		
		gd.addChoice("Bildtyp", choices, choices[0]);
		
		
		gd.showDialog();	// generiere Eingabefenster
		
		choice = gd.getNextChoice(); // Auswahl uebernehmen
		
		if (gd.wasCanceled())
			System.exit(0);
	}
}
