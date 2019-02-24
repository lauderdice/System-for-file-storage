package cz.vmacura.ear.upload.rest;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vmacura.ear.upload.environment.Environment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static cz.vmacura.ear.upload.environment.Environment.createDefaultMessageConverter;
import static cz.vmacura.ear.upload.environment.Environment.createStringEncodingMessageConverter;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.junit.Assert.assertEquals;

public class BaseControllerTest {

    ObjectMapper objectMapper;

    MockMvc mockMvc;

    public void setUp(Object controller) {
        setupObjectMapper();
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(createDefaultMessageConverter(),
                        createStringEncodingMessageConverter())
                .setUseSuffixPatternMatch(false)
                .build();

    }

    void setupObjectMapper() {
        this.objectMapper = Environment.getObjectMapper();
    }

    String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    <T> T readValue(MvcResult result, Class<T> targetType) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), targetType);
    }

    <T> T readValue(MvcResult result, TypeReference<T> targetType) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), targetType);
    }

    void verifyLocationEquals(String expectedPath, MvcResult result) {
        final String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals("http://localhost:8080/upload" + expectedPath, locationHeader);
    }
}
