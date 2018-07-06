package com.tcs.airline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.airline.controller.FlightController;
import com.tcs.airline.model.Flight;
import com.tcs.airline.repository.FlightRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class FlightControllerTest {


  private MockMvc mockMvc;
  
  @Autowired
  WebApplicationContext context;
  
  
  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }
  
  
  @Test
  public void acreateFlight() throws Exception{
     DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH");
     Date dTime = df.parse("2018-01-12-01");
     Date aTime = df.parse("2018-01-12-03");
    Flight f=new Flight("A565", "Chennai", "Bangalore", 2000, 200,
        "testdesc", aTime, dTime, "manufacturer", "model", 2009);
    byte[] fJson = toJson(f);
    mockMvc.perform(post("/api/flight/A565" ).content(fJson)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated());
  }


  
  
  @Test
  public void edeleteFlight() throws Exception{
    mockMvc.perform(delete("/api/flight/A565" )
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
  }
  


  private byte[] toJson(Object r) throws Exception {
    ObjectMapper map = new ObjectMapper();
    return map.writeValueAsString(r).getBytes();
}

  
}

