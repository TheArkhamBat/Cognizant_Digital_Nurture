package com.cognizant.springlearn.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.cognizant.springlearn.Country;
import com.cognizant.springlearn.service.exception.CountryNotFoundException;

@Service
public class CountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    @SuppressWarnings("unchecked")
    public List<Country> getAllCountries() {
        LOGGER.info("START");
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        ArrayList<Country> countryList = (ArrayList<Country>) context.getBean("countryList", ArrayList.class);
        LOGGER.info("END");
        return countryList;
    }

    // Case-insensitive match on country code. A lambda/stream based lookup
    // could also be used here instead of the explicit loop.
    public Country getCountry(String code) throws CountryNotFoundException {
        LOGGER.info("START");
        LOGGER.debug("code: {}", code);

        for (Country country : getAllCountries()) {
            if (country.getCode().equalsIgnoreCase(code)) {
                LOGGER.info("END");
                return country;
            }
        }

        throw new CountryNotFoundException();
    }
}
