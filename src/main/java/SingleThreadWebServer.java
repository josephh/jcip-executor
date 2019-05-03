import java.io.IOException;
import java.net.ServerSocket;

/**
 * SingleThreadWebServer processes requests arriving at its port sequentially.
 * 
 * This server is simple and theoretically correct but it will exhibit poor performance as it can only handle one
 * request at a time.
 */
public class SingleThreadWebServer {

    static final int port = 59090;

    protected void doWork() {

        try (var listener = new ServerSocket(port)) { // Java try-with-resources, ensures
            // declared resource is closed at end of try block...
            // Any object that implements java.lang.AutoCloseable (including implementations of java.io.Closeable)
            System.out.println("The server is running...on port " + port);
            while (true) {
                try (var socket = listener.accept()) {
                    System.out.println("doWork: number of threads? " + java.lang.Thread.activeCount());
                    RequestHandler.handleRequest(socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        System.out.println("main: number of threads? " + java.lang.Thread.activeCount());
        SingleThreadWebServer stws = new SingleThreadWebServer();
        stws.doWork();
    }

}
