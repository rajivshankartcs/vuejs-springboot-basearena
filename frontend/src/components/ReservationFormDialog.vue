<template>
  <v-form>
    <v-select
      :items="flightIds"
      v-model="flightIdChosen"
      :error-messages="flightErr"
      single-line
      label="Flight"
      required
    />
    <v-select
      :items="passengerIds"
      v-model="passengerId"
      :error-messages="passengerErr"
      item-text="text"
      item-value="id"
      return-object
      single-line
      label="Passenger"
      required
    />
    <v-btn @click="submit">submit</v-btn>
    <v-btn @click="clear">clear</v-btn>
  </v-form>
</template>

<script>
/* global BASE_URL */
import API from '../API'

export default {
  name: 'PassengerFormDialog',
  data () {
    return {
      relativeURLPath: 'reservation',
      flightIds: [],
      passengerIds: [],
      getDataFirstTime: true,
      flightIdChosen: null,
      passengerId: null,
      flightErr: [],
      passengerErr: [],
      apiResponse: {}
    }
  },
  beforeMount () {
    this.loadFlightNPassengerData()
  },
  methods: {
    loadFlightNPassengerData () {
      var flights = []
      var passengers = []
      API.call(BASE_URL, 'get', 'flight').then((response) => {
        flights = response.data
        for (var i = 0; i < flights.length; i++) {
          this.flightIds.push(flights[i].number)
        }
      })
      API.call(BASE_URL, 'get', 'passenger').then((response) => {
        passengers = response.data
        for (var i = 0; i < passengers.length; i++) {
          this.passengerIds.push(this.sanitizePassengerData(passengers[i]))
        }
      })
    },
    sanitizePassengerData (rawPassengerData) {
      return {
        id: rawPassengerData.id + '',
        text: rawPassengerData.firstname + ' ' + rawPassengerData.lastname + ', ' +
              rawPassengerData.age + ', ' + rawPassengerData.gender
      }
    },
    submit () {
      if (!this.validateInputs()) {
        var relURL = this.relativeURLPath + '/' + this.passengerId.id + '/' + this.flightIdChosen

        API.call(BASE_URL, 'post', relURL).then((response) => {
          this.apiResponse = response.data
          this.clear()
          this.$emit('close-dialog')
        })
      }
    },
    clearErrArr () {
      this.flightErr = []
      this.passengerErr = []
    },
    validateInputs () {
      this.clearErrArr()
      if (this.flightIdChosen == null) {
        this.flightErr.push('Flight must be a valid entry')
      }
      if (this.passengerId == null) {
        this.passengerErr.push('Passenger must be a valid entry')
      }
      return (this.flightErr.length > 0 && this.passengerErr.length > 0)
    },
    clear () {
      this.flightIdChosen = null
      this.passengerId = null
    }
  }
}
</script>
