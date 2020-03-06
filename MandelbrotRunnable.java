import java.awt.image.BufferedImage;
import java.lang.Math;

public class MandelbrotRunnable implements Runnable {

   BufferedImage image;
   int num_chunks;
   double chunk_size;
   double width;
   double height;
   double compl[];
   int thread_num;
   int thread_count;
   int maxIter;

   public MandelbrotRunnable(BufferedImage image, int num_chunks, double chunk_size, double width, double height, double compl[], int thread_num, int thread_count) {
 
         this.image = image;
         this.num_chunks = num_chunks;
         this.chunk_size = chunk_size;
         this.width = width;
         this.height = height;
         this.compl = compl;
         this.thread_num = thread_num;
         this.thread_count = thread_count;
         maxIter = 1000;
    }

   public void run() {

       int black = 0x000000, green = 0x00FF00;
  
       for(int j=0; j < num_chunks; j++) {

         int remainder = (j + 1) % thread_count; 

         if(remainder == thread_num) {
             int start = (int) (j * chunk_size);
             int end = (int) ((j + 1) * chunk_size - 1);

             //we calculate only the area between start and end
             for(int x = start; x <= end; x++) {
                for(int y = 0; y < width; y++) {
                   double c_re = (y + (compl[0] * width)/(compl[1] - compl[0]))*(compl[1] - compl[0])/width;
                   double c_im = (x + (compl[2] * height)/(compl[3] - compl[2]))*(compl[3] - compl[2])/height; 
                   double p = 0, q = 0;
                   int iter = 0;
                   while(p*p+q*q < 4 && iter < maxIter) {
                    double p_new = p*p-q*q+(c_re*Math.cos(q)+c_im*Math.sin(q))/Math.pow(Math.E,p);
                    q = 2*p*q+(c_im*Math.cos(q)-c_re*Math.sin(q))/Math.pow(Math.E,p);
                    p = p_new;
                    iter++;
                } 
                if (iter < maxIter) image.setRGB(y, x, green);
                else image.setRGB(y, x, black);
            }
          }
         }            
        }
       }
      } 

