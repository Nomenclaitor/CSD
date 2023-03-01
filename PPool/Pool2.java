// CSD feb 2015 Juansa Sendra

import java.util.concurrent.atomic.AtomicInteger;

public class Pool2 extends Pool{ //max kids/instructor

    private int ki;
    private int cap;
    int kidSwimming = 0;
    int instructorSwimming = 0;
    public void init(int ki, int cap) {
        this.ki = ki;
        this.cap = cap;
    }
    public synchronized void kidSwims() throws InterruptedException {
        while (instructorSwimming <= 0 || kidSwimming / instructorSwimming >= ki) {
            log.waitingToSwim();
            wait();
        }
        kidSwimming += 1;
        log.swimming();
    }

    public synchronized void kidRests() throws InterruptedException {
        kidSwimming -= 1;
        log.resting();
        notifyAll();
    }

    public synchronized void instructorSwims() throws InterruptedException {
        instructorSwimming += 1;
        log.swimming();
        notifyAll();
    }

    public synchronized void instructorRests() throws InterruptedException {
        while (kidSwimming > 0 || kidSwimming / instructorSwimming - 1 > ki) {
            log.waitingToRest();
            wait();
        }
        instructorSwimming -= 1;
        log.resting();
    }
}
