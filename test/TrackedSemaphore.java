package test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class TrackedSemaphore extends Semaphore {
    private final List<Thread> waitingThreads;

    public TrackedSemaphore(int permits) {
        super(permits);
        waitingThreads = new ArrayList<>();
    }

    @Override
    public void acquire() throws InterruptedException {
        super.acquire();
        trackWaitingThread(Thread.currentThread());
    }

    @Override
    public void acquire(int permits) throws InterruptedException {
        super.acquire(permits);
        trackWaitingThread(Thread.currentThread());
    }

    private synchronized void trackWaitingThread(Thread thread) {
        waitingThreads.add(thread);
    }

    public synchronized List<Thread> getWaitingThreads() {
        return new ArrayList<>(waitingThreads);
    }
}