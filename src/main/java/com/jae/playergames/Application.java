package com.jae.playergames;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("************ backend api service start**************");
        SpringApplication.run(Application.class, args);
    }

}
