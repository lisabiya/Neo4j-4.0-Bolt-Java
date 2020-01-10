/*
 * Copyright (c) 2019-2020 "Neo4j,"
 * Neo4j Sweden AB [https://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MoviesController {

    private final static String getHolderRel = "MATCH (pa:Partner{contributive_initiator:$name})-[:SHAREHOLDING]-(com:Company{company:$company})\n" +
            "MATCH p=(pa)-[*..4]-() \n" +
            "WHERE ALL (r IN relationships(p) WHERE type(r) in ['SHAREHOLDING'])\n" +
            "with p,nodes(p) as nodes,relationships(p) as relationships\n" +
            "unwind nodes as node\n" +
            "with p,relationships,collect({name:coalesce(node.company, node.contributive_initiator),id:coalesce(node.company_md5, node.key_id),type:labels(node)[0]}) as nodes\n" +
            "unwind relationships as r\n" +
            "with p,nodes,case type(r) when 'SHAREHOLDING2' then \"控股\" when \"SUB_COMPANY\" then \"子公司\" ELSE '其他' END AS result\n" +
            "with p,nodes,collect({type:result}) as relationships\n" +
            "RETURN {nodes:nodes,relationships:relationships}  as path";


    private final Driver driver;

    public MoviesController(Driver driver) {
        this.driver = driver;
    }

    @RequestMapping(path = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getMovieTitles() {

        try (Session session = driver.session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            return session.run("MATCH (n:Partner) RETURN n LIMIT 20")
                    .list(r -> {
                        Map s = r.get("n").asNode().asMap();
                        return (JSONObject) JSON.toJSON(s);
                    });
        }
    }

    @RequestMapping(path = "/getHolderRel", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getHolderRel(@RequestParam String name, @RequestParam String company) {

        try (Session session = driver.session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            return session.run(getHolderRel, Map.of("name", name, "company", company))
                    .list(r -> {
                        Map s = r.get("path").asMap();
                        return (JSONObject) JSON.toJSON(s);
                    });
        }
    }

}
