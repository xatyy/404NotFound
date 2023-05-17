package ro.notfound.co_gui.bench.ram;

import ro.notfound.co_gui.bench.IBenchmark;

import java.lang.reflect.Array;

public class RAMmemoryUsage implements IBenchmark {
    private int  workload;
    private long sum=0;
    private int size = 1000000; // size of the array in bytes
    private long beforeUsedMem;
    private long afterUsedMem;
    private long usedMemory;
    private long average;
    @Override
    public void initialize(Object... params) {
        workload = (int) params[0];
    }

    @Override
    public void warmUp() {
        for(int i = 0; i<20;i++){
            beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            byte[] warmUparray = new byte[size];
            afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            usedMemory = afterUsedMem - beforeUsedMem;
        }
    }

    @Override
    public void run() {
        for(int i = 0; i<workload;i++){
            beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            byte[] array = new byte[size];
            afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            usedMemory = afterUsedMem - beforeUsedMem;
            sum+=usedMemory;
        }
        average=sum/workload;
    }

    @Override
    public void run(Object... options) {
        throw new UnsupportedOperationException(
                "Method not implemented. Use run() instead");
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
        return "The average used memory is: "+average+" bytes";
    }

}
