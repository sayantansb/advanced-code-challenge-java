package com.statista.code.challenge;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.statista.code.challenge.database.DBData;
import com.statista.code.challenge.database.MockDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.zalando.jackson.datatype.money.MoneyModule;

import javax.annotation.PreDestroy;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.io.*;

@SpringBootApplication
@Slf4j
@EnableTransactionManagement
public class App{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new JtaTransactionManager(userTransaction(), transactionManager());
    }

    @Bean
    public UserTransaction userTransaction() {
        return new UserTransactionImp();
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public TransactionManager transactionManager() {
        return new UserTransactionManager();
    }

    @Bean
    public MoneyModule moneyModule() {
        return new MoneyModule();
    }


}
