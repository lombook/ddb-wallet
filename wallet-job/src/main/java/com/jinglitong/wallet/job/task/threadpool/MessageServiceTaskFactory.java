package com.jinglitong.wallet.job.task.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MessageServiceTaskFactory extends Thread {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public List<Runnable> waitTaskList = new Vector();
    // 创建初始容量为20的线程池
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(50, 100, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue(20, true));
    private Object lock = new Object();

    private MessageServiceTaskFactory() {
        this.start();
    }

    public static final MessageServiceTaskFactory newInstance() {
        return Holder.messageServiceTaskFactory;
    }

    /**
     * 线程池增加task
     *
     * @param task
     */
    public void addNewTask(Runnable task) {
        synchronized (lock) {
            if (threadPool.getQueue().size() == 20) {
                waitTaskList.add(task);
            } else {
                try {
                    threadPool.submit(task);
                } catch (RejectedExecutionException e) {
                    logger.error("线程池增加task拒绝策略异常，e=", e);
                    waitTaskList.add(task);
                } catch (Exception ex) {
                    logger.error("线程池增加task异常，e=", ex);
                }
            }
        }
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        logger.info("shutdown==============");
        while (true) {
            if (waitTaskList.size() > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("线程池工厂异常,e=", e);
                }
            } else {
                threadPool.shutdown();
                break;
            }

        }
    }

    @Override
    public void run() {
        while (true) {
            if (threadPool.getQueue().size() == 0 && waitTaskList.size() > 0) {
                synchronized (waitTaskList) {
                    try {
                        for (; threadPool.getQueue().size() < 20; ) {
                            if (waitTaskList.size() > 0) {
                                Runnable task = waitTaskList.get(0);
                                boolean isAdd = threadPool.getQueue().add(task);
                                if (isAdd) {
                                    waitTaskList.remove(task);
                                }
                            } else {
                                break;
                            }
                        }
                    } catch (Exception e) {
                        logger.error("线程池增加task异常,e=", e);
                    }
                }
            } else {
                //线程等待1s
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("线程池工厂异常,e=", e);
                }
            }
        }
    }

    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

    private static class Holder {
        private static final MessageServiceTaskFactory messageServiceTaskFactory = new MessageServiceTaskFactory();
    }
}
