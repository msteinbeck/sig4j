package so.sig4j;

import org.junit.Assert;
import org.junit.Test;
import so.sig4j.signal.Signal1;
import so.sig4j.signal.Signal4;

import static org.junit.Assert.*;

public class SignalTest {

    private static class SignalProvider {
        Signal1<String> stringSignal1 = new Signal1<>();
        Signal4<Integer, Integer, Integer, Integer> intSignal4 = new Signal4<>();
    }

    private static class SlotProvider {
        void stringSlot1(final String s) {}
    }


    @Test
    public void implicitDirect() {
        SignalProvider signalProvider = new SignalProvider();
        SlotProvider slotProvider = new SlotProvider();
        signalProvider.stringSignal1.connect(slotProvider::stringSlot1);
    }

    @Test
    public void implicitDirectLambda() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(s -> {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void implicitDirectNull() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(null);
    }



    @Test
    public void explicitDirect() {
        SignalProvider signalProvider = new SignalProvider();
        SlotProvider slotProvider = new SlotProvider();
        signalProvider.stringSignal1.connect(
                slotProvider::stringSlot1, ConnectionType.DIRECT);
    }

    @Test
    public void explicitDirectLambda() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(s -> {}, ConnectionType.DIRECT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void explicitDirectNull() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(null, ConnectionType.DIRECT);
    }



    @Test
    public void queued() {
        SignalProvider signalProvider = new SignalProvider();
        SlotProvider slotProvider = new SlotProvider();
        signalProvider.stringSignal1.connect(
                slotProvider::stringSlot1, ConnectionType.QUEUED);
    }

    @Test
    public void queuedLambda() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(s -> {}, ConnectionType.QUEUED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void queuedNull() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(null, ConnectionType.QUEUED);
    }



    @Test
    public void emitString1() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(s ->
                assertEquals("test", s));
        signalProvider.stringSignal1.emit("test");
    }

    @Test
    public void emitInteger4() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.intSignal4.connect(
                (i, ii, iii, iiii) -> {
                    assertEquals(new Integer(0), i);
                    assertEquals(new Integer(1), ii);
                    assertEquals(new Integer(2), iii);
                    assertEquals(new Integer(-3), iiii);
                });
        signalProvider.intSignal4.emit(0, 1, 2, -3);
    }

    @Test
    public void emitNull4() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.intSignal4.connect(
                (i, ii, iii, iiii) -> {
                    assertNull(i);
                    assertNull(ii);
                    assertNull(iii);
                    assertNull(iiii);
                });
        signalProvider.intSignal4.emit(null, null, null, null);
    }



    @Test(expected = RuntimeException.class)
    public void enabledDefault() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(
                s -> {throw new RuntimeException();});
        signalProvider.stringSignal1.emit("enabled");
    }

    @Test(expected = RuntimeException.class)
    public void enabled() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(
                s -> {throw new RuntimeException();});
        signalProvider.stringSignal1.disable();
        signalProvider.stringSignal1.enable();
        signalProvider.stringSignal1.emit("enabled");
    }

    @Test
    public void disabled() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(
                s -> {throw new RuntimeException();});
        signalProvider.stringSignal1.disable();
        signalProvider.stringSignal1.emit("enabled");
    }



    @Test
    public void clear() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(
                s -> {throw new RuntimeException();});
        signalProvider.stringSignal1.clear();
        signalProvider.stringSignal1.emit("enabled");
    }

    @Test
    public void clearMulti() {
        SignalProvider signalProvider = new SignalProvider();
        signalProvider.stringSignal1.connect(
                s -> {throw new RuntimeException();});
        signalProvider.stringSignal1.connect(
                s -> {throw new RuntimeException();});
        signalProvider.stringSignal1.connect(
                s -> {throw new RuntimeException();});
        signalProvider.stringSignal1.connect(
                s -> {throw new RuntimeException();});
        signalProvider.stringSignal1.clear();
        signalProvider.stringSignal1.emit("enabled");
    }
}
