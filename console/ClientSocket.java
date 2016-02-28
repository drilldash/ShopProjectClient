package console;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import utils.PortSettingsNew;
import utils.commands.CommandOutputFields;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


public class ClientSocket {
    public static final Logger log = Logger.getLogger("console");
    public static void execute() throws IOException,ClassNotFoundException {
        System.out.println("Connecting to server via socket");

        Socket serverSocket = new Socket("localhost", PortSettingsNew.PORT_NUMBER);
        BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
        JsonParser parser = new JsonParser();


        while (true) {
            System.out.println(">>>");


            try {
                String userInput = getInputStringCommand();
                out.println(userInput);
                String serverInput = in.readLine();
                log.info(serverInput);
                JsonObject json = parser.parse(serverInput).getAsJsonObject();

                String statusHeader = CommandOutputFields.STATUS.toString();
                String codeHeader = CommandOutputFields.CODE.toString();
                String contentHeader = CommandOutputFields.CONTENT.toString();

                if (json.has(statusHeader) && json.has(codeHeader) && json.has(contentHeader)) {
                    String status = "Status: " + json.get(statusHeader).getAsString();
                    String statusCode = "Code: " + json.get(codeHeader).getAsString();
                    String content = "Content: " + json.get(contentHeader).getAsString();
                    log.info(status + "\n" + statusCode + "\n" + content);
                } else {
                    log.info(serverInput);
                }

                if (userInput != null && userInput.equalsIgnoreCase("close")) break;
            } catch (com.google.gson.JsonSyntaxException JSE) {
                log.info(JSE);
                JsonObject json = new JsonObject();
            } catch (SocketException se) {
                log.info(se);
                log.info("Lost connection. Is socket-server working?");
                break;
            }
            //log.info(serverInput);
            //hardcoded-style, pal



        }
        out.close();
        in.close();
        serverSocket.close();
    }

    private static String getInputStringCommand() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException ioe) {
            log.info("Exception during user input");
            ioe.printStackTrace();
            return null;
        } catch (NullPointerException NPE) {
            log.info("Exception during user input " + NPE);
        }
        return null;
    }
}