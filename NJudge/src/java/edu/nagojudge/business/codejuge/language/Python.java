
package edu.nagojudge.business.codejuge.language;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import edu.nagojudge.business.codejuge.logic.exe.TimedShell;
import org.apache.log4j.Logger;

public class Python  {

    private final Logger logger = Logger.getLogger(Python.class);

    private String file, contents, dir;
    private int timeout;

    public Python(String file, int timeout, String contents, String dir) {
        this.file = file;
        this.timeout = timeout;
        this.contents = contents;
        this.dir = dir;
    }

    public void compile() {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/" + file)));
            out.write(contents);
            out.close();
            // create the execution script
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/run.sh")));
            out.write("cd \"" + dir + "\"\n");
            out.write("chroot .\n");
            out.write("python " + file + "< in.txt > out.txt 2>err.txt");
            out.close();
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("chmod +x " + dir + "/run.sh");
            p.waitFor();
            p = r.exec(dir + "/run.sh"); // execute the script
            //TimedShell shell = new TimedShell(this, p, 3000);
           // shell.start();
            p.waitFor();
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }

   
    public void execute() {
        // nothing to be done here
    }
}
