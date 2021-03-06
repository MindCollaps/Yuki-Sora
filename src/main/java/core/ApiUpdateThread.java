package core;

public class ApiUpdateThread implements Runnable{

    private final Engine engine;
    private boolean stop = false;

    public ApiUpdateThread(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void run() {
        while (true) {
            if (stop) {
                break;
            }
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                if (engine.getProperties().debug) {
                    e.printStackTrace();
                }
            }
            if (stop) {
                break;
            }
            if (engine.getDiscEngine().isRunning()) {
                engine.getDiscEngine().getFilesHandler().updateApiData();
            }
        }
    }

    public void stop() {
        stop = true;
    }
}
