package com.tcs.airline.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonViewModule;
import com.tcs.airline.model.Flight;
import com.tcs.airline.model.Reservation;
import com.tcs.airline.repository.FlightRepository;
import com.tcs.airline.repository.PassengerRepository;
import com.tcs.airline.repository.ReservationRepository;
import com.tcs.airline.service.FlightService;

@RestController
@EnableAutoConfiguration
@RequestMapping("/api")
public class FlightController {


	@Autowired
	FlightRepository flightRepository;
	
	@Autowired
	FlightService flightService;

	@Autowired
	PassengerRepository passengerRepository;

	@Autowired
	ReservationRepository reservationRepository;


	@RequestMapping(value = "/flight", method = RequestMethod.GET)
	public ResponseEntity<List<Flight>> getFlight() {
		List<Flight> f = new ArrayList<>();
		try {
			f=flightService.getFlight();
			return new ResponseEntity(f, new HttpHeaders(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(f,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/flight/{number}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Flight> getFlight(@PathVariable("number") String number)
			throws JsonProcessingException, JSONException {
		Flight f = flightRepository.findOne();
		if (f == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);

		} else {
			return new ResponseEntity(f, HttpStatus.OK);
		}
	}

	// --------------------- POST Flight --------------------------------------

	@RequestMapping(value = "/flight/{flightNumber}", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> createOrUpdateFlight(@PathVariable("flightNumber") String flightNumber,
			@RequestBody Flight newFlight) {
			newFlight.setNumber(flightNumber);
			newFlight.setSeatsLeft(newFlight.getCapacity());
			if (!flightRepository.exists(flightNumber)) {
				newFlight = flightRepository.save(newFlight);
				String st= "new flight created";
				return new ResponseEntity(st,HttpStatus.CREATED);

			} else {
				// flight already exists, so update details
				Flight existingFlight = flightRepository.findOne(flightNumber);
				if (!checkFlightCapacity(existingFlight, newFlight.getCapacity())) {
					String st = "Cannot reduce capacity,active reservation count for this flight is higher than the target capacity ";
					return new ResponseEntity(st, HttpStatus.BAD_REQUEST);

				}

				List<Reservation> reservations = existingFlight.getReservations();
				if (!reservations.isEmpty()) {
					for (Reservation reservation : reservations) {
						List<Flight> reservedFlights = reservation.getFlights();
						reservedFlights.remove(existingFlight);
						reservedFlights.add(newFlight);
						reservation.setFlights(reservedFlights);
						reservationRepository.save(reservation);
					}
				}
				int oldPlaneCap = existingFlight.getCapacity();
				int newPlaneCap = newFlight.getCapacity();
				int changeInCapacity = Math.abs(oldPlaneCap - newPlaneCap);
				if (oldPlaneCap > newPlaneCap) {
					newFlight.setSeatsLeft(existingFlight.getSeatsLeft() - changeInCapacity);
				} else {
					newFlight.setSeatsLeft(existingFlight.getSeatsLeft() + changeInCapacity);
				}
				flightRepository.save(newFlight);
				return new ResponseEntity("Flight details Updated", HttpStatus.OK);
			}
	}

	// ------------------- Delete a
	// flight-----------------------------------------

	@RequestMapping(value = "/flight/{number}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteFlight(@PathVariable("number") String number) throws JSONException {
		Flight f = flightRepository.findOne(number);

		if (f == null) {
			return new ResponseEntity<String>("Flight with Number \" + number + \" does not exist", HttpStatus.NOT_FOUND);
		} else {
			if (f.getReservations().size() > 0) {
				return new ResponseEntity<String>(
						"Flight with Number " + number + " cannot be deleted , has one or more reservations",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			flightRepository.delete(number);
			return new ResponseEntity<String>("Flight deleted successfully", HttpStatus.OK);
		}
	}

	private boolean checkFlightCapacity(Flight flight, int newCap) {
		int currCap = flight.getCapacity();

		if (currCap < newCap) {
			return true;
		} else {
			if (newCap < (currCap - flight.getSeatsLeft())) {
				return false;
			}
		}
		return true;
	}

//	private boolean checkOverlap(Passenger passenger, List<Flight> temp) {
//
//		for (Flight f : temp) {
//			Date dTime = f.getDepartureTime();
//			for (Reservation r : passenger.getReservations()) {
//				for (Flight booked : r.getFlights()) {
//					if (!booked.equals(f)) {
//						if (dTime.compareTo(booked.getDepartureTime()) == 0) {
//							return true;
//						}
//					}
//				}
//			}
//		}
//		return false;
//	}
}
