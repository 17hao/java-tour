package xyz.shiqihao.advanced.concurrency.juc.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Use of Semaphore
 */
public class MyTest4 {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            es.submit(new Task(semaphore));
        }
        es.shutdown();
    }

    static class Task implements Runnable {
        private final Semaphore semaphore;

        Task(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                Thread.sleep(100);
                System.out.println("thread id: " + Thread.currentThread().getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }
}
