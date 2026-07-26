package com.cognizant.springlearn.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Checked exception on purpose: the hands-on doc explicitly asks for a
// "throws" clause on the controller method, which only makes sense for a
// checked exception.
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Country not found")
public class CountryNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public CountryNotFoundException() {
        super("Country not found");
    }
}
