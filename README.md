# sig4j
Sig4j is a Java library for Qt like 
[signals and slots](http://doc.qt.io/qt-4.8/signalsandslots.html) which uses the
[FunctionalInterface](https://docs.oracle.com/javase/8/docs/api/java/lang/FunctionalInterface.html)
Annotation introduced with Java 8. This Annotation allows sig4j to connect
functions and lambdas to signals without much effort .

### Quickstart
The following code snippet shows a short example:
```java
import so.sig4j.ConnectionType;
import so.sig4j.signal.Signal1;

public class Quickstart {
    private final Signal1<String> signal = new Signal1<>();
    
    private final void print(final String string) {
        System.out.println(string);
    }

    public static void main(final String[] args) {
        Quickstart q = new Quickstart();
        q.signal.connect(q::print);
        q.signal.emit("hellow world!");
    }
}
```
