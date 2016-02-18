package co.andrewdai.android.groupdialer;

import java.util.ArrayList;
import java.util.List;

public class Callee {

    public final String first, last, fullname, phone;
    private boolean called;
    private List<CalleeStateListener> listeners;

    public Callee(String first, String last, String phone) {
        this.first = first;
        this.last = last;
        this.fullname = first + " " + last;
        this.phone = phone;
        this.called = false;

        listeners = new ArrayList<CalleeStateListener>();
    }

    public void setCalled(boolean called) {
        this.called = called;
        fireCalledStateChangedEvent();
    }

    public boolean wasCalled() { return called; }

    public void attach(CalleeStateListener l) {
        listeners.add(l);
    }

    private void fireCalledStateChangedEvent() {
        for (CalleeStateListener l : listeners) {
            l.calleeCalledStateChanged(this, called);
        }
    }

    public interface CalleeStateListener {
        void calleeCalledStateChanged(Callee c, boolean called);
    }

    @Override
    public String toString() { return fullname + ": " + phone; }

    public static final List<Callee> TEST_ITEMS = new ArrayList<Callee>();
    private static final int COUNT = 25;

    static {
        for (int i = 0; i < COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Callee c) {
        TEST_ITEMS.add(c);
    }

    private static Callee createDummyItem(int position) {
        return new Callee("First" + position, "Last" + position, "978-201-0796");
    }
}
