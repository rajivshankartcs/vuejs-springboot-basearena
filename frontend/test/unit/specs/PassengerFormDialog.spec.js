import Vue from 'vue'
import PassengerFormDialog from '@/components/PassengerFormDialog'
import { mount } from '@vue/test-utils'
import API from '@/API'

describe('PassengerFormDialog.vue', () => {
  it('should be initialized with proper values', () => {
    const passengerins = {
      id: null,
      firstName: null,
      lastName: null,
      age: null,
      gender: null,
      phone: null
    }

    expect(PassengerFormDialog.data().relativeURLPath).to.equal('passenger')
    expect(PassengerFormDialog.data().nameMaxLength).to.equal(20)
    expect(PassengerFormDialog.data().getDataFirstTime).to.equal(true)
    expect(PassengerFormDialog.data().genders).to.be.deep.equal(['Male', 'Female', 'Other'])
    expect(PassengerFormDialog.data().passengerId).to.equal(null)
    expect(PassengerFormDialog.data().firstName).to.equal(null)
    expect(PassengerFormDialog.data().lastName).to.equal(null)
    expect(PassengerFormDialog.data().age).to.equal(null)
    expect(PassengerFormDialog.data().gender).to.equal(null)
    expect(PassengerFormDialog.data().phone).to.equal(null)
    expect(PassengerFormDialog.data().firstNameErr).to.be.deep.equal([])
    expect(PassengerFormDialog.data().lastNameErr).to.be.deep.equal([])
    expect(PassengerFormDialog.data().ageErr).to.be.deep.equal([])
    expect(PassengerFormDialog.data().genderErr).to.be.deep.equal([])
    expect(PassengerFormDialog.data().phoneErr).to.be.deep.equal([])
    expect(PassengerFormDialog.data().apiResponse).to.be.deep.equal({})
    expect(PassengerFormDialog.props.passengerins.default()).to.be.deep.equal(passengerins)
  })

  it('should call loadPassengerData method when a variable is updated', () => {
    var wrapper = mount(PassengerFormDialog, {
      propsData: {
        actionname: 'create',
        passengerins: {
          firstName: 'User',
          lastName: '1',
          age: 25,
          gender: 'Female',
          phone: 9876543210
        }
      },
      data () {
        return {
          getDataFirstTime: false,
          gender: ''
        }
      }
    })
    wrapper.setData({ gender: 'Male' })
    Vue.nextTick(() => {
      expect(wrapper.vn.getDataFirstTime).to.equal(true)
    })
    wrapper = mount(PassengerFormDialog, {
      propsData: {
        actionname: 'edit',
        passengerins: {
          firstName: 'User',
          lastName: '1',
          age: 25,
          gender: 'Female',
          phone: 9876543210
        }
      },
      data () {
        return {
          getDataFirstTime: true,
          model: ''
        }
      }
    })
    wrapper.setData({ gender: 'Male' })
    Vue.nextTick(() => {
      expect(wrapper.vm.getDataFirstTime).to.equal(false)
    })
  })

  it('should call clearErrArr method when submit method is called to create without errors', () => {
    let APICall = sinon.stub(API, 'call').resolves({ test: 'test' })
    const wrapper = mount(PassengerFormDialog, {
      propsData: {
        actionname: 'create'
      },
      data () {
        return {
          firstName: 'User',
          lastName: '1',
          age: 25,
          gender: 'Female',
          phone: 9876543210
        }
      }
    })
    wrapper.findAll('form button').at(0).trigger('click')
    expect(wrapper.vm.age).to.equal(25)
    APICall.restore()
  })

  it('should call clearErrArr method when submit method is called to edit without errors', () => {
    let APICall = sinon.stub(API, 'call').resolves({ test: 'test' })
    let validateInputsCall = sinon.stub(PassengerFormDialog.methods, 'validateInputs').returns(false)
    const wrapper = mount(PassengerFormDialog, {
      propsData: {
        actionname: 'edit'
      },
      data () {
        return {
          passengerId: 1,
          firstName: 'User',
          lastName: '1',
          age: 25,
          gender: 'Female',
          phone: 9876543210
        }
      }
    })
    wrapper.findAll('form button').at(0).trigger('click')
    expect(wrapper.vm.age).to.equal(25)
    validateInputsCall.restore()
    APICall.restore()
  })

  it('should call clearErrArr method when submit method is called to create with errors', () => {
    let APICall = sinon.stub(API, 'call').resolves({ test: 'test' })
    const wrapper = mount(PassengerFormDialog, {
      propsData: {
        actionname: 'create'
      },
      data () {
        return {
          firstName: null,
          lastName: null,
          age: null,
          gender: null,
          phone: null
        }
      }
    })
    wrapper.findAll('form button').at(0).trigger('click')
    expect(wrapper.vm.ageErr.length).to.equal(1)
    APICall.restore()
  })

  it('should call clearErrArr method when submit method is called to edit with errors', () => {
    let APICall = sinon.stub(API, 'call').resolves({ test: 'test' })
    const wrapper = mount(PassengerFormDialog, {
      propsData: {
        actionname: 'edit'
      },
      data () {
        return {
          passengerId: 1,
          firstName: null,
          lastName: null,
          age: null,
          gender: null,
          phone: null
        }
      }
    })
    wrapper.findAll('form button').at(0).trigger('click')
    expect(wrapper.vm.ageErr.length).to.equal(1)
    APICall.restore()
  })
})
