# sig4j
[![Maven Central](https://img.shields.io/maven-central/v/com.github.msteinbeck/sig4j.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.msteinbeck/sig4j)

Sig4j is a Java library for Qt like 
[signals and slots](http://doc.qt.io/qt-4.8/signalsandslots.html) which uses the
[FunctionalInterface](https://docs.oracle.com/javase/8/docs/api/java/lang/FunctionalInterface.html)
Annotation introduced with Java 8. This Annotation allows sig4j to connect
functions and lambdas to signals without much effort .

### Quickstart
The following code snippet gives a short example:
```java
import com.github.msteinbeck.sig4.ConnectionType;
import com.github.msteinbeck.sig4j.Signal1;

public class Quickstart {
    private final Signal1<String> signal = new Signal1<>();
    
    private void print(final String string) {
        System.out.println(string);
    }

    public static void main(final String[] args) {
        Quickstart q = new Quickstart();
        q.signal.connect(q::print);
        q.signal.emit("hellow world!");
    }
}
```

Sig4j supports the following connection types:
- Direkt: A slot is actuated within the thread context of the emitter.
- Queued: A slot is actuated by a global worker thread.
- Dispatched: A slot is actuated by a custom dispatcher.
- JavaFX: A slot is actuated by the JavaFX dispatcher thread.
- Swing: A slot is actuated by the Swing dispatcher thread.

Have a look at [examples](https://github.com/msteinbeck/sig4j/tree/master/src/main/java/com/github/msteinbeck/sig4j/example).
