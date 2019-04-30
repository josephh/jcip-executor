import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class RequestHandler {

    public static void handleRequest(Socket conn) throws IOException {
        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

        PrintWriter out = new PrintWriter(conn.getOutputStream(), true);

        out.println("Received input : " + in.readLine());
        out.println(new Date().toString());
    }
}
