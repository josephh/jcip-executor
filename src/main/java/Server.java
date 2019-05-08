import java.net.Socket;

public interface Server {
    void doWork();

    /**
     * Task handling code must be thread-safe, wherever it may be invoked concurrently.
     * @param connection
     */
    void handleTask(Socket connection);
}
