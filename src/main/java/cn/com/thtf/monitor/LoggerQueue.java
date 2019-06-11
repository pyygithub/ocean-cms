package cn.com.thtf.monitor;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 创建一个阻塞队列，作为日志系统输出的日志的一个临时载体
 * @author pyy
 * @date 2019-05-27 09:54:40
 */
public class LoggerQueue {

    /**
     * 队列大小
     */
    public static final int QUEUE_MAX_SIZE = 10000;

    /**
     * 阻塞队列
     */
    private BlockingQueue blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    /**
     * 静态内部类实现单例模式
     * 当第一次加载LoggerQueue类时并不会初始化singleton,只有第一次调用getInstance方法的时候才会初始化singleton
     * 第一次调用getInstance 方法的时候虚拟机才会加载SingletonHoder类,这种方式不仅能够保证线程安全,也能够保证对象的唯一
     * 还延迟了单例的实例化,所有推荐使用这种方式
     */
    private LoggerQueue() {
    }

    public static LoggerQueue getInstance() {
        return SingletonHolder.singleton;
    }

    private static class SingletonHolder {
        private static LoggerQueue singleton = new LoggerQueue();
    }

    /**
     * 消息入队
     * @param log
     * @return
     */
    public boolean push(LogMessage log) {
        return this.blockingQueue.add(log);
    }

    /**
     * 消息出队
     *
     * @return
     */
    public LogMessage poll() {
        LogMessage result = null;
        try {
            result = (LogMessage) this.blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
