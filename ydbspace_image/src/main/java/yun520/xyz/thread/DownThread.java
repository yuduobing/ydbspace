package yun520.xyz.thread;

import cn.hutool.core.date.DateUtil;

import yun520.xyz.entity.Filechunk;
import yun520.xyz.service.StoreService;

import java.io.OutputStream;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * 多线程下载
 */
public class DownThread implements Runnable {
    private static Logger logger = Logger.getLogger("DownThread.class");
    //同个文件记录已经写入第几个切片 从1开始
    private AtomicInteger num = null;
    private Filechunk filechunk = null;
    private OutputStream outputStream = null;
    private StoreService fastdfs = null;
    //控制所有线程执行完成
    private CountDownLatch latch = null;
    //线程队列 方便异常清楚
    private LinkedBlockingQueue linkedBlockingQueue = null;
    //出现异常时终止所有线程
    private ExecutorService executorService = null;

    public DownThread(ExecutorService executorService, CountDownLatch latch, AtomicInteger num, Filechunk filechunk, OutputStream outputStream, StoreService fastdfs, LinkedBlockingQueue linkedBlockingQueue) {
        this.executorService = executorService;
        this.num = num;
        this.latch = latch;
        this.fastdfs = fastdfs;
        this.filechunk = filechunk;
        this.outputStream = outputStream;
        this.linkedBlockingQueue = linkedBlockingQueue;
    }

    @Override
    public void run() {
        try {

            //检测活性 防止下载完报错耽误下载网速

            outputStream.flush();

            logger.info("开始下载" + Thread.currentThread().getName() + "时间" + DateUtil.now() + "下载" + filechunk.getChunksnum());
            //开始下载
            byte[] download = fastdfs.download(filechunk.getChunkpath());

            logger.info("下载结束" + Thread.currentThread().getName() + "时间" + DateUtil.now() + "下载结束" + filechunk.getChunksnum());
            //等待唤醒
            synchronized (
                    latch
            ) {
                while (!(Integer.valueOf(this.num.get()).equals(filechunk.getChunksnum()))) {
                    this.latch.wait();
                    //检测活性
                    outputStream.flush();
                }
                outputStream.write(download);
                outputStream.flush();
                this.latch.countDown();
                int andAdd = this.num.getAndAdd(1);
                logger.info("开始执行线程" + Thread.currentThread().getName() + "唤醒" + this.num.get() + "此时写入第" + andAdd);
                //精确唤醒用lock
                latch.notifyAll();

            }
        } catch (Exception e) {
            //异常重试机制




//            shutdown 会等待线程池中的任务执行完成之后关闭线程池，而 shutdownNow 会给所有线程发送中断信号，中断任务执行，然后关闭线程池
//            shutdown 没有返回值，而 shutdownNow 会返回关闭前任务队列中未执行的任务集合（List）
            this.executorService.shutdown();
            this.linkedBlockingQueue.clear();

            logger.info("线程异常" + Thread.currentThread().getName() + "下载" + filechunk.getChunksnum() + e.getMessage());
            while (this.latch.getCount() > 0) {
                this.latch.countDown();
            }
            e.printStackTrace();
            //如果对方关闭了链接我们应该停止所有线程

        }

    }
}
