package scenario;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import utils.PortSettingsNew;
import utils.ScenarioConstants;
import utils.commands.CommandOutputFields;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;

public class ClientScenarioSocket {

    public static final Logger log = Logger.getLogger("com.netcracker.edu.ishop.client");

    public static void execute(String scenarioFileName) throws IOException, ClassNotFoundException {

        executeScenarioScript(scenarioFileName);
    }


    private static void executeScenarioScript(String scenarioFilename) throws IOException, AccessDeniedException {
        JsonParser parser = new JsonParser();
        if (ScenarioConstants.PERMIT_SCENARIO_EXECUTION) {

            System.out.println("Connecting to server via socket");

            Socket serverSocket = new Socket("localhost", PortSettingsNew.PORT_NUMBER);
            BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);


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
                            out.println(cmdData);

                            //receiving from server
                            String serverInput = in.readLine();

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
                out.close();
                in.close();
                serverSocket.close();
            }

        } else {
            log.info("Execution of scenarios is not allowed, because in 'ScenarioConstants.java' constant 'PERMIT_SCENARIO_EXECUTION'= false");
        }
    }

}
