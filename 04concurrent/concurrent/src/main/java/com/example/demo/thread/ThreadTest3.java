package com.example.demo.thread;


import java.util.concurrent.*;

public class ThreadTest3 {


    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        //创建一个线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> future = executor.submit(() -> {
            return sum();
        });
        executor.shutdown();
        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + future.get());
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
