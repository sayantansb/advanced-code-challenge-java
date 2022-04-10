package com.statista.code.challenge;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.client.RestTemplate;
import org.zalando.jackson.datatype.money.MoneyModule;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

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
    public RestTemplate restTemplate(){

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new MoneyModule().withQuotedDecimalNumbers()
                .withAmountFieldName("price")
                .withCurrencyFieldName("currency"));

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().removeIf(messageConverter -> messageConverter.getClass() == MappingJackson2HttpMessageConverter.class);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter(objectMapper));
        return restTemplate;
    }

    @Bean
    public MoneyModule moneyModule() {
        return new MoneyModule()
                .withAmountFieldName("price")
                .withCurrencyFieldName("currency");
    }

}
