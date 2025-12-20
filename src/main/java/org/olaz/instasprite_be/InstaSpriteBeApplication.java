package org.olaz.instasprite_be;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@SpringBootApplication
public class InstaSpriteBeApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone", "Asia/Ho_Chi_Minh");
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        
        SpringApplication.run(InstaSpriteBeApplication.class, args);
    }

}
