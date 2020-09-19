package com.halfnet.myyearbook.webservice;

import com.halfnet.myyearbook.webservice.util.LoggingConstants;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan
@EntityScan({"com.halfnet.myyearbook.webservice.entities"})
@EnableJpaRepositories({"com.halfnet.myyearbook.webservice.repos"})
public class Main {
    
    private final Log log = LogFactory.getLog(LoggingConstants.MAIN_LOG);
    
    @PostConstruct
    private void init(){
        log.info("Server started");
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
