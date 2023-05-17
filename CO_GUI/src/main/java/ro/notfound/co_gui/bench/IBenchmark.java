package ro.notfound.co_gui.bench;

public interface IBenchmark {
    /**
     * The method which will contain the core of the code used to actually benchmark a hardware component. <br>
     * Apart from the core of the code, there may be additional garbage code needed before and after the execution of <b> run() </b>.
     */
    void run();

    /**
     * The method which will contain the core of the code used to actually benchmark a hardware component. <br>
     * Apart from the core of the code, there may be additional garbage code needed before and after the execution of <b> run() </b>.
     */
    void run(Object... params);

    /**
     * Here we place all code needed to prepare the data for the run method to execute.
     * We can pass any number and type of arguments to this method, like, file path, array size etc.
     */
    void initialize(Object... params);

    void warmUp();

    /**
     * Here we might need to clean up after the run method.
     */
    void clean();

    /**
     * Useful mainly when exposing a UI (user interface) to a client, so that we can cancel the execution at any time (through a <b>Cancel</b> button), if the execution takes too much time, or something else goes wrong (infinite loop).
     *
     */
    void cancel();

    /**
     * Return the result of the benchmark. <br>
     * This call should not be benchmarked.
     *
     * @return
     */
    public String getResult();
}