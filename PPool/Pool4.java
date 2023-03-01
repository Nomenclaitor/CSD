// CSD feb 2013 Juansa Sendra

public class Pool4 extends Pool { //kids cannot enter if there are instructors waiting to exit
    int ki;
    int cap;
    int kidSwimming = 0;
    int instructorSwimming = 0;
    int waitingToRest = 0;
    public void init(int ki, int cap) {
        this.ki = ki;
        this.cap = cap;
    }
    public synchronized void kidSwims() throws InterruptedException {
        while (instructorSwimming <= 0 || kidSwimming / instructorSwimming >= ki || kidSwimming + instructorSwimming + 1 > cap || waitingToRest > 0) {
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
        while (kidSwimming + instructorSwimming + 1 >= cap) {
            log.waitingToSwim();
            wait();
        }
        instructorSwimming += 1;
        log.swimming();
        notifyAll();
    }

    public synchronized void instructorRests() throws InterruptedException {
        waitingToRest += 1;
        while (ki * (instructorSwimming - 1) < kidSwimming) {
            log.waitingToRest();
            wait();
        }
        instructorSwimming -= 1;
        waitingToRest -= 1;
        log.resting();
        notifyAll();
    }
}
