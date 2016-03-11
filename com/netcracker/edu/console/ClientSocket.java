package com.netcracker.edu.console;

import com.netcracker.edu.utils.JSONUtils;
import org.apache.log4j.Logger;
import com.netcracker.edu.utils.SocketPortSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


public class ClientSocket {
    public static final Logger log = Logger.getLogger("com.netcracker.edu.console");

    public static void execute() throws IOException,ClassNotFoundException {
        System.out.println("Connecting to server via socket...");

        Socket serverSocket = new Socket("localhost", SocketPortSettings.PORT_NUMBER);
        BufferedReader outputFromSocketServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        PrintWriter inputToSocketServer = new PrintWriter(serverSocket.getOutputStream(), true);

        inputToSocketServer.println("Connected!");

        while (true) {
            System.out.println(">>>");

            try {
                String userInput = getInputStringCommand();

                if (userInput != null && userInput.equalsIgnoreCase("close")) {
                    System.out.println("Disconnected from socket server!");
                    System.exit(0);
                }
                else {

                    inputToSocketServer.println(userInput);
                    // -->
                    String serverOutput = outputFromSocketServer.readLine();

                    log.info(JSONUtils.createStringStatusFromCommandJsonFormat(serverOutput));
                }
            } catch (com.google.gson.JsonSyntaxException JSE) {
                log.info(JSE);

            } catch (SocketException se) {
                log.info(se);
                log.info("Lost connection. Is socket-server working?");
                break;
            }
            //log.info(serverInput);
            //hardcoded-style, pal
        }
//        inputToSocketServer.close();
//        outputFromSocketServer.close();
//        serverSocket.close();
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