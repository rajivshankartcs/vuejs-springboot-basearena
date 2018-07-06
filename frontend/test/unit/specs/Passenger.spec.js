/* global assert */
import Vue from 'vue'
import Passenger from '@/components/Passenger'
import API from '@/API'

describe('Passenger.vue', () => {
  function componentHelper (component, propsData) {
    const app = document.createElement('div')
    app.setAttribute('data-app', true)
    document.body.append(app)
    let Ctor = Vue.extend(component)
    const el = document.createElement('DIV')
    return new Ctor({propsData, el})
  }

  it('should be initialized with proper values', () => {
    let headers = [ {text: 'First name', value: 'firstName', align: 'left'},
                    {text: 'Last name', value: 'lastName', align: 'left'},
                    {text: 'Age', value: 'age', align: 'left'},
                    {text: 'Gender', value: 'gender', align: 'left'},
                    {text: 'Phone', value: 'phone', align: 'left'},
                    {text: 'Actions', value: '', align: 'left'}]
    expect(Passenger.data().showAlert).to.equal(false)
    expect(Passenger.data().actionPerformed).to.equal('')
    expect(Passenger.data().addPassengerDialog).to.equal(false)
    expect(Passenger.data().passengers.length).to.equal(0)
    expect(Passenger.data().passengerIns).to.equal(null)
    expect(Passenger.data().editPassengerDialog).to.equal(false)
    expect(Passenger.data().formAction).to.equal('create')
    expect(Passenger.data().tableHeaders).to.be.deep.equal(headers)
  })

  it('should return cleaned data when cleanPassengerData method is called', () => {
    let paramData = {
      id: 1,
      firstname: 'User',
      lastname: '1',
      age: 25,
      gender: 'Male',
      phone: 9876543210
    }
    let expected = {
      id: 1,
      firstName: 'User',
      lastName: '1',
      age: 25,
      gender: 'Male',
      phone: 9876543210
    }

    let actual = Passenger.methods.cleanPassengerData(paramData)
    expect(expected).to.be.deep.equal(actual)
  })

  it('should call API when editPassenger method is called', () => {
    let APICall = sinon.stub(API, 'call').resolves({ test: 'test' })
    Passenger.methods.editPassenger(1)
    Vue.nextTick(() => {
      expect(Passenger.data().flightIns != null).to.be.equal(false)
    })
    APICall.restore()
  })

  it('should call API when delPassenger method is called', () => {
    let APICall = sinon.stub(API, 'call').resolves({ test: 'test' })
    Passenger.methods.delPassenger(1)
    Vue.nextTick(() => {
      expect(Passenger.data().actionPerformed).to.be.equal('deleted')
      expect(Passenger.data().showAlert).to.be.equal(true)
    })
    APICall.restore()
  })
})
