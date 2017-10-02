package com.github.sig4j.example;


import com.github.sig4j.ConnectionType;
import com.github.sig4j.signal.Signal0;

public class QueuedExample {

    public static void main(final String[] args) throws InterruptedException {
        A a = new A();
        a.signal.connect(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("this should be the last line");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, ConnectionType.QUEUED);
        a.signal.emit();
        System.out.println("this should be the first line");
        Thread.sleep(2000);
    }

    private static class A {
        public final Signal0 signal = new Signal0();
    }
}
