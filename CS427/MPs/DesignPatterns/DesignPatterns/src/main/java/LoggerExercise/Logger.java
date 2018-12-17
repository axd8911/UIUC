package LoggerExercise;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static Logger instance = null;
    private static File log = null;
    /**
     * @TODO Add any necessary fields and variables.
     */

    /**
     * The constructor for SingletonLogger. Set all necessary fields.
     *
     */
    private Logger(){
        String date = new SimpleDateFormat("MMddyyyy").format(new Date());
        String today = "log" + date + ".log";
        log = new File(today);
        try {
            log.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * @return A Logger instance of this class.
     */
    public static Logger getInstance (){

        if (instance == null){
            instance = new Logger();
        }

        return instance;
    }


    /**
     *
     * @param log
     *            The Object that will be logged in the file.
     */
    public void logInFile(Object log) {
        try{
            FileWriter fw = new FileWriter(this.log, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(log.toString());
            bw.newLine();
            bw.close();

            String date = new SimpleDateFormat("MMddyyyy").format(new Date());
            if (log.equals(date + " has clear sky.")){
                instance = null;
                this.log = null;
            }

        } catch (IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

}