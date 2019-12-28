import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ForkJoinStudy extends RecursiveTask<Long> {

    int mStartData;
    int mEndData;

    public ForkJoinStudy(int startData, int endData) {
        this.mStartData = startData;
        this.mEndData = endData;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int data = 210000000;
        long result = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i <= data; i++) {
            result += i;
        }
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(result);
        
        long start2 = System.currentTimeMillis();
        Future<Long> future = forkJoinPool.submit(new ForkJoinStudy(0, data));

        System.out.println(future.get());
        
        System.out.println(System.currentTimeMillis() - start2);
    }

    int parValue = 200000;

    @Override
    protected Long compute() {
        if (mEndData - mStartData <= parValue) {
            long count = 0;
            for (int i = mStartData; i <= mEndData; i++) {
                count+=i;
            }
            return count;
        } else {
            int midData = (mEndData + mStartData) / 2;
            ForkJoinTask<Long> leftValue = new ForkJoinStudy(mStartData, midData).fork();
            ForkJoinTask<Long> rightValue = new ForkJoinStudy(midData + 1, mEndData).fork();
            return leftValue.join()+rightValue.join();
        }
    }
}
