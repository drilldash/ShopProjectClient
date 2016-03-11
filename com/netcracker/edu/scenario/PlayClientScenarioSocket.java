package com.netcracker.edu.scenario;

import org.apache.log4j.Logger;

import java.util.*;

public class PlayClientScenarioSocket {
    private static final Logger log = Logger.getLogger("com.netcracker.edu.ishop.client");

    public static void main(String[] args) throws Exception {
        cyclicstart();
        //simplestart();
    }

    public static void cyclicstart() {
        Random rand = new Random(System.currentTimeMillis());

        List<String> listOfScriptNames = new ArrayList<>();
        listOfScriptNames.add("D:/ShopProject/scenario/scenario0.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario1.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario2.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario3.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario4.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario5.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario6.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario7.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario8.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario9.script");



        for (int i = 1; i < 2; i++) {
            try {
                //ClientScenarioSocket.execute("D:/ShopProject/scenario/command_sequence.script");
                //ClientScenarioSocket.execute("D:/ShopProject/scenario/make_admins.script");
                //System.out.println(choice(listOfScriptNames, rand));
                ClientScenarioSocket.execute(choice(listOfScriptNames, rand));

                // TODO: Is it good to use Common Exception instead of some concrete Exception Types?
            } catch (Exception e) {
                log.info(e);
                System.exit(0);
            }

        }


    }

    public static void simplestart() {

        Random rand = new Random(System.currentTimeMillis());

        List<String> listOfScriptNames = new ArrayList<>();
        listOfScriptNames.add("D:/ShopProject/scenario/scenario1.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario2.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario3.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario4.script");
        listOfScriptNames.add("D:/ShopProject/scenario/scenario5.script");


        try {
            //ClientScenarioSocket.execute("D:/ShopProject/scenario/command_sequence.script");
            //System.out.println(choice(listOfScriptNames, rand));
            //ClientScenarioSocket.execute(choice(listOfScriptNames, rand));
            ClientScenarioSocket.execute("D:/ShopProject/scenario/make_admins.script");

            // TODO: Is it good to use Common Exception instead of some concrete Exception Types?
        } catch (Exception e) {
            log.info(e);
            System.exit(0);
        }
    }


    public static <E> E choice(Collection<? extends E> coll, Random rand) {
        if (coll.size() == 0) {
            return null; // or throw IAE, if you prefer
        }

        int index = rand.nextInt(coll.size());
        if (coll instanceof List) { // optimization
            return ((List<? extends E>) coll).get(index);
        } else {
            Iterator<? extends E> iter = coll.iterator();
            for (int i = 0; i < index; i++) {
                iter.next();
            }
            return iter.next();
        }
    }
}
