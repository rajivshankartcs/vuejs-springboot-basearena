package com.tcs.airline.repository;

import com.tcs.airline.model.Passenger;
import com.tcs.airline.model.Reservation;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, String> {
  
	
}
