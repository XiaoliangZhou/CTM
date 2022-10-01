package transport.ctm.Thread;

import org.apache.log4j.Logger;

public class ResumeThread extends Thread{
    public static Logger logger=Logger.getLogger(ResumeThread.class);
    @Override
    public void run() {
        logger.debug("开始");
        System.out.println("Hello world");
    }
}