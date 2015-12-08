package so.sig4j.example;

import so.sig4j.ConnectionType;
import so.sig4j.signal.Signal0;

public class BlockingQueuedExample {

    public static void main(final String[] args) throws InterruptedException {
        A a = new A();
        a.signal.connect(() -> {
            try {
                System.out.println("this should be the first line");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, ConnectionType.BLOCKING_QUEUED);
        a.signal.connect(() -> {
            try {
                System.out.println("this should be the second line");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, ConnectionType.BLOCKING_QUEUED);
        a.signal.emit();
        System.out.println("this should be the last line");
    }

    public static class A {
        public final Signal0 signal = new Signal0();
    }
}
