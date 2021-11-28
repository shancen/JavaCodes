package com.example.demo.thread;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadTest4 {

    public static  int sum = 0;

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        //创建一个线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        final CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sum = sum();
                latch.countDown();//当前线程调用此方法，则计数减一
            }
        };
        executor.execute(runnable);
        latch.await();//阻塞当前线程，直到计数器的值为0
        System.out.println("异步计算结果为：" + sum);
        // 确保  拿到result 并输出
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        // 然后退出main线程
        System.out.println("最后执行");
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}
