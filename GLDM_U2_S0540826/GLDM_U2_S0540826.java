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

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
     Opens an image window and adds a panel below the image
*/
public class GLDM_U2_S0540826 implements PlugIn {

    ImagePlus imp; // ImagePlus object
	private int[] origPixels;
	private int width;
	private int height;
	
	
    public static void main(String args[]) {
		//new ImageJ();
    	IJ.open("/Applications/ImageJ/plugins/gdm/GLDM_U2_S0540826/orchid.jpg");
    	//IJ.open("Z:/Pictures/Beispielbilder/orchid.jpg");
		
		GLDM_U2_S0540826 pw = new GLDM_U2_S0540826();
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
    
    
    class CustomWindow extends ImageWindow implements ChangeListener {
         
        private JSlider jSliderBrightness;
		private JSlider jSliderKontrast;
		private JSlider jSliderSaettigung;
		private JSlider jSliderFarbton;
		private double brightness = 0;
		private double contrast = 1;
		private double saturation = 1;
		private double hue;

		CustomWindow(ImagePlus imp, ImageCanvas ic) {
            super(imp, ic);
            addPanel();
        }
    
        void addPanel() {
        	//JPanel panel = new JPanel();
        	Panel panel = new Panel();

            panel.setLayout(new GridLayout(4, 1));
            jSliderBrightness = makeTitledSilder("Helligkeit", -128, 128, 0);
            jSliderKontrast = makeTitledSilder("Kontrast", 0, 100, 10);
            jSliderSaettigung = makeTitledSilder("Saettigung", 0, 50, 25);
            jSliderFarbton = makeTitledSilder("Farbton", 0, 360, 0);
            panel.add(jSliderBrightness);
            panel.add(jSliderKontrast);
            panel.add(jSliderSaettigung);
            panel.add(jSliderFarbton);
            
            add(panel);
            
            pack();
         }
      
        private JSlider makeTitledSilder(String string, int minVal, int maxVal, int val) {
		
        	JSlider slider = new JSlider(JSlider.HORIZONTAL, minVal, maxVal, val );
        	Dimension preferredSize = new Dimension(width, 50);
        	slider.setPreferredSize(preferredSize);
			TitledBorder tb = new TitledBorder(BorderFactory.createEtchedBorder(), 
					string, TitledBorder.LEFT, TitledBorder.ABOVE_BOTTOM,
					new Font("Sans", Font.PLAIN, 11));
			slider.setBorder(tb);
			slider.setMajorTickSpacing((maxVal - minVal)/10 );
			slider.setPaintTicks(true);
			slider.addChangeListener(this);
			
			return slider;
		}
        
        private void setSliderTitle(JSlider slider, String str) {
			TitledBorder tb = new TitledBorder(BorderFactory.createEtchedBorder(),
				str, TitledBorder.LEFT, TitledBorder.ABOVE_BOTTOM,
					new Font("Sans", Font.PLAIN, 11));
			slider.setBorder(tb);
		}

		public void stateChanged( ChangeEvent e ){
			JSlider slider = (JSlider)e.getSource();

			if (slider == jSliderBrightness) {
				brightness = slider.getValue();
				String str = "Helligkeit " + brightness; 
				setSliderTitle(jSliderBrightness, str); 
			}
			
			if (slider == jSliderKontrast) {
				contrast = (double)slider.getValue() / (double)10;
				String str = "Kontrast " + contrast; 
				setSliderTitle(jSliderKontrast, str); 
			}
			
			if (slider == jSliderSaettigung) {
				saturation = slider.getValue() / 10;
				String str = "Saettigung " + saturation; 
				setSliderTitle(jSliderSaettigung, str); 
			}
			
			if (slider == jSliderFarbton) {
				hue = slider.getValue();
				String str = "Farbton " + hue; 
				setSliderTitle(jSliderFarbton, str); 
			}
			
			changePixelValues(imp.getProcessor());
			
			imp.updateAndDraw();
		}

		
		private void changePixelValues(ImageProcessor ip) {
			
			// Array fuer den Zugriff auf die Pixelwerte
			int[] pixels = (int[])ip.getPixels();
			
			
			for (int y=0; y<height; y++) {
				for (int x=0; x<width; x++) {
					int pos = y*width + x;
					int argb = origPixels[pos];  // Lesen der Originalwerte 
					
					int r = (argb >> 16) & 0xff;
					int g = (argb >>  8) & 0xff;
					int b =  argb        & 0xff;
					
					double yn;
					double cbn;
					double crn;
					
					// anstelle dieser drei Zeilen spaeter hier die Farbtransformation durchfuehren,
					// die Y Cb Cr -Werte veraendern und dann wieder zuruecktransformieren
										
					double rn = r;
					double gn = g;
					double bn = b;
					
					
					
					//Kontrast
					if (contrast != 1){
						rn = (int)(contrast * (rn - 128) + 128);
						gn = (int)(contrast * (gn - 128) + 128);
						bn = (int)(contrast * (bn - 128) + 128);
					}
					
					//RGB --> YCbCr 
					yn = 0.299 * rn + 0.587 * gn + 0.114 * bn;
					//1. Chrominanzkomponente
					cbn = -0.168736 * rn - 0.331264 * gn + 0.5 * bn;
					//2. Chrominanzkomponente
					crn = 0.5 * rn - 0.418688 * gn - 0.081312 * bn;
					
					//Helligkeit
					if (brightness != 0){
						yn = yn + brightness;
					}
					
					//Saettigung
					if(saturation != 1){
					cbn = cbn * saturation;
					crn = crn * saturation;
					}
					
					//Farbton
					if(hue != 0){
					double cbH = cbn;
					cbn = Math.cos(hue)*cbH - Math.sin(hue)*crn;
					crn = Math.sin(hue)*cbH + Math.cos(hue)*crn;
					}
					
					//YCbCr --> RGB
					rn = yn + 1.402 * crn;
					gn = yn - 0.3441 * cbn - 0.7141 * crn;
					bn = yn + 1.772 * cbn;
					
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
					
					/*if(r==(int)rn && g == (int)gn && b == (int)bn){
						System.out.println("Die Werte stimmen vor und nach der Transformation ueberein.");
					}*/
					
					pixels[pos] = (0xFF<<24) | ((int)rn<<16) | ((int)gn<<8) | (int)bn;
					
				}
			}
		}
		
    } // CustomWindow inner class
} 