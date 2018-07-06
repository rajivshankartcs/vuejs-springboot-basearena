<template>
  <v-form>
    <v-text-field
      v-model="flightNumber"
      :counter="lengthValidations.maxVal4"
      :disabled="actionname == 'edit'"
      :error-messages="flightNumberErr"
      label="Flight number (cannot be edited later)"
      required
    />
    <v-text-field
      v-model="manufacturer"
      :counter="lengthValidations.maxVal1"
      :error-messages="manufacturerErr"
      label="Manufacturer"
      required
    />
    <v-text-field
      v-model="model"
      :counter="lengthValidations.maxVal2"
      :error-messages="modelErr"
      label="Model"
      required
    />
    <v-text-field
      v-model="description"
      :counter="lengthValidations.maxVal3"
      :error-messages="descriptionErr"
      label="Description"
      required
    />
    <v-select
      :items="yomItems"
      v-model="yearOfManufacture"
      :error-messages="yomErr"
      single-line
      label="Year of Manufacture"
      required
    />
    <v-text-field
      v-model="fromLocation"
      :counter="lengthValidations.maxVal2"
      :error-messages="fromLocErr"
      label="From"
      required
    />
    <v-text-field
      v-model="toLocation"
      :counter="lengthValidations.maxVal2"
      :error-messages="toLocErr"
      label="To"
      required
    />
    <v-text-field
      v-model="price"
      :hook="flightins == null"
      :error-messages="priceErr"
      label="Price"
      required
    />
    <v-text-field
      v-model="departureTime"
      :error-messages="depTimeErr"
      label="Departure Time"
      required
    />
    <v-text-field
      v-model="arrivalTime"
      :error-messages="arrTimeErr"
      label="Arrival Time"
      required
    />
    <v-text-field
      v-model="capacity"
      :error-messages="capacityErr"
      label="Capacity"
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
  name: 'FlightFormDialog',
  props: {
    flightins: {
      type: Object,
      required: false,
      default: () => ({
        id: null,
        manufacturer: null,
        model: null,
        description: null,
        yearOfManufacture: null,
        vacancy: null,
        price: null,
        between: null,
        departure: null,
        arrival: null
      })
    },
    actionname: {
      type: String,
      required: true,
      default: ''
    }
  },
  data () {
    return {
       relativeURLPath: 'flight',
       lengthValidations: {
         maxVal1: 20,
         maxVal2: 25,
         maxVal3: 50,
         maxVal4: 6
       },
       getDataFirstTime: true,
       yomItems: [],
       flightNumber: null,
       manufacturer: null,
       model: null,
       description: null,
       yearOfManufacture: null,
       fromLocation: null,
       toLocation: null,
       price: null,
       departureTime: null,
       arrivalTime: null,
       capacity: null,
       flightNumberErr: [],
       manufacturerErr: [],
       modelErr: [],
       descriptionErr: [],
       yomErr: [],
       fromLocErr: [],
       toLocErr: [],
       priceErr: [],
       depTimeErr: [],
       arrTimeErr: [],
       capacityErr: [],
       apiResponse: {}
    }
  },
  beforeMount () {
    this.fillYOMItems()
  },
  updated () {
    this.loadFlightData()
  },
  methods: {
    fillYOMItems() {
      var i = 0
      for (var year = 1950; year <= 2018; year++) {
        this.yomItems[i++] = year
      }
    },
    loadFlightData () {
      if (this.actionname === 'edit' && this.getDataFirstTime) {
        var timeFormat = /^(\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}):\d{1,2}$/
        this.price = this.flightins.price
        this.fromLocation = this.flightins.between.split(' -> ')[0]
        this.toLocation = this.flightins.between.split(' -> ')[1]
        this.description = this.flightins.description
        this.departureTime = this.flightins.departure.match(timeFormat)[1]
        this.arrivalTime = this.flightins.arrival.match(timeFormat)[1]
        this.capacity = this.flightins.vacancy.split('/')[1]
        this.model = this.flightins.model
        this.manufacturer = this.flightins.manufacturer
        this.yearOfManufacture = this.flightins.yearOfManufacture
        this.flightNumber = this.flightins.id
        this.getDataFirstTime = false
      } else if (this.actionname === 'create' && !this.getDataFirstTime) {
        this.clear()
        this.getDataFirstTime = true
      }
    },
    formatDateTime (dtStr) {
      var timeFormat = /^(\d{4})-(\d{1,2})-(\d{1,2})\s(\d{1,2})$/
      var dtSplits = dtStr.match(timeFormat)
      return new Date(dtSplits[1], dtSplits[2], dtSplits[3], dtSplits[4])
    },
    submit () {
      if (!this.validateInputs()) {
        var reqParams = {
          price: this.price + '',
          from: this.fromLocation,
          to: this.toLocation,
          departureTime: this.formatDateTime(this.departureTime),
          description: this.description,
          arrivalTime: this.formatDateTime(this.arrivalTime),
          capacity: this.capacity,
          model: this.model,
          manufacturer: this.manufacturer,
          yearOfManufacture: this.yearOfManufacture
        }

        var relUrl = this.relativeURLPath + '/' + this.flightNumber
        API.call(BASE_URL, 'post', relUrl, reqParams).then((response) => {
          this.apiResponse = response
          this.$emit('close-dialog')
          this.clear()
        })
      }
    },
    stringErrMsg (fieldName, maxLength) {
      return fieldName + ' cannot be empty or exceed ' + maxLength + ' letters'
    },
    clearErrArr () {
      this.flightNumberErr = []
      this.manufacturerErr = []
      this.modelErr = []
      this.descriptionErr = []
      this.yomErr = []
      this.fromLocErr = []
      this.toLocErr = []
      this.priceErr = []
      this.depTimeErr = []
      this.arrTimeErr = []
      this.capacityErr = []
    },
    validateInputs () {
      this.clearErrArr()
      var dateFormat = /^(\d{4})-(\d{1,2})-(\d{1,2})\s(\d{1,2})$/
      if (this.flightNumber == null || this.flightNumber.length === 0 ||
        this.flightNumber.length > this.lengthValidations.maxVal4) {
        this.flightNumberErr.push(
          this.stringErrMsg('Flight number', this.lengthValidations.maxVal4)
        )
      }
      if (this.manufacturer == null || this.manufacturer.length === 0 ||
        this.manufacturer.length > this.lengthValidations.maxVal1) {
        this.manufacturerErr.push(
          this.stringErrMsg('Manufacturer', this.lengthValidations.maxVal1)
        )
      }
      if (this.model == null || this.model.length === 0 ||
        this.model.length > this.lengthValidations.maxVal2) {
        this.modelErr.push(
          this.stringErrMsg('Model', this.lengthValidations.maxVal2)
        )
      }
      if (this.description == null || this.description.length === 0 ||
        this.description.length > this.lengthValidations.maxVal3) {
        this.descriptionErr.push(
          this.stringErrMsg('Description', this.lengthValidations.maxVal3)
        )
      }
      if (this.fromLocation == null || this.fromLocation.length === 0 ||
        this.fromLocation.length > this.lengthValidations.maxVal2) {
        this.fromLocErr.push(
          this.stringErrMsg('From', this.lengthValidations.maxVal2)
        )
      }
      if (this.toLocation == null || this.toLocation.length === 0 ||
        this.toLocation.length > this.lengthValidations.maxVal2) {
        this.toLocErr.push(
          this.stringErrMsg('To', this.lengthValidations.maxVal2)
        )
      }
      if (this.yearOfManufacture == null || isNaN(this.yearOfManufacture) ||
        this.yearOfManufacture < 1950 || this.yearOfManufacture > 2018) {
        this.yomErr.push(
          'Year of Manufacture must be a valid year between 1950 and 2018'
        )
      }
      if (this.price == null || isNaN(this.price) || this.price < 0) {
        this.priceErr.push('Price must be a number equal or more than zero')
      }
      if (
        this.departureTime == null ||
        this.departureTime.match(dateFormat) == null
      ) {
        this.depTimeErr.push(
          'Departure Time must be in the format YYYY-MM-DD HH'
        )
      }
      if (
        this.arrivalTime == null || this.arrivalTime.match(dateFormat) == null
      ) {
        this.arrTimeErr.push('Arrival Time must be in the format YYYY-MM-DD HH')
      }
      if (this.capacity == null || isNaN(this.capacity) || this.capacity <= 0) {
        this.capacityErr.push('Capacity must be a number more than zero')
      }
      return (this.manufacturerErr.length > 0 && this.modelErr.length > 0 &&
            this.descriptionErr.length > 0 && this.yomErr.length > 0 &&
            this.fromLocErr.length > 0 && this.toLocErr.length > 0 &&
            this.priceErr.length > 0 && this.depTimeErr.length > 0 &&
            this.arrTimeErr.length > 0 && this.capacityErr.length > 0)
    },
    clear () {
      this.flightNumber = null
      this.manufacturer = null
      this.model = null
      this.description = null
      this.yearOfManufacture = null
      this.fromLocation = null
      this.toLocation = null
      this.price = null
      this.departureTime = null
      this.arrivalTime = null
      this.capacity = null
    }
  }
}
</script>
