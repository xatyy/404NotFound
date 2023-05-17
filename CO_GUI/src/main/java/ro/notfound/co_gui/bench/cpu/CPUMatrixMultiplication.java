package ro.notfound.co_gui.bench.cpu;

import ro.notfound.co_gui.bench.IBenchmark;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CPUMatrixMultiplication implements IBenchmark {
    private static final int BLOCK_SIZE = 32;
    private int matrixSize;
    private int numThreads;
    private double[][] matrixA;
    private double[][] matrixB;
    private double[][] result;

    @Override
    public void run() {
        throw new UnsupportedOperationException(
                "Method not implemented. Use run(Object) instead");
    }

    public void run(Object... params) {
        numThreads = (int) params[0];
        int blockSize = BLOCK_SIZE;

        ExecutorService executor = new ThreadPoolExecutor(numThreads, numThreads,
                0L, TimeUnit.MILLISECONDS,
                new java.util.concurrent.LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < matrixSize; i += blockSize) {
            for (int j = 0; j < matrixSize; j += blockSize) {
                for(int k = 0; k < matrixSize; k +=blockSize){
                    executor.submit(new MatrixMultiplicationTask(i, j, k, blockSize));
                }
            }
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initialize(Object... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("Expected 1 parameter: matrix size");
        }
        matrixSize = (int) params[0];

        matrixA = new double[matrixSize][matrixSize];
        matrixB = new double[matrixSize][matrixSize];
        result = new double[matrixSize][matrixSize];
        // initialize matrices with random values
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                matrixA[i][j] = Math.random();
                matrixB[i][j] = Math.random();
            }
        }
    }

    public void warmUp() {
    }

    public void clean() {
        matrixA = null;
        matrixB = null;
        result = null;
    }

    public void cancel() {
    }

    public String getResult() {
        return String.format("Matrix size: %d, Number of threads: %d", matrixSize, numThreads);
    }

    public double score(double time,int i) {
        double score = (double) matrixSize/( Math.log(time) * 10E-2* i);
        return score;
    }

    private class MatrixMultiplicationTask implements Runnable {

        private int startRow;
        private int startCol;
        private int startK;
        private int blockSize;

        public MatrixMultiplicationTask(int startRow, int startCol, int startK, int blockSize) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.startK = startK;
            this.blockSize = blockSize;
        }

        public void run() {
            int endRow = Math.min(startRow + blockSize, matrixSize);
            int endCol = Math.min(startCol + blockSize, matrixSize);
            int endK = Math.min(startK + blockSize, matrixSize);

            for (int i = startRow; i < endRow; i++) {
                for (int j = startCol; j < endCol; j++) {
                    double sum = 0;
                    for (int k = startK; k < endK; k++) {
                        sum += matrixA[i][k] * matrixB[k][j];
                    }
                    result[i][j] += sum;
                }
            }
        }
    }
}
