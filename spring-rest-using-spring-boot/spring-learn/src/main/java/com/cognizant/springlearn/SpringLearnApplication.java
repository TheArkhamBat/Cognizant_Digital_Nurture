package com.cognizant.springlearn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class SpringLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        LOGGER.info("START");
        SpringApplication.run(SpringLearnApplication.class, args);

        SpringLearnApplication app = new SpringLearnApplication();
        app.displayDate();
        app.displayCountry();
        app.displayPrototypeScope();
        app.displayCountries();
        LOGGER.info("END");
    }

    // Hands on 2: load a SimpleDateFormat bean from date-format.xml and use it
    public void displayDate() {
        LOGGER.info("START");
        ApplicationContext context = new ClassPathXmlApplicationContext("date-format.xml");
        SimpleDateFormat format = context.getBean("dateFormat", SimpleDateFormat.class);
        try {
            Date date = format.parse("31/12/2018");
            LOGGER.debug("Parsed date: {}", date);
        } catch (Exception e) {
            LOGGER.error("Error parsing date", e);
        }
        LOGGER.info("END");
    }

    // Hands on 4 + 5 (singleton half): load the "country" bean twice from the
    // same context and show both references point at the same instance
    public void displayCountry() {
        LOGGER.info("START");
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        Country country = context.getBean("country", Country.class);
        Country anotherCountry = context.getBean("country", Country.class);
        LOGGER.debug("Country : {}", country.toString());
        LOGGER.debug("Singleton scope -- same instance? {}", country == anotherCountry);
        LOGGER.info("END");
    }

    // Hands on 5 (prototype half): "countryPrototype" bean is scope="prototype",
    // so every getBean() call must return a different instance
    public void displayPrototypeScope() {
        LOGGER.info("START");
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        Country country = context.getBean("countryPrototype", Country.class);
        Country anotherCountry = context.getBean("countryPrototype", Country.class);
        LOGGER.debug("Prototype scope -- same instance? {}", country == anotherCountry);
        LOGGER.info("END");
    }

    // Hands on 6: load the full countryList ArrayList bean and display it
    @SuppressWarnings("unchecked")
    public void displayCountries() {
        LOGGER.info("START");
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        ArrayList<Country> countryList = (ArrayList<Country>) context.getBean("countryList", ArrayList.class);
        for (Country country : countryList) {
            LOGGER.debug("Country : {}", country.toString());
        }
        LOGGER.info("END");
    }
}
