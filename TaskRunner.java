import java.util.Calendar;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TaskRunner {

public static void main(String[] args) throws Exception {
int gran = 1, threads = 1;
double w = 640.0;
double h = 480.0;
double compl[] = new double[4];
compl[0] = -2.0;
compl[1] = 2.0;
compl[2] = -2.0;
compl[3] = 2.0;

String fileName = "image.png";

  for(int i = 0; i < args.length; i++){

  if(args[i].compareTo("-g") == 0 || args[i].compareTo("-gran") == 0){
      gran = new Integer(args[i+1]);
   }
   else if(args[i].compareTo("-s") == 0 || args[i].compareTo("-size") == 0){
         String s = args[i+1];
         int l = s.indexOf('x');
         w = new Double(s.substring(0,l));
         h = new Double(s.substring(l + 1));
       }
   else if(args[i].compareTo("-r") == 0 || args[i].compareTo("-rect") == 0){
        String s = args[i+1];
        int prev = -1, k = 0;
        int curr;
        while(prev + 1 < s.length() && k < 4){
          curr = s.indexOf(':', prev + 1);
          if(curr == -1)
             compl[k] = new Double(s.substring(prev + 1));
          else
             compl[k] = new Double(s.substring(prev + 1, curr));
          prev = curr;
          k++;
          }
        }
    else if(args[i].compareTo("-t") == 0 || args[i].compareTo("-tasks") == 0){
         threads = new Integer(args[i+1]);
       }
    else if(args[i].compareTo("-o") == 0 || args[i].compareTo("-output") == 0){
         fileName = args[i+1];
    }
  }


                int num_chunks = gran*threads;
                double chunk_size = h/num_chunks;

               if (chunk_size < 1) {
                     System.out.println("Error chunk_size < 1 ");
                     System.exit(1);
                }

      BufferedImage image = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_RGB);
               
                Thread tr[] = new Thread[threads];
                
                long ts_b = Calendar.getInstance().getTimeInMillis(); 

                for(int i = 0; i < threads; i++) {

		MandelbrotRunnable r = new MandelbrotRunnable(image, num_chunks, chunk_size, w, h, compl, i, threads);
		        tr[i] = new Thread(r);
			tr[i].start();
		}

                for(int i = 0; i < threads; i++) {
			
			try {
				
				tr[i].join();
				
			} catch (InterruptedException e) {
				
			}
			
		}

		long ts_e = Calendar.getInstance().getTimeInMillis();
                
                System.out.println("Threads used in current run: " + threads); 
		System.out.println("Total execution time for current run (millis): " + (ts_e - ts_b) );
                ImageIO.write(image, "png", new File(fileName));

   }
}
