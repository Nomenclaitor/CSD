import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Terrain3 implements Terrain{
    Viewer v;
    private final ReentrantLock blockLock = new ReentrantLock();
    private Condition[][] conditions;

    public Terrain3(int t, int ants, int movs, String msg) {
        v = new Viewer(t, ants, movs, msg);
        conditions = new Condition[t][t];
        for (int i = 0; i < conditions.length; i++) {
            for (int j = 0; j < conditions[i].length; j++) {
                conditions[i][j] = blockLock.newCondition();
            }
        }
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
            Pos org = v.getPos(a);
            v.bye(a);
            conditions[org.x][org.y].signalAll();
        } finally {
            blockLock.unlock();
        }
    }

    public void move (int a) throws InterruptedException {
        blockLock.lock();   // locking of the ReentrantLock
        try {
            v.turn(a);
            Pos org = v.getPos(a);
            Pos dest = v.dest(a);
            while (v.occupied(dest)) {              // Wait condition (while)
                if (!conditions[dest.x][dest.y].await(300, TimeUnit.MILLISECONDS)) { // Waiting condition
                    v.chgDir(a);
                    dest = v.dest(a);
                }
            }
            v.go(a);
            conditions[org.x][org.y].signalAll();  // Notifies every thread waiting on <condition>
        } finally {
            // Releases the lock at the end regardless of the outcome
            blockLock.unlock();
        }
    }
}
