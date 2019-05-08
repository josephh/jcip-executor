import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


/**
 * ExplicitThreadPerTask creates a new thread to service each request.
 *
 * This server creates a new thread for each request.  It is an improvement on {@link SingleThreadForAllTasks} but will
 * be problematic as the number of threads increases.
 * It's improvement are,
 * <ul>
 *     <li>Task processing is off-loaded from the main thread, allowing the main thread to return to handling
 *     incoming requests sooner, so that new requests can be handled before processing of the previous request has
 *     finished.</>
 *     <li>Tasks can be processed in parallel, meaning that potentially many requests can be handled at the same
 *     time, which may improve throughput.</>
 *     <li>Task-handling code must be thread-safe!</>
 * <ul/>
 *
 * So long as the request arrival rate does not exceed the server's capacity to handle requests, this approach offers
 * reasonable responsiveness and throughput.
 *
 * The disadvantages of unbounded thread creation are,
 * <ul>
 *     <li>Thread creation and teardown are not <em>free</em> operations.  Both the JVM and OS must commit processing
 *     time to this overhead.</li>
 *     <li>Active threads consume system resources, especially memory, even when sitting idle.  Idle threads put
 *     pressure on garbage collection and compete for CPU time.</>
 *     <li>There is a limit to the maximum number that can be created, which varies depending on platform and JVM
 *     parameters.  Reaching the limit will probably result in an OutOfMemoryError.</>
 * <ul/>
 *
 */
public class ExplicitThreadPerTask implements Server {

    static final int port = 59090;

    public void doWork() {

        try{
            var listener = new ServerSocket(port);
            while (true) {
                var socket = listener.accept(); /** ServerSocket.accept() blocks until a connection is made.
                                                * The socket must be explicity closed in the
                                                * thread doing the work, since it is created
                                                * in a different (main) thread */
                Runnable r = () -> handleTask(socket);
                new Thread(r).start();  // one thread for every request
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
            out.println("New server thread accepted the input : " + in.readLine());
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
        ExplicitThreadPerTask etpt = new ExplicitThreadPerTask();
        etpt.doWork();
    }

}





