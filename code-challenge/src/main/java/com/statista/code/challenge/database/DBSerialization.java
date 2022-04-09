package com.statista.code.challenge.database;

import com.statista.code.challenge.dao.AppDAO;
import com.statista.code.challenge.database.DBData;
import com.statista.code.challenge.database.MockDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@Profile("!test")
public class DBSerialization implements CommandLineRunner {

    /**
     * Purpose of this method is to initialize the database with the state in previous application context load
     * If you do not want to store states between context loads, you can remove this class
     *
     * @param args
     * @throws Exception
     */

    @Autowired
    private AppDAO appDAO;

    @Override
    public void run(String... args) throws Exception {
        String rootPath = System.getProperty("user.dir");
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(rootPath.concat("/code-challenge/src/main/resources/database.txt")));
        DBData dbData = (DBData) objectInputStream.readObject();
        objectInputStream.close();
        MockDB.bookingMap.putAll(dbData.bookingMap);
        MockDB.bookingIdListByDepartmentIdMap.putAll(dbData.bookingIdListByDepartmentIdMap);
        MockDB.pricesForBookingsByCurrencyMap.putAll(dbData.pricesForBookingsByCurrencyMap);
        appDAO.getIdGeneratorMap().put("booking",new AtomicInteger(MockDB.bookingMap.size()));
    }

    /**
     * Purpose of this method is to serialize the present database snapshot in the filedatabase.txt
     * This snapshot will be used to initialize the database next time when application context loads
     * @throws IOException
     */
    @PreDestroy
    public void destroy() throws IOException {
        String rootPath = System.getProperty("user.dir");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(rootPath.concat("/code-challenge/src/main/resources/database.txt")));
        DBData dbData = new DBData();
        dbData.bookingMap = MockDB.bookingMap;
        dbData.bookingIdListByDepartmentIdMap = MockDB.bookingIdListByDepartmentIdMap;
        dbData.pricesForBookingsByCurrencyMap = MockDB.pricesForBookingsByCurrencyMap;
        objectOutputStream.writeObject(dbData);
        objectOutputStream.close();
    }
}
