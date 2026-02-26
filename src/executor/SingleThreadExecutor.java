package executor;

public class SingleThreadExecutor implements TaskExecutor {

    @Override
    public void execute(Runnable task) {
        task.run();   // Runs in same thread
    }
}