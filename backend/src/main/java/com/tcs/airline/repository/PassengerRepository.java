package com.tcs.airline.repository;

import com.tcs.airline.model.Passenger;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PassengerRepository extends CrudRepository<Passenger, String> {

  List<Passenger> findByFirstname(String firstname);

  Passenger findById(String id);
  
  Passenger findByPhone(String phone);

  @Override
  void delete(String id);

  /* @Query("select p from flight_passenger where passenger_id=") */
}
