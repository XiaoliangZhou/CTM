package transport.ctm.Thread;

import org.apache.log4j.Logger;

/**
 * 用于循环1000次的线程
 * @Title: Run1000Thread.java
 * @Package cn.lonecloud.Thread.study
 * @Description:
 * @author lonecloud
 * @date 2016年8月14日 下午11:06:07
 */
public class Run1000Thread implements Runnable{
    boolean suspended=false;
    public static Logger logger=Logger.getLogger(Run1000Thread.class);
    @Override
    public void run() {
        while(true){
            int i = 0;
            System.out.println(i + "线程执行！");
            while (suspended){
                synchronized (this){
                    try {
                       this.wait();
                    } catch (Exception e) { e.printStackTrace();
                        break;
                    }
                }
                i++;
            }
        }
    }

    /**
     * 休眠
     * @param millis
     */
    void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停
     */
     void suspend(){
        suspended = true;
    }

    /**
     * 继续
     */
    synchronized void resume(){
        suspended = false;
        notify();
    }

}