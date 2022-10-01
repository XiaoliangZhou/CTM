package transport.ctm.Thread;

import org.apache.log4j.Logger;

public class MainTest {
    public static Logger logger=Logger.getLogger(MainTest.class);
    public static void main(String[] args) throws Exception {
       /* ResumeThread thread=new ResumeThread();
        thread.start();
        logger.debug("开始");
        thread.sleep(1000);
        thread.suspend();//获取此线程停止
        logger.debug("我是休眠");
        thread.sleep(1000);
        thread.resume();//获取线程继续
        logger.debug("我是启动");*/

        Run1000Thread thread1 = new Run1000Thread();
        thread1.run();
        logger.debug("开始");
        thread1.suspend();//此线程停止

        thread1.resume();//获取线程继续
        logger.debug("线程继续");
    }
}