package com.github.sig4j.example;

import com.github.sig4j.Dispatcher;
import com.github.sig4j.signal.Signal1;

import java.util.Scanner;

public class ConsumerProducer {
    public static void main(final String[] args) {
        Producer p = new Producer();
        Consumer c = new Consumer();
        p.signal.connect(c::print, c);
        p.signal.connect(c::printReverse, c);

        new Thread(p::start).start();
        new Thread(c::run).start();
    }

    private static final class Producer {
        public final Signal1<String> signal = new Signal1<>();
        private final Scanner scanner = new Scanner(System.in);

        void start() {
            System.out.println("Write something:");
            while (true) {
                signal.emit(scanner.nextLine());
            }
        }
    }

    private static final class Consumer extends Dispatcher {
        void print(final String input) {
            System.out.println(input);
        }

        void printReverse(final String input) {
            System.out.println(new StringBuilder(input).reverse().toString());
        }
    }
}
