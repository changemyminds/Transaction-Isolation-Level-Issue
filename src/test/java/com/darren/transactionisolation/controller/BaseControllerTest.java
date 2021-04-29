package com.darren.transactionisolation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseControllerTest {
    protected final static ResultMatcher STATUS_OK = status().isOk();
    protected final static ResultMatcher STATUS_CREATED = status().isCreated();
    protected final static ResultMatcher STATUS_BAD_REQUEST = status().isBadRequest();
    protected final static ResultMatcher STATUS_UNAUTHORIZED = status().isUnauthorized();
    protected final static ResultMatcher STATUS_FORBIDDEN = status().isForbidden();
    protected final static ResultMatcher STATUS_NOT_FOUND = status().isNotFound();
    protected final static ResultHandler PRINT = print();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected Environment environment;

    @BeforeAll
    public void setupAll() {
        System.out.println("Profiles: " + getActiveProfiles());
    }

    protected String getActiveProfiles() {
        return String.join(",", environment.getActiveProfiles());
    }

    protected <T> T fromResult(ResultActions resultActions, Class<T> tClass) throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(getJsonResponse(resultActions), tClass);
    }

    protected <T> T fromResult(ResultActions resultActions, TypeReference<T> typeReference) throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(getJsonResponse(resultActions), typeReference);
    }

    protected String getJsonResponse(ResultActions resultActions) throws UnsupportedEncodingException {
        return resultActions.andReturn().getResponse().getContentAsString();
    }

    protected ResultActions performPost(String urlTemplate, Object object) throws Exception {
        return mockMvc.perform(post(urlTemplate)
                .content(objectMapper.writeValueAsString(object)));
    }

    protected ResultActions performPost(String urlTemplate, Consumer<MockHttpServletRequestBuilder> consumer) throws Exception {
        MockHttpServletRequestBuilder builder = post(urlTemplate);
        if (consumer != null) {
            consumer.accept(builder);
        }
        return mockMvc.perform(builder);
    }

    protected ResultActions performGet(String urlTemplate) throws Exception {
        return performGet(urlTemplate, null);
    }

    protected ResultActions performGet(String urlTemplate, Consumer<MockHttpServletRequestBuilder> consumer) throws Exception {
        MockHttpServletRequestBuilder builder = get(urlTemplate);
        if (consumer != null) {
            consumer.accept(builder);
        }
        return mockMvc.perform(builder);
    }
}
