package com.javatechie.elk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
public class ElkStackExampleApplication {

    @GetMapping("/getUser/{id}")
    public User getUserById(@PathVariable int id) throws Exception {
        UUID commonUuid = UUID.randomUUID();

        List<User> users = getUsers();
        User user = users.stream()
                .filter(u -> u.getId() == id)
                .findAny()
                .orElse(null);

        if (user != null) {
            LoggingUtil.logAuditGetUserById("getUserById", "Successfully retrieved user by id", user, commonUuid);
            LoggingUtil.logTechGetUserByIdInfo("getUserById", "Successfully retrieved user by id", commonUuid);
            return user;
        } else {
            Exception exception = new Exception("User with ID: " + id + " not found.");
            LoggingUtil.logTechGetUserByIdError("getUserById", exception, commonUuid);
            throw exception;

        }
    }

    private List<User> getUsers() {
        return Stream.of(new User(1, "John"),
                        new User(2, "Shyam"),
                        new User(3, "Rony"),
                        new User(4, "mak"))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        SpringApplication.run(ElkStackExampleApplication.class, args);
    }
}
