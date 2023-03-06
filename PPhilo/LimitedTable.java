// CSD Mar 2013 Juansa Sendra

public class LimitedTable extends RegularTable { //max 4 in dinni
    int inTable = 0;// ng-room
    public LimitedTable(StateManager state) {super(state);}

    @Override
    public synchronized void enter(int id) throws InterruptedException {
        while (inTable >= 4) {
            wait();
        }
        inTable += 1;
    }

    @Override
    public synchronized void ponder(int id) {
        inTable -= 1;
        notifyAll();
        super.ponder(id);
    }
}
