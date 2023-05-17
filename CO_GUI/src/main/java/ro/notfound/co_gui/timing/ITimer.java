package ro.notfound.co_gui.timing;

public interface ITimer {
    /**
     * Saves the current elapsed time in a long variable, and resets any previously stored total time.
     */
    void start();

    /**
     * Make sure you return differences between stored times, and not the absolute current time.
     * Stop should return the total elapsed time since start was called.
     * @return the elapsed time since the start of the timer.
     */
    long stop();

    /**
     * Saves the current elapsed time in a variable, without resetting any previous saved times.
     */
    void resume();

    /**
     * @return the elapsed time since the last start or resume of the timer.
     */
    long pause();
}
