package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutor implements TaskExecutor {

    private final ExecutorService executorService;

    public ThreadPoolExecutor() {

        int poolSize = Runtime.getRuntime().availableProcessors();

        System.out.println("Initializing Thread Pool with size: " + poolSize);

        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    public void execute(Runnable task) {
        executorService.submit(task);
    }
}