import org.neo4j.driver.*;
import org.neo4j.driver.summary.ResultSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class boltDriver {
    private final static String query = "call apoc.bolt.load(\"bolt://neo4j:904028997@10.0.17.123:7687\",\n" +
            "\"MATCH (n:Partner)-[r:SHAREHOLDING2]-(com) return n,r,com skip $skip limit $limit \", null) yield row\n" +
            "with row.n.properties as p,row.r.properties as rel,row.com.properties as com\n" +
            "MERGE(pa:Partner{key_id:coalesce(p.key_id,p.OriginID)})\n" +
            "ON CREATE SET \n" +
            "pa+=p{.contributive_initiator,.paid_contributive_date,.contributive_id,.subcribe_contributive_way\n" +
            " ,.date_posted,.subcribe_contributive_date,.paid_contributive_limit,.key_id,.OriginType,.OriginID},\n" +
            "pa.created = timestamp()\n" +
            "MERGE(company:Company{company:com.company})\n" +
            "ON CREATE SET \n" +
            "company+=com{.*},company.created = timestamp()\n" +
            "MERGE(pa)-[r:SHAREHOLDING]-(company)\n" +
            "ON CREATE SET r=rel{.*},r.desc=\"控股\"";

    private final static Logger LOGGER = LoggerFactory.getLogger(boltDriver.class);


    public static void main(String[] args) {
        getPartner();
    }

    private static void getPartner() {
        Driver driver = GraphDatabase.driver("bolt://10.0.17.123:7697",
                AuthTokens.basic("neo4j", "904028997"));

        try (Session session = driver.session()) {

            for (int i = 1061; i < 10000; i++) {
                String sessionQuery = query.replace("$skip", i * 10000 + "");
                sessionQuery = sessionQuery.replace("$limit", "10000");
                ResultSummary result = session.run(sessionQuery).consume();
                LOGGER.debug("***************|" + i + "|**************************************");
                LOGGER.debug("***return: " + result.counters().nodesCreated());
                LOGGER.debug("*****************************************************");

            }
        }
        driver.close();
    }
}
