import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This Class decouples submitting request-handling tasks from task execution via an Executor.  The behaviour of this
 * class can be easily tweaked by changing the <em>Executor</em> implementation.
 *
 */
public class TaskExecution implements Server {

    private final static int NUMBER_OF_THREADS = 100;
    private static final int port = 59090;
    private static final Executor exec = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    @Override
    public void doWork() {
        try {
            var listener = new ServerSocket(port);
            while (true) {
                final Socket s = listener.accept();
                Runnable r = () -> handleTask(s);
                /**
                 * Executor configuration is usually a one-time task, so this code benefits from a better design (decoupling of task
                 * submission and task execution) as well as more readily-accessible thread 'management' code locations.
                 */
                exec.execute(r);
                System.out.println("doWork: number of threads? " + java.lang.Thread.activeCount());
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
            out.println("\nTask execution server thread - " + Thread.currentThread() + " - accepted the input : "
                    + in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                System.out.println("Closed: " + connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(new Date().toString());
    }

    public static void main(String[] args) {
        System.out.println("main: number of threads? " + java.lang.Thread.activeCount());
        TaskExecution te = new TaskExecution();
        te.doWork();
    }

}
    