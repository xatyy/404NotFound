package ro.notfound.co_gui.timing;

public class Timer implements ITimer {

    long stop;
    long start;
    long elapsed;
    long total;

    @Override
    public void start() {
        start = System.nanoTime();
        total = 0;
    }

    @Override
    public long stop() {
        stop = System.nanoTime();
        elapsed = stop - start;
        total = total + elapsed;
        return total;
    }

    @Override
    public void resume() {
        start = System.nanoTime();
    }

    @Override
    public long pause() {
        stop = System.nanoTime();
        elapsed = stop - start;
        total = total + elapsed;
        return elapsed;
    }
}
