package test;

import java.util.List;

public class SemaphoreExample {
    private static final TrackedSemaphore semaphore = new TrackedSemaphore(2);

    public static void main(String[] args) {
        Runnable runnable = () -> {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " acquired the semaphore");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
                System.out.println(Thread.currentThread().getName() + " released the semaphore");
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Thread> waitingThreads = semaphore.getWaitingThreads();
        System.out.println("Waiting threads: " + waitingThreads);
    }
}