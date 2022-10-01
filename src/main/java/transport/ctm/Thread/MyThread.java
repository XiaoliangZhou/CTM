package transport.ctm.Thread;

public abstract class MyThread extends Thread {

    private boolean suspend = false;
    private String lock = ""; // 只是需要一个对象而已，这个对象没有实际意义
    public void setSuspend(boolean suspend) {
        if (!suspend) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }
        this.suspend = suspend;
    }

    public boolean isSuspend() {
        return this.suspend;
    }

    public void run() {
        while (true) {
            synchronized (lock) {
                if (suspend) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.runPersonelLogic();
        }
    }

    protected abstract void runPersonelLogic();

    public static void main(String[] args) throws Exception {
        MyThread myThread = new MyThread() {
            int i=0;
            protected void runPersonelLogic() {
                try {
                    Thread.sleep(3000);
                    System.out.println(i+"线程执行");
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        myThread.start();
        Thread.sleep(3000);
        myThread.setSuspend(true);
        System.out.println("myThread has stopped");
        Thread.sleep(3000);
        myThread.setSuspend(false);
    }
}
