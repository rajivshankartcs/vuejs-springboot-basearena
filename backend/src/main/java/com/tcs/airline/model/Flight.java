package com.tcs.airline.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Flight implements Serializable {


@Id
  @Column(name = "flight_number", unique = true)
  private String number; // Each flight has a unique flight number.

  private int price;

  @Column(name = "from_source")
  private String from;

  @Column(name = "to_destination")
  private String to;

  /*
   * Date format: yy-mm-dd-hh, do not include minutes and sceonds. Example: 2017-03-22-19 The system
   * only needs to supports PST. You can ignore other time zones.
   */

  // @JsonFormat(pattern="yyyy-MM-dd-HH")
  private Date departureTime;

  // @JsonFormat(pattern="yyyy-MM-dd-HH")
  private Date arrivalTime;

  private int seatsLeft;

  private String description;

  private int capacity;

  private String model;

  private String manufacturer;

  private int yearOfManufacture;

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public int getYearOfManufacture() {
    return yearOfManufacture;
  }

  public void setYearOfManufacture(int yearOfManufacture) {
    this.yearOfManufacture = yearOfManufacture;
  }

  @ManyToMany
  @JsonIgnore
  @JoinTable(name = "flight_passenger", joinColumns = {
      @JoinColumn(name = "flight_umber", referencedColumnName = "flight_number") }, inverseJoinColumns = {
          @JoinColumn(name = "passenger_id", referencedColumnName = "passenger_id") })
  private List<Passenger> passengers;

  @JsonIgnore
  @ManyToMany(mappedBy = "flights")
  private List<Reservation> reservations;

  public Flight() {

  }

  public Flight(String number) {
    this.number = number;
  }

  public Flight(String flightNumber, String from, String to, int price, int capacity,
      String description, Date aTime, Date dTime, String manufacturer, String model, int yearsofM) {
    this.arrivalTime = (Date) aTime.clone();
    this.number = flightNumber;
    this.departureTime = (Date) dTime.clone();
    this.from = from;
    this.to = to;
    this.price = price;
    this.seatsLeft = capacity;
    this.description = description;
    this.setModel(model);
    this.setCapacity(capacity);
    this.setManufacturer(manufacturer);
    this.setYearOfManufacture(yearsofM);

  }

  // ---------------setter getter----------------

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public Date getDepartureTime() {
	return departureTime;
  }

  public void setDepartureTime(Date departureTime) {
    this.departureTime = departureTime;
  }

  public Date getArrivalTime() {
	  return arrivalTime;
  }

  public void setArrivalTime(Date arrivalTime) {
    this.arrivalTime =  arrivalTime;
  }

  public int getSeatsLeft() {
    return seatsLeft;
  }

  public void setSeatsLeft(int seatsLeft) {
    this.seatsLeft = seatsLeft;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Passenger> getPassengers() {
    return passengers;
  }

  public void setPassengers(List<Passenger> passengers) {
    this.passengers = passengers;
  }

  public List<Reservation> getReservations() {
    return reservations;
  }
//
//  public void setReservations(List<Reservation> reservations) {
//    this.reservations = reservations;
//  }

  // public JSONObject getJSON() throws JSONException
  // {
  // JSONObject flightJson=new JSONObject();
  // flightJson.put("number",this.getNumber());
  // flightJson.put("price",this.getPrice());
  // flightJson.put("from",this.getFrom());
  // flightJson.put("to",this.getTo());
  // flightJson.put("departureTime",this.getDepartureTime());
  // flightJson.put("arrivalTime",this.getArrivalTime());
  // flightJson.put("seatsLeft",this.getSeatsLeft());
  // flightJson.put("description",this.getDescription());
  // flightJson.put("plane",this.getPlane().getJSON());
  // return flightJson;
  // }

  /**
   * Flight Data as JSONObject inclusive of all Passengers details
   * 
   * @return JSONObject
   * @throws JSONException
   */
  // public JSONObject getFullJson() throws JSONException
  // {
  // JSONObject resultObject=new JSONObject();
  // JSONObject flight=this.getJSON();
  // JSONObject passengers=new JSONObject();
  // JSONArray passengerArray=new JSONArray();
  // for(Passenger passenger:this.getPassengers())
  // {
  // JSONObject pass=passenger.getJSON();
  // passengerArray.put(pass);
  // }
  // passengers.put("passenger",passengerArray);
  // flight.put("passengers",passengers);
  // resultObject.put("flight",flight);
  // return resultObject;
  // }
}
