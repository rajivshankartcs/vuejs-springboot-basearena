/* global assert */
import Vue from 'vue'
import Reservation from '@/components/Reservation'
import API from '@/API'

describe('Reservation.vue', () => {
  function componentHelper (component, propsData) {
    const app = document.createElement('div')
    app.setAttribute('data-app', true)
    document.body.append(app)
    let Ctor = Vue.extend(component)
    const el = document.createElement('DIV')
    return new Ctor({propsData, el})
  }

  it('should be initialized with proper values', () => {
    const headers = [ {
      text: 'Flight Numbers',
      value: 'flightNumbers',
      align: 'left'
    },
    {text: 'Name', value: 'name', align: 'left'},
    {text: 'Age', value: 'age', align: 'left'},
    {text: 'Gender', value: 'gender', align: 'left'},
    {text: 'Actions', value: '', align: 'left'}]

    expect(Reservation.data().showAlert).to.equal(false)
    expect(Reservation.data().actionPerformed).to.equal('')
    expect(Reservation.data().addReservationDialog).to.equal(false)
    expect(Reservation.data().reservations).to.be.deep.equal([])
    expect(Reservation.data().tableHeaders).to.be.deep.equal(headers)
  })

  it('should call loadReservations method on beforeMount and load reservations', () => {
    let loadReservationsCall = sinon.spy(Reservation.methods, 'loadReservations')
    componentHelper(Reservation)
    assert(loadReservationsCall.called)
    Vue.nextTick(() => {
      expect(Reservation.data().reservations).to.bee.deep.equal([])
    })
  })

  it('should load reservations data when loadReservations method is called', () => {
    let APICall = sinon.stub(API, 'call').resolves({data: [{ test: 'test' }]})
    Reservation.methods.loadReservations()
    expect(Reservation.data().reservations.length).to.equal(0)
    APICall.restore()
  })

  it('should set correct values to variables when closeFlightDialog method is called', () => {
    Reservation.methods.closeReservationDialog(true)
    Vue.nextTick(() => {
      expect(Reservation.data().actionPerformed).to.equal('created')
      expect(Reservation.data().showAlert).to.equal(true)
      expect(Reservation.data().addReservationDialog).to.equal(false)
    })
    Reservation.methods.closeReservationDialog(false)
    Vue.nextTick(() => {
      expect(Reservation.data().addReservationDialog).to.equal(false)
    })
  })

  it('should add data to passengers when getPassengerDetails method is called', () => {
    let paramData = {
      passengerNumber: 1,
      flightNumber: 'A408',
      orderNumber: 1
    }
    let responseData = { data: {
      firstname: 'User',
      lastname: '1',
      age: 25,
      gender: 'Male'
    } }
    let APICall = sinon.stub(API, 'call').resolves(responseData)
    Reservation.methods.getPassengerDetails(paramData)
    expect(Reservation.data().reservations.length).to.equal(0)
    APICall.restore()
  })

  it('should generate data in proper format when getPassengerDetails method is called', () => {
    let rawData = {
      passengerNumber: 1,
      flightNumber: 'A408',
      orderNumber: 1
    }

    Reservation.methods.getPassengerDetails(rawData)
    expect(Reservation.data().reservations).to.be.deep.equal([])
  })

  it('should call API when delReservation method is called', () => {
    let APICall = sinon.stub(API, 'call').resolves({ test: 'test' })
    Reservation.methods.delReservation('A408')
    Vue.nextTick(() => {
      expect(Reservation.data().actionPerformed).to.be.equal('deleted')
      expect(Reservation.data().showAlert).to.be.equal(true)
    })
    APICall.restore()
  })
})
