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
public class GRDM_U3_S0540826 implements PlugIn {

	ImagePlus imp; // ImagePlus object
	private int[] origPixels;
	private int width;
	private int height;

	String[] items = {"Original", "Rot-Kanal", "Negativ", "Graustufen", "Binaerbild", "Fuenf Graustufen", "Zehn Graustufen", "Binaerbild mit horizontaler Diffusion", "Sepia", "Sechs Farben"};


	public static void main(String args[]) {

		IJ.open("/applications/ImageJ/plugins/gdm/GLDM_U3_S0540826/bear.jpg");
		//IJ.open("Z:/Pictures/Beispielbilder/orchid.jpg");

		GRDM_U3_S0540826 pw = new GRDM_U3_S0540826();
		pw.imp = IJ.getImage();
		pw.run("");
	}

	public void run(String arg) {
		if (imp==null) 
			imp = WindowManager.getCurrentImage();
		if (imp==null) {
			return;
		}
		CustomCanvas cc = new CustomCanvas(imp);

		storePixelValues(imp.getProcessor());

		new CustomWindow(imp, cc);
	}


	private void storePixelValues(ImageProcessor ip) {
		width = ip.getWidth();
		height = ip.getHeight();

		origPixels = ((int []) ip.getPixels()).clone();
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

			// Array zum ZurÃ¼ckschreiben der Pixelwerte
			int[] pixels = (int[])ip.getPixels();

			if (method.equals("Original")) {

				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						int pos = y*width + x;
						
						pixels[pos] = origPixels[pos];
					}
				}
			}
			
			if (method.equals("Rot-Kanal")) {

				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						int pos = y*width + x;
						int argb = origPixels[pos];  // Lesen der Originalwerte 

						int r = (argb >> 16) & 0xff;
						//int g = (argb >>  8) & 0xff;
						//int b =  argb        & 0xff;

						int rn = r;
						int gn = 0;
						int bn = 0;

						// Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

						pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
					}
				}
			}
			
			if (method.equals("Negativ")) {

				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						int pos = y*width + x;
						int argb = origPixels[pos];  // Lesen der Originalwerte 

						int r = (argb >> 16) & 0xff;
						int g = (argb >>  8) & 0xff;
						int b =  argb        & 0xff;

						int rn = 255-r;
						int gn = 255-g;
						int bn = 255-b;

						// Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

						pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
					}
				}
			}
			
			if (method.equals("Graustufen")) {

				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						int pos = y*width + x;
						int argb = origPixels[pos];  // Lesen der Originalwerte 

						int r = (argb >> 16) & 0xff;
						int g = (argb >>  8) & 0xff;
						int b =  argb        & 0xff;

						int grau = (r+g+b)/3;
						
						int rn = grau;
						int gn = grau;
						int bn = grau;

						// Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

						pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
					}
				}
			}
			
			if (method.equals("Binaerbild")) {

				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						int pos = y*width + x;
						int argb = origPixels[pos];  // Lesen der Originalwerte 

						int r = (argb >> 16) & 0xff;
						int g = (argb >>  8) & 0xff;
						int b =  argb        & 0xff;

						int grau = (r+g+b)/3;
						
						int rn;
						int gn;
						int bn;

						if(grau > 128){
							rn = 255;
							gn = 255;
							bn = 255;
						} else {
							rn = 0;
							gn = 0;
							bn = 0;
						}

						// Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

						pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
					}
				}
			}
			
			if (method.equals("Fuenf Graustufen")) {

				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						int pos = y*width + x;
						int argb = origPixels[pos];  // Lesen der Originalwerte 

						int r = (argb >> 16) & 0xff;
						int g = (argb >>  8) & 0xff;
						int b =  argb        & 0xff;

						int grau = (r+g+b)/3;
						
						int rn;
						int gn;
						int bn;
						
						int grauStufe = 256/5;

						if(grau < grauStufe){
							rn = 0;
							gn = 0;
							bn = 0;
						} else if(grau < 2*grauStufe){
							rn = grauStufe;
							gn = grauStufe;
							bn = grauStufe;
						} else if(grau < 3*grauStufe){
							rn = 2*grauStufe;
							gn = 2*grauStufe;
							bn = 2*grauStufe;
						} else if(grau < 4*grauStufe){
							rn = 3*grauStufe;
							gn = 3*grauStufe;
							bn = 3*grauStufe;
						}  else {
							rn = 255;
							gn = 255;
							bn = 255;
						}

						// Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

						pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
					}
				}
			}
			
			if (method.equals("Zehn Graustufen")) {

				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						int pos = y*width + x;
						int argb = origPixels[pos];  // Lesen der Originalwerte 

						int r = (argb >> 16) & 0xff;
						int g = (argb >>  8) & 0xff;
						int b =  argb        & 0xff;

int grau = (r+g+b)/3;
						
						int rn;
						int gn;
						int bn;
						
						int grauStufe = 256/10;

						if(grau < grauStufe){
							rn = 0;
							gn = 0;
							bn = 0;
						} else if(grau < 2*grauStufe){
							rn = grauStufe;
							gn = grauStufe;
							bn = grauStufe;
						} else if(grau < 3*grauStufe){
							rn = 2*grauStufe;
							gn = 2*grauStufe;
							bn = 2*grauStufe;
						} else if(grau < 4*grauStufe){
							rn = 3*grauStufe;
							gn = 3*grauStufe;
							bn = 3*grauStufe;
						} else if(grau < 5*grauStufe){
							rn = 4*grauStufe;
							gn = 4*grauStufe;
							bn = 4*grauStufe;
						} else if(grau < 6*grauStufe){
							rn = 5*grauStufe;
							gn = 5*grauStufe;
							bn = 5*grauStufe;
						} else if(grau < 7*grauStufe){
							rn = 6*grauStufe;
							gn = 6*grauStufe;
							bn = 6*grauStufe;
						} else if(grau < 8*grauStufe){
							rn = 7*grauStufe;
							gn = 7*grauStufe;
							bn = 7*grauStufe;
						} else if(grau < 9*grauStufe){
							rn = 8*grauStufe;
							gn = 8*grauStufe;
							bn = 8*grauStufe;
						}  else {
							rn = 255;
							gn = 255;
							bn = 255;
						}
						// Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

						pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
					}
				}
			}
			
			
			if (method.equals("Binaerbild mit horizontaler Diffusion")) {

				int error = 0;
				//Floyd Steinberg Dithering
				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						int pos = y*width + x;
						int argb = origPixels[pos];  // Lesen der Originalwerte 

						int r = (argb >> 16) & 0xff;
						int g = (argb >>  8) & 0xff;
						int b =  argb        & 0xff;

						int grau = (r+g+b)/3 + error;
						
						/*//x+1 Werte
						int argbx_1 = origPixels[(y*width + x + 1)];  // Lesen der Originalwerte
						int rx_1 = (argbx_1 >> 16) & 0xff;
						int gx_1 = (argbx_1 >>  8) & 0xff;
						int bx_1 =  argbx_1        & 0xff;
						
						int graux_1 = (rx_1+gx_1+bx_1)/3;
						
						//y+1 Werte
						int argby_1 = origPixels[((y+1)*width+x)];  // Lesen der Originalwerte
						int ry_1 = (argby_1 >> 16) & 0xff;
						int gy_1 = (argby_1 >>  8) & 0xff;
						int by_1 =  argby_1        & 0xff;
						
						int grauy_1 = (ry_1+gy_1+by_1)/3;
												
						//y+1 x+1 Werte
						int argbyx_1 = origPixels[((y+1)*width+x+1)];  // Lesen der Originalwerte
						int ryx_1 = (argbyx_1 >> 16) & 0xff;
						int gyx_1 = (argbyx_1 >>  8) & 0xff;
						int byx_1 =  argbyx_1        & 0xff;
						
						int grauyx_1 = (ryx_1+gyx_1+byx_1)/3;*/
						
						int rn;
						int gn;
						int bn;
						
						if (grau > 128){
							rn = 255;
							gn = 255;
							bn = 255;
							error = grau - 255;
						} else {
							rn = 0;
							gn = 0;
							bn = 0;				
							error = grau - 0;
						}
						
							
						
						// Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

						pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
					}
				}
			}
			
			if (method.equals("Sepia")) {

				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						int pos = y*width + x;
						int argb = origPixels[pos];  // Lesen der Originalwerte 

						int r = (argb >> 16) & 0xff;
						int g = (argb >>  8) & 0xff;
						int b =  argb        & 0xff;

						int grau = (r+g+b)/3;
						
						int rn = grau;
						int gn = grau;
						int bn = grau;
						double deltar = 1.7;
						double deltag = 1.2;
						double deltab = 0.8;
						
						rn = 255 * (int)Math.pow(rn, (1/deltar)) / (int)Math.pow(255, (1/deltar));
						gn = 255 * (int)Math.pow(gn, (1/deltag)) / (int)Math.pow(255, (1/deltag));
						bn = 255 * (int)Math.pow(bn, (1/deltab)) / (int)Math.pow(255, (1/deltab));

						// Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden
						if (rn>255){
							rn = 255;
						} else if(rn < 0){
								rn = 0;
						}
						
						if (gn>255){
							gn = 255;
						} else if(gn<0){
							gn = 0;
						}
						
						if (bn>255){
							bn = 255;
						} else if(bn<0){
							bn = 0;
						}
						
						pixels[pos] = (0xFF<<24) | ((int)rn<<16) | ((int)gn<<8) | (int)bn;
					}
				}
			}
			
			if (method.equals("Sechs Farben")) {

				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						int pos = y*width + x;
						int argb = origPixels[pos];  // Lesen der Originalwerte 

						int r = (argb >> 16) & 0xff;
						int g = (argb >>  8) & 0xff;
						int b =  argb        & 0xff;

						int rn = 0;
						int gn = 0;
						int bn = 0;
						
						if(r>127){
							if(g>127){
								if(b>127){
									rn = 240;
									gn = 240;
									bn = 240;
								} else {
									rn = 255;
									gn = 214;
									bn = 127;
								} 
							} else {
								rn = 255;
								gn = 111;
								bn = 111;
							} 
						} else {
							if(g<128){
								if(b<128){
									rn = 74;
									gn = 52;
									bn = 52;
								} else {
									rn = 0;
									gn = 116;
									bn = 187;
								} 
							} else {
								rn = 0;
								gn = 155;
								bn = 255;
							} 
						}

						// Hier muessen die neuen RGB-Werte wieder auf den Bereich von 0 bis 255 begrenzt werden

						pixels[pos] = (0xFF<<24) | (rn<<16) | (gn<<8) | bn;
					}
				}
			}
			
		}


	} // CustomWindow inner class
} 