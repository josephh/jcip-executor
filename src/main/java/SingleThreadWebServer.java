import java.io.IOException;
import java.net.ServerSocket;

public class SingleThreadWebServer {

    static final int port = 59090;

    protected void doWork() {

        try (var listener = new ServerSocket(port)) { // Java try-with-resources, ensures
            // declared resource is closed at end of try block...
            // Any object that implements java.lang.AutoCloseable (including implementations of java.io.Closeable)
            System.out.println("The server is running...on port " + port);
            while (true) {
                try (var socket = listener.accept()) {
                    RequestHandler.handleRequest(socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SingleThreadWebServer stws = new SingleThreadWebServer();
        stws.doWork();
    }

}
