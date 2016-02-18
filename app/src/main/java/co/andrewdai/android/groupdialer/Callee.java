package co.andrewdai.android.groupdialer;

import java.util.ArrayList;
import java.util.List;

public class Callee {

    public String first, last, phone;
    public boolean called;

    public Callee(String first, String last, String phone) {
        this.first = first;
        this.last = last;
        this.phone = phone;
        this.called = false;
    }

    public String fullname() { return first + " " + last; }

    @Override
    public String toString() { return fullname() + ": " + phone; }

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
