package com.example.demo.thread;


public class ThreadTest {

    public static int sum = 0;

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        Thread thread = new Thread(() -> {
            sum = sum();
        });
        thread.start();
        thread.join();
        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + sum);
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
