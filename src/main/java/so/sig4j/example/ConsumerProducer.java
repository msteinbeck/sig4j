package so.sig4j.example;

import so.sig4j.SlotDispatcher;
import so.sig4j.signal.Signal1;

import java.util.Scanner;

public class ConsumerProducer {
    public static void main(final String[] args) {
        Producer p = new Producer();
        Consumer c = new Consumer();
        p.signal.connect(c, c::print);
        p.signal.connect(c, c::printReverse);

        new Thread(p::start).start();
        new Thread(c::start).start();
    }

    private static final class Producer {
        public final Signal1<String> signal = new Signal1<>();
        private final Scanner scanner = new Scanner(System.in);

        public void start() {
            System.out.println("Write something:");
            while (true) {
                signal.emit(scanner.nextLine());
            }
        }
    }

    private static final class Consumer extends SlotDispatcher {
        public void print(final String input) {
            System.out.println(input);
        }

        public void printReverse(final String input) {
            System.out.println(new StringBuilder(input).reverse().toString());
        }
    }
}
