import com.alibaba.fastjson.JSON;
import org.neo4j.driver.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class boltDriver {
    public static void main(String[] args) {
        getPartner();
    }

    private static void getPartner() {
        Driver driver = GraphDatabase.driver("bolt://10.0.17.123:7697",
                AuthTokens.basic("neo4j", "904028997"));

        try (Session session = driver.session()) {
            session.run("MATCH (n:Movie) RETURN n LIMIT 25").stream()
                    .map(r -> r.get("n").asNode())
                    .map(n -> JSON.toJSON(n.asMap()).toString())
                    .forEach(s -> {
                        Logger.getLogger("boltDriver").log(Level.INFO, s);
                    });
        }
        driver.close();
    }
}
