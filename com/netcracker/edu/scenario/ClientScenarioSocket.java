package com.netcracker.edu.scenario;


import com.netcracker.edu.utils.JSONUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import com.netcracker.edu.utils.SocketPortSettings;
import com.netcracker.edu.utils.ScenarioConstants;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;

public class ClientScenarioSocket {

    private static final Logger log = Logger.getLogger("com.netcracker.edu.ishop.client");

    public static void execute(String scenarioFileName) throws IOException, ClassNotFoundException {

        executeScenarioScript(scenarioFileName);
    }


    private static void executeScenarioScript(String scenarioFilename) throws IOException, AccessDeniedException {

        if (ScenarioConstants.PERMIT_SCENARIO_EXECUTION) {

            System.out.println("Connecting to server via socket");

            Socket serverSocket = new Socket("localhost", SocketPortSettings.PORT_NUMBER);
            BufferedReader outputFromSocketServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            PrintWriter inputToSocketServer = new PrintWriter(serverSocket.getOutputStream(), true);

            inputToSocketServer.println("Connected!");

            File scenarioFile = new File(scenarioFilename);
            log.info("Path of the scenario file is: " + scenarioFilename);

            LineIterator lineIterator = null;

            try {
                lineIterator = FileUtils.lineIterator(scenarioFile, "UTF-8");
                long lineNumber = 0;
                while (lineIterator.hasNext()) {
                    lineNumber++;
                    String cmdData = lineIterator.next();
                    Thread.sleep(1);
                    if (!cmdData.startsWith("#")) {
                        try {
                            log.info(cmdData);

                            //sending to server
                            inputToSocketServer.println(cmdData);

                            //receiving from server
                            String serverOutput = outputFromSocketServer.readLine();

                            log.info(JSONUtils.createStringStatusFromCommandJsonFormat(serverOutput));


                        } catch (AccessDeniedException ADE) {
                            log.info(ADE);
                        } catch (SocketException se) {
                            log.info(se);
                            log.info("Lost connection. Is socket-server working?");

                        }
                    } else {
                        log.info("Scenario line " + lineNumber + ":\n\"" + cmdData + "\" was omitted for execution, because it\'s a comment");
                    }

                }
                log.info("Executing of scenario\'s file was finished");

            } catch (IOException IOE) {
                log.info("No scenario file was found! Is scenario\'s filepath\n" + scenarioFile + " correct?\n" + IOE);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LineIterator.closeQuietly(lineIterator);
                inputToSocketServer.close();
                outputFromSocketServer.close();
                serverSocket.close();
            }

        } else {
            log.info("Execution of scenarios is not allowed, because in 'ScenarioConstants.java' constant 'PERMIT_SCENARIO_EXECUTION'= false");
        }
    }

}
