package com.tcs.airline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reservation")
public class Reservation {

@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String orderNumber;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "passenger_id")
  private Passenger passenger;

  private int price; // sum of each flight’s price.

  @JsonIgnore
  @ManyToMany
  @JoinTable(name = "reservation_flight", joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "orderNumber"), inverseJoinColumns = @JoinColumn(name = "flight_id", referencedColumnName = "flight_number"))
  private List<Flight> flights;
  
  private String passengerNumber;
  
  private String flightNumber;

  public String getOrderNumber() {
    return orderNumber;
  }

  public Passenger getPassenger() {
    return passenger;
  }

  public void setPassenger(Passenger passenger) {
    this.passenger = passenger;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public List<Flight> getFlights() {
    return flights;
  }

  public void setFlights(List<Flight> flights) {
    this.flights = flights;
  }

  public String getPassengerNumber() {
    return passengerNumber;
  }

  public void setPassengerNumber(String passengerNumber) {
    this.passengerNumber = passengerNumber;
  }

  public String getFlightNumber() {
    return flightNumber;
  }

  public void setFlightNumber(String flightNumber) {
    this.flightNumber = flightNumber;
  }

  // public JSONObject getFullJSON() throws JSONException
  // {
  // JSONObject result = new JSONObject();
  // JSONObject reserv=new JSONObject();
  // reserv.put("orderNumber",this.getOrderNumber());
  // reserv.put("price",this.getPrice());
  // JSONArray flights = new JSONArray();
  // for (Flight flight:this.getFlights())
  // {
  // flights.put(flight.getJSON());
  // }
  // reserv.put("flights",new JSONObject().put("flight",flights));
  // reserv.put("passenger",this.getPassenger().getJSON());
  // result.put("reservation",reserv);
  // return result;
  // }

}
