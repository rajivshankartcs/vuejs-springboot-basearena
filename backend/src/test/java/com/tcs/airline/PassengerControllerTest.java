package com.tcs.airline;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.airline.model.Flight;
import com.tcs.airline.model.Passenger;
import com.tcs.airline.repository.FlightRepository;
import com.tcs.airline.repository.PassengerRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class PassengerControllerTest {
 private MockMvc mockMvc;
  
  @Autowired
  WebApplicationContext context;
  
  @Autowired
  @Mock
  PassengerRepository passengerRepository;
  
  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }
  
 
  public void acreatePass() throws Exception{
    Passenger pass= new Passenger("Ram", "Ganesh", "Male", 20, "676767");
    byte[] passJson = toJson(pass);
    mockMvc.perform(post("/api/passenger" ).content(passJson)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated());
  }

  @Test
  public void bupdatePass() throws Exception{
//   MvcResult mm= mockMvc.perform(get("/api/passenger/phone/676767" )
//        .contentType(MediaType.APPLICATION_JSON)
//        .accept(MediaType.APPLICATION_JSON)).andDo(print()).andReturn();
    Passenger pass= new Passenger("1","Ramm", "Ganesh1", "Male", 22, "676767");
    byte[] passJson = toJson(pass);
    mockMvc.perform(put("/api/passenger" ).content(passJson)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
  }
  

  
  
  @Test
  public void edeletePass() throws Exception{
    mockMvc.perform(delete("/api/passenger/1" )
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
  }
  

  private byte[] toJson(Object r) throws Exception {
    ObjectMapper map = new ObjectMapper();
    return map.writeValueAsString(r).getBytes();
}

  
  
}
