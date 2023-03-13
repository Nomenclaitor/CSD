import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Terrain1 implements  Terrain{
    Viewer v;
    private final ReentrantLock blockLock = new ReentrantLock();
    private final Condition condition = blockLock.newCondition();

    public Terrain1(int t, int ants, int movs, String msg) {
            v = new Viewer(t, ants, movs, msg);
    }

    @Override
    public void hi (int a) {
        blockLock.lock();
        try {
            v.hi(a);
        } finally {
            blockLock.unlock();
        }
    }
    public void bye (int a) {
        blockLock.lock();
        try {
            v.bye(a);
            condition.signalAll();
        } finally {
            blockLock.unlock();
        }
    }
    public void move (int a) throws InterruptedException {
        blockLock.lock();   // locking of the ReentrantLock
        try {
            v.turn(a);
            Pos dest = v.dest(a);
            while (v.occupied(dest)) { // Wait condition (while)
                condition.await();  // Waiting condition
                v.retry(a);
            }
            v.go(a);
            condition.signalAll();  // Notifies every thread waiting on <condition>
        } finally {
            // Releases the lock at the end regardless of the outcome
            blockLock.unlock();
        }
    }
}
