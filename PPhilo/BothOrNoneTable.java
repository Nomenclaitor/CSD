// CSD Mar 2013 Juansa Sendra

public class BothOrNoneTable extends RegularTable { //both or none
    public BothOrNoneTable(StateManager state) {super(state);}

    @Override
    public synchronized void takeLR(int id) throws InterruptedException {
        while (!state.leftFree(id) || !state.rightFree(id)) {
            wait();
        }
        state.takeLR(id);
        notifyAll();
    }

    @Override
    public synchronized void ponder(int id) {
        notifyAll();
        super.ponder(id);
    }
}
