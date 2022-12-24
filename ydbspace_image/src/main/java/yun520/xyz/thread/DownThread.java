package yun520.xyz.thread;

import cn.hutool.core.date.DateUtil;
import kotlin.jvm.Throws;
import org.springframework.beans.factory.annotation.Autowired;
import yun520.xyz.entity.Filechunk;
import yun520.xyz.service.impl.FastDfsServiceimpl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class DownThread implements Runnable {


    //同个文件记录已经写入第几个切片 从1开始
    private AtomicInteger num = null;
    private Filechunk filechunk = null;
    private OutputStream outputStream = null;
    private   FastDfsServiceimpl fastdfs= null;
    private CountDownLatch latch= null;
    //出现异常时终止所有线程
    private ExecutorService executorService= null;
    public DownThread(ExecutorService executorService,CountDownLatch latch, AtomicInteger num , Filechunk filechunk, OutputStream outputStream, FastDfsServiceimpl fastdfs) {
        this.executorService= executorService;
        this.num= num;
        this.latch= latch;
        this.fastdfs = fastdfs;
        this.filechunk = filechunk;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
        System.out.println("开始下载执行线程"+Thread.currentThread().getName()+"时间"+ DateUtil.now()+"下载"+filechunk.getChunksnum());
        //检测活性
        outputStream.flush();
        //开始下载
        byte[] download = fastdfs.download(filechunk.getChunkpath());
        System.out.println("下载结束"+Thread.currentThread().getName()+"时间"+ DateUtil.now());
        //等待唤醒
        synchronized (
                latch
        ) {

                while (!(Integer.valueOf(this.num.get()).equals(filechunk.getChunksnum()))) {

                    try {
                        this.latch.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("唤醒" + this.num.get());

                }

                outputStream.write(download);
                this.latch.countDown();
                int andAdd = this.num.getAndAdd(1);
                System.out.println("此时写入第" + andAdd);
                //精确唤醒用lock
                latch.notifyAll();


        }
        } catch (Exception e) {
//            shutdown 会等待线程池中的任务执行完成之后关闭线程池，而 shutdownNow 会给所有线程发送中断信号，中断任务执行，然后关闭线程池
//            shutdown 没有返回值，而 shutdownNow 会返回关闭前任务队列中未执行的任务集合（List）
            e.printStackTrace();
            this.executorService.shutdown();

            System.out.println("关闭线程"+Thread.currentThread().getName()+e.getMessage());
            while ( this.latch.getCount() >0){
                this.latch.countDown();
            }
            //如果对方关闭了链接我们应该停止所有线程

        }


    }
}
