import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * SingleThreadForAllTasks processes requests arriving at its port sequentially.
 * <p>
 * This server is simple and theoretically correct but it will exhibit poor performance as it can only handle one
 * request at a time.
 */
public class SingleThreadForAllTasks implements Server {

    static final int port = 59090;

    public void doWork() {

        try (var listener = new ServerSocket(port)) { /** Java try-with-resources, ensures
         * declared resource is closed at end of try block but we can only use this where the main thread is the same
         * as the thread doing the work.  If we create a new thread it will not be scoped to this try block.
         * Any object that implements java.lang.AutoCloseable (including implementations of java.io.Closeable)
         * System.out.println("The server is running...on port " + port);
         */
            System.out.println("The server is running...on port " + port);
            while (true) {
                try (var socket = listener.accept()) {
                    System.out.println("doWork: number of threads? " + java.lang.Thread.activeCount());
                    handleTask(socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handleTask(Socket connection) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
            out.println("Single-threaded server accepted the input : " + in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(new Date().toString());
    }

    public static void main(String[] args) {
        System.out.println("main: number of threads? " + java.lang.Thread.activeCount());
        SingleThreadForAllTasks stws = new SingleThreadForAllTasks();
        stws.doWork();
    }

}
