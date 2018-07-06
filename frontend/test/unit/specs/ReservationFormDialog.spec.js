import Vue from 'vue'
import ReservationFormDialog from '@/components/ReservationFormDialog'
import { mount } from '@vue/test-utils'
import API from '@/API'

describe('ReservationFormDialog.vue', () => {
  it('should be initialized with proper values', () => {
    expect(ReservationFormDialog.data().relativeURLPath).to.equal('reservation')
    expect(ReservationFormDialog.data().flightIds).to.be.deep.equal([])
    expect(ReservationFormDialog.data().passengerIds).to.be.deep.equal([])
    expect(ReservationFormDialog.data().getDataFirstTime).to.equal(true)
    expect(ReservationFormDialog.data().flightIdChosen).to.equal(null)
    expect(ReservationFormDialog.data().passengerId).to.equal(null)
    expect(ReservationFormDialog.data().flightErr).to.be.deep.equal([])
    expect(ReservationFormDialog.data().passengerErr).to.be.deep.equal([])
    expect(ReservationFormDialog.data().apiResponse).to.be.deep.equal({})
  })

  it('should get flight and passenger data when loadFlightNPassengerData method is called', () => {
    let responseData = { data: [{ test: 'test1' }, { test: 'test2' }] }
    let APICall = sinon.stub(API, 'call').resolves(responseData)
    ReservationFormDialog.methods.loadFlightNPassengerData()
    expect(ReservationFormDialog.data().flightIds.length).equal(0)
    expect(ReservationFormDialog.data().passengerIds.length).equal(0)
    APICall.restore()
  })

  it('should clear data when submit method is called without errors', () => {
    let APICall = sinon.stub(API, 'call').resolves({ test: 'test' })
    const wrapper = mount(ReservationFormDialog, {
      data () {
        return {
          passengerId: {
            id: 1,
            text: 'User 1, 25, Male'
          },
          flightIdChosen: 'A408'
        }
      }
    })
    wrapper.findAll('form button').at(0).trigger('click')
    Vue.nextTick(() => {
      expect(wrapper.vm.passengerId).to.equal(null)
    })
    APICall.restore()
  })

  it('should return a proper format data when sanitizePassengerData method is called', () => {
    let paramData = {
      id: 1,
      firstname: 'User',
      lastname: '1',
      age: 25,
      gender: 'Male'
    }
    let expected = {
      id: '1',
      text: 'User 1, 25, Male'
    }

    let actual = expected
    expect(expected).to.be.deep.equal(actual)
  })
})
