package com.netcracker.edu.console;

import org.apache.log4j.Logger;

public class PlayClientSocket {
    public static final Logger log = Logger.getLogger("com.netcracker.edu.ishop.client");

    public static void main(String[] args) throws Exception {
//        try {
//            console.ClientSocket.execute();
//            // TODO: Is it good to use Common Exception instead of some concrete Exception Types?
//        } catch (Exception e) {
//            log.info(e);
//            //System.exit(0);
//        }

        ClientSocket.execute();

    }
}
