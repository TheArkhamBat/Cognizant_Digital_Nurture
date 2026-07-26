package com.cognizant.springlearn;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cognizant.springlearn.controller.CountryController;

// NOTE: this project layers Spring Security (see the JWT-handson doc) on top
// of the REST services built in the earlier docs, so every request below is
// sent with HTTP Basic credentials via httpBasic("user", "pwd") -- otherwise
// Spring Security would reject them with 401 before they ever reach MockMvc's
// assertions.
@SpringBootTest
@AutoConfigureMockMvc
class SpringLearnApplicationTests {

    @Autowired
    private CountryController countryController;

    @Autowired
    private MockMvc mvc;

    @Test
    void contextLoads() {
        assertNotNull(countryController);
    }

    @Test
    void testGetCountry() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/in").with(httpBasic("user", "pwd")));
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.code").exists());
        actions.andExpect(jsonPath("$.code").value("IN"));
        actions.andExpect(jsonPath("$.name").exists());
        actions.andExpect(jsonPath("$.name").value("India"));
    }

    @Test
    void testGetCountryException() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/az").with(httpBasic("user", "pwd")));
        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Country not found"));
    }

    @Test
    void testUpdateEmployeeException() throws Exception {
        String employeeJson = "{"
                + "\"id\":999,"
                + "\"name\":\"Ghost Employee\","
                + "\"salary\":1000,"
                + "\"permanent\":true,"
                + "\"dateOfBirth\":\"01/01/2000\""
                + "}";

        ResultActions actions = mvc.perform(put("/employees")
                .with(httpBasic("user", "pwd"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson));

        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Employee not found"));
    }
}
