package scenario;
import org.apache.log4j.Logger;

public class PlayClientScenarioSocket {
    public static final Logger log = Logger.getLogger("com.netcracker.edu.ishop.client");

    public static void main(String[] args) throws Exception {
        try {
            ClientScenarioSocket.execute("D:/ShopProject/scenario/command_sequence.script");

            // TODO: Is it good to use Common Exception instead of some concrete Exception Types?
        } catch (Exception e) {
            log.info(e);
            System.exit(0);
        }
    }
}
