package executor;

public class PerThreadExecutor implements TaskExecutor {

    @Override
    public void execute(Runnable task) {

        Thread thread = new Thread(task);
        thread.start();
    }
}