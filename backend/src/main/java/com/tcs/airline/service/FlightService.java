package com.tcs.airline.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.airline.model.Flight;
import com.tcs.airline.repository.FlightRepository;

@Service
public class FlightService {
	@Autowired
	FlightRepository flightRepository;

	
	public List<Flight> getFlight(){
		List<Flight> f = new ArrayList<>();
		flightRepository.findAll().forEach(f::add);
		return f;
	}
}
