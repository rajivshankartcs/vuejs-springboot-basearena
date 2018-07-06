import API from '@/API'
import axios from 'axios'

describe('API.js', () => {
  let axiosReqCall

  beforeEach(() => {
    axiosReqCall = sinon.stub(axios, 'request').returns('success')
  })

  afterEach(() => {
    axios.request.restore()
  })

  it('should call the api with http://', () => {
    API.call('localhost')
    expect(axiosReqCall.calledOnce).to.equal(true)
  })

  it('should call the api if base url is set', () => {
    API.call('http://localhost')
    expect(axiosReqCall.calledOnce).to.equal(true)
  })
})
