package com.tcs.airline.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.airline.model.Flight;
import com.tcs.airline.model.Passenger;
import com.tcs.airline.model.Reservation;
import com.tcs.airline.repository.FlightRepository;
import com.tcs.airline.repository.PassengerRepository;

/**
 * @author 
 *
 */
@RestController
@RequestMapping("/api")
public class PassengerController {
	
	public static final Logger logger = LoggerFactory.getLogger(PassengerController.class);

  @Autowired
  PassengerRepository passengerRepository;

  @Autowired
  FlightRepository flightRepository;

  @RequestMapping(value = "/passenger", method = RequestMethod.GET)
  public ResponseEntity<List<Passenger>> getPassenger() {
	  List<Passenger> pass = new ArrayList<>();
      passengerRepository.findAll().forEach(pass::add);
      return new ResponseEntity<List<Passenger>>(pass, new HttpHeaders(), HttpStatus.OK);
  }

  @RequestMapping(value = "/passenger/{id}", method = RequestMethod.GET)
  public ResponseEntity<Passenger> getPassanger(@PathVariable("id") String id)
      throws JSONException {

    Passenger p = passengerRepository.findById(id);
    if (p == null) {
    	logger.error("Unable to update. Passenger with id {} not found.", id);
      return new ResponseEntity<Passenger>(HttpStatus.BAD_REQUEST);

    } else {
      
    }
  }

  
  @RequestMapping(value = "/passenger/name/{name}", method = RequestMethod.GET)
  public ResponseEntity<Passenger> getPassengerbyName(@PathVariable("name") String name)
      throws JSONException {

    List<Passenger> pList = passengerRepository.findByFirstname(name);
    Passenger p=null;
    if(pList!=null && pList.size()>0) {
    	p= pList.get(0);
    }
    if (p == null) {
    	logger.error("Unable to update. Passenger with name {} not found.", name);
      return new ResponseEntity(HttpStatus.BAD_REQUEST);

    } else {
      System.out.println(p);
      return new ResponseEntity(p, HttpStatus.OK);
    }
  }
  

  @RequestMapping(value = "/passenger", method = RequestMethod.POST, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<Passenger> createPassanger(@RequestBody Passenger pass) {
    try {
      passengerRepository.save(pass);
      return new ResponseEntity<Passenger>(pass,HttpStatus.CREATED);

    } catch (Exception ex) {
      return new ResponseEntity<Passenger>(pass,
          HttpStatus.INTERNAL_SERVER_ERROR);

    }

  }


  @RequestMapping(value = "/passenger", method = RequestMethod.PUT)
  public ResponseEntity<String> updateLocation(@RequestBody Passenger pass) {
    try {
      passengerRepository.save(pass);
      return new ResponseEntity<String>("Passenger Updated" , HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity<String>("passenger with phone nbr already exists",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @RequestMapping(value = "/passenger/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<String> deletePassanger(@PathVariable("id") String id) throws JSONException {
    String st;
    Passenger p = passengerRepository.findById(id);
    if (p == null) {
      st = "Passenger with id " + id + " does not exist";
      return new ResponseEntity(st, HttpStatus.NOT_FOUND);
    } else {
      for (Reservation r : p.getReservations()) {
        for (Flight flight : r.getFlights()) {
          flight.setSeatsLeft(flight.getSeatsLeft() + 1);
          flightRepository.save(flight);
        }
      }
      passengerRepository.delete(id);
      st = "Passenger with Number " + id + " is deleted successfully";
      return new ResponseEntity<String>(st, HttpStatus.OK);
    }
  }
}
