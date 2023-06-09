package ro.notfound.co_gui.bench.ram;

import ro.notfound.co_gui.bench.IBenchmark;

public class RAMmemoryUsage implements IBenchmark {

    //ArraySize at least 8000
    //Input for ArraySize
    private long sum=0;
    private int Arraysize ; // size of the array in bytes
    private long beforeUsedMem;
    private long afterUsedMem;
    private long usedMemory;
    private double average;
    private double score;
    @Override
    public void initialize(Object... params) {
        throw new UnsupportedOperationException(
                "Method not implemented.");
    }

    @Override
    public void warmUp() {
        throw new UnsupportedOperationException(
                "Method not implemented.");
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException(
                "Method not implemented. Use run() instead");
    }

    @Override
    public void run(Object... options) {
        Arraysize = (Integer) options[0];
        for(int i = 0; i<50;i++){
            beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            byte[] array = new byte[Arraysize];
            afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            usedMemory = afterUsedMem - beforeUsedMem;
            sum+=usedMemory;
        }

        average= (long) (sum/50.0);
        score= (long) (Arraysize/(average+1)*10.0);
    }


    @Override
    public void cancel() {
        throw new UnsupportedOperationException(
                "Method not implemented.");
    }

    @Override
    public void clean() {
        throw new UnsupportedOperationException(
                "Method not implemented.");
    }

    @Override
    public String getResult() {
        String str = new String();
        str= Double.toString(score);
        return str;
    }

}
