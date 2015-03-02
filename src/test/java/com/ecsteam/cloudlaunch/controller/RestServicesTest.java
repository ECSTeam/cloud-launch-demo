package com.ecsteam.cloudlaunch.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ecsteam.cloudlaunch.TestContext;
import com.ecsteam.cloudlaunch.services.statistics.ApplicationStatisticsProvider;
import com.ecsteam.cloudlaunch.services.statistics.model.ApplicationStatistics;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes =
{ TestContext.class })
public class RestServicesTest
{
    @Autowired
    ApplicationStatisticsProvider applicationStatisticsProvider;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Before
    public void setup()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void testStatistics() throws Exception
    {
        
        MockHttpServletRequestBuilder getRequest = get("http://localhost:8080/services/statistics");
        
        when(applicationStatisticsProvider.getCurrentStatistics()).thenReturn(new ApplicationStatistics());
        
        mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("host", is("127.0.0.1")))
        .andExpect(jsonPath("port", is(80)));
    }
}
