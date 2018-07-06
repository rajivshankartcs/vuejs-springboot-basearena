package com.tcs.airline.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.airline.model.Flight;
import com.tcs.airline.model.Passenger;
import com.tcs.airline.model.Reservation;
import com.tcs.airline.repository.FlightRepository;
import com.tcs.airline.repository.PassengerRepository;
import com.tcs.airline.repository.ReservationRepository;

@RestController
@EnableAutoConfiguration
@RequestMapping("/api")
public class ReservationController {

  @Autowired
  ReservationRepository resRepository;

  @Autowired
  FlightRepository fRepository;

  @Autowired
  PassengerRepository pRepository;

  @RequestMapping(value = "/reservation/{order_number}", method = RequestMethod.GET)
  public ResponseEntity<Reservation> getReservationOrder(@PathVariable("order_number") String order_number)
      throws JSONException {
    Reservation p = resRepository.findOne(order_number);
    if (p == null) {
      return new ResponseEntity<Reservation>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<Reservation>(p, HttpStatus.OK);
  }
  
  
  @RequestMapping(value = "/reservation", method = RequestMethod.GET)
  public ResponseEntity<List<Reservation>> getAllReservation() {
      List<Reservation> res = new ArrayList<>();
      resRepository.findAll().forEach(res::add);
      return new ResponseEntity<List<Reservation>>(res, new HttpHeaders(), HttpStatus.OK);
  }
 



  @RequestMapping(value = "/reservation/{passengerId}/{flightId}", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<String> createReservation(@PathVariable String passengerId, @PathVariable String flightId) {

    Reservation reservation = new Reservation();
    Passenger p = pRepository.findById(passengerId);

    if (p == null) {
      String st = "Passenger with id " + passengerId + " does not exist";
      return new ResponseEntity<String>(st, HttpStatus.NOT_FOUND);
    }
    
    List<Flight> flights = new ArrayList<Flight>();

    int price = 0;


      Flight f = fRepository.findOne();
      // flights.add(f);
      // check time overlap
      Date dTime = f.getDepartureTime();
      for (Reservation r : p.getReservations()) {
        for (Flight pf : r.getFlights()) {
          if (pf.getDepartureTime().compareTo(dTime) == 0) {
            String st = "Reservation time overlap. .Reservation cannot be created";
            return new ResponseEntity(st, HttpStatus.BAD_REQUEST);
          }
        }
      }
      if (f.getSeatsLeft() > 0) {
    	List<Passenger> pList = new ArrayList<Passenger>();
        pList = f.getPassengers();
        pList.add(p);
        price += f.getPrice();
        f.setSeatsLeft(f.getSeatsLeft() - 1);
        f.setPassengers(pList);
        fRepository.save(f);
      } else {
        String st = "Reservation cannot be created. No seats available in flight " + f.getNumber();
        return new ResponseEntity(st, HttpStatus.BAD_REQUEST);
      }
      // }
      // }
      reservation.setPassenger(p);
      reservation.setPrice(price);
      flights.add(f);
      reservation.setFlights(flights);
      System.out.println("post reservation");
      System.out.println(reservation.getOrderNumber() + "|" + reservation.getPrice() + "|"
          + reservation.getFlights().size());
      reservation.setFlightNumber(flightId);
      reservation.setPassengerNumber(passengerId);
      reservation = resRepository.save(reservation);
      System.out.println(reservation);
      // reservation = resRepository.findOne(reservation.getOrderNumber());
      return new ResponseEntity(reservation, HttpStatus.CREATED);
  }

  // ------------------- Delete a
  // reservation-----------------------------------------

  @RequestMapping(value = "/reservation/{id}", method = RequestMethod.DELETE)
  // @ResponseStatus(value = HttpStatus.ACCEPTED)
  public ResponseEntity<?> deleteReservation(@PathVariable("id") String id) throws JSONException {

    Reservation p = resRepository.findOne(id);
    if (p == null) {
      String st = "Reservation not found";
      return new ResponseEntity();

    } else {
      List<Flight> f_list = p.getFlights();
      for (Flight f : f_list) {
        int cnt = f.getSeatsLeft();
        if (cnt > 0) {
          f.setSeatsLeft(++cnt);
        }
        fRepository.save(f);
      }

        Reservation reservation = resRepository.findOne(id);
        Passenger passenger = reservation.getPassenger();
        // remove passenger from flight and udpate seats
        for (Flight flight : reservation.getFlights()) {
          flight.setSeatsLeft(flight.getSeatsLeft() + 1);
          flight.getPassengers().remove(passenger);
          fRepository.save(flight);
        }
        resRepository.delete(id);
        String st = "Reservation with id " + id + " is deleted successfully";
        return new ResponseEntity(st, HttpStatus.OK);
    }

  }

  // @ExceptionHandler(BadHttpRequest.class)
  // @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  // public Map<String, Object>
  // handleUnsupportedMediaTypeException(HttpMediaTypeNotSupportedException ex)
  // throws IOException {
  // Map<String, Object> map = new HashMap();
  // map.put("code", "404");
  // // map.put("cause", ex.getLocalizedMessage());
  // map.put("msg", "Not found");
  // return map;
  // }
}
