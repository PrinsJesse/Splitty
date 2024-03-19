/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import commons.Event;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import server.database.EventRepository;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EntityScan(basePackages = { "commons", "server" })
public class Main {

    public static String password;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        password = generatePassword(10);
        System.out.println("The new password for the admin panel is: " + password);
    }

    @Bean
    public CommandLineRunner run(EventRepository eventDB){
        return (args -> {
            insertEvent(eventDB);
            System.out.println(eventDB.findAll());
        });
    }

    private void insertEvent(EventRepository eventDB){
        eventDB.save(new Event("jesse"
                , new Date(10, 10, 2005)
                , "group 31"
                , "this is a test is the database works"));
    }

    private static String generatePassword(int passwordSize) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").substring(0, passwordSize);
    }

}