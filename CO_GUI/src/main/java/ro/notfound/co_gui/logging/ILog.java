package ro.notfound.co_gui.logging;

public interface ILog {
    /**
     * Will print the long parameter
     */
    void write(long longVar);

    /**
     * Will print the string parameter
     */
    void write(String stringVar);

    /**
     * Will print all values separated by space
     */
    void write(Object... values);

    /**
     * Writes a long value representing elapsed time to the implementing output
     * stream. <br>
     * The time value is converted to the given time unit.
     */
    public void writeTime(long value, TimeUnit unit);

    /**
     * Writes a (descriptive) string followed by a long value representing elapsed
     * time to the implementing output stream. <br>
     * The time value is converted to the given time unit.
     */
    public void writeTime(String string, long value, TimeUnit unit);

    /**
     * Used to close (if necessary) any open stream (connection) used for writing.
     */
    void close();
}
