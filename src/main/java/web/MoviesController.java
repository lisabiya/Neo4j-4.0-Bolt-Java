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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MoviesController {

//    private final Driver driver;
//
//    public MoviesController(Driver driver) {
//        this.driver = driver;
//    }
//
//    @GetMapping(path = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<JSONObject> getMovieTitles() {
////        Logger.getLogger("boltDriver").log(Level.INFO, "***return: " + title);
//
//        try (Session session = driver.session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
//            return session.run("MATCH (n:Partner) RETURN n LIMIT 20")
//                    .list(r -> {
//                        Map s = r.get("n").asNode().asMap();
//                        return (JSONObject) JSON.toJSON(s);
//                    });
//        }
//    }
}
