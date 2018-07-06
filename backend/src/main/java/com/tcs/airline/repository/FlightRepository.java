package com.tcs.airline.repository;

import com.tcs.airline.model.Flight;

import org.springframework.data.repository.CrudRepository;


public interface FlightRepository extends CrudRepository<Flight, String> {

}
