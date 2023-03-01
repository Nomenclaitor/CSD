// CSD feb 2015 Juansa Sendra

import java.util.concurrent.atomic.AtomicInteger;

public class Pool1 extends Pool {   //no kids alone
    AtomicInteger instructorsSwimming = new AtomicInteger(0);
    AtomicInteger kidsSwimming = new AtomicInteger(0);
    public void init(int ki, int cap)           {}
    public synchronized void kidSwims() throws InterruptedException {
        while (instructorsSwimming.get() <= 0) {
            log.waitingToSwim();
            wait();
        }
        kidsSwimming.set(kidsSwimming.get() + 1);
        log.swimming();
    }

    public synchronized void kidRests() {
        kidsSwimming.set(kidsSwimming.get() - 1);
        log.resting();
        notifyAll();
    }

    public synchronized void instructorSwims() {
        instructorsSwimming.set(instructorsSwimming.get() + 1);
        log.swimming();
    }

    public synchronized void instructorRests() throws InterruptedException {
        while (kidsSwimming.get() > 0) {
            log.waitingToRest();
            wait();
        }
        instructorsSwimming.set(instructorsSwimming.get() - 1);
        log.resting();
    }
}
