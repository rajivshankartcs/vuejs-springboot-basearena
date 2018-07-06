<template>
  <v-container fluid>
    <v-layout row>
      <v-snackbar
        :timeout="2000"
        :top="true"
        :multi-line="true"
        v-model="showAlert"
      >
        Flight {{ actionPerformed }} successfully
        <v-btn
          flat
          color="pink"
          @click.native="snackbar = false"
        >
          Close
        </v-btn>
      </v-snackbar>
      <h1>Manage Flights</h1>
      <v-btn fab dark small color="blue" @click.stop="openFlightDialog">
        <v-icon dark>add</v-icon>
      </v-btn>
      <v-dialog
        v-model="addFlightDialog"
        persistent
        max-width="500px"
      >
        <v-card>
          <v-card-title color="blue">
            Add a flight
          </v-card-title>
          <v-card-text>
            <FlightFormDialog
              :flightins="flightIns"
              :actionname="formAction"
              @close-dialog="closeFlightDialog(true, true)"
            />
          </v-card-text>
          <v-card-actions>
            <v-btn
              color="primary"
              flat
              @click.stop="closeFlightDialog(true)"
            >
              Close
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-dialog
        v-model="editFlightDialog"
        persistent
        max-width="500px"
      >
        <v-card>
          <v-card-title color="blue">
            Edit a flight
          </v-card-title>
         <v-card-text>
            <FlightFormDialog
              :flightins="flightIns"
              :actionname="formAction"
              @close-dialog="closeFlightDialog(false, true)"
            />
          </v-card-text>
          <v-card-actions>
            <v-btn
              color="primary"
              flat
              @click.stop="closeFlightDialog(false)"
            >
              Close
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-layout>
    <v-layout column>
      <v-data-table
        :headers="tableHeaders"
        :items="flights"
        item-key="name"
        class="elevation-1"
      >
        <template
          slot="items"
          slot-scope="props"
        >
          <tr id="props.item.id">
            <td class="text-xs">{{ props.item.id }}</td>
            <td class="text-xs">{{ props.item.vacancy }}</td>
            <td class="text-xs">{{ props.item.price }}</td>
            <td class="text-xs">{{ props.item.between }}</td>
            <td class="text-xs">{{ props.item.departure.split('.')[0] }}</td>
            <td class="text-xs">{{ props.item.arrival.split('.')[0] }}</td>
            <td>
              <v-layout row>
                <v-flex>
                  <v-btn
                    small
                    flat
                    icon
                    color="blue"
                    @click.stop="editFlight(props.item.id)"
                  >
                    <v-icon>fas fa-edit</v-icon>
                  </v-btn>
                </v-flex>
                <v-flex>
                  <v-btn
                    small
                    flat
                    icon
                    color="deep-orange"
                    @click.stop="delFlight(props.item.id)"
                  >
                    <v-icon>fas fa-trash</v-icon>
                  </v-btn>
                </v-flex>
              </v-layout>
            </td>
          </tr>
        </template>
      </v-data-table>
    </v-layout>
  </v-container>
</template>

<script>
/* global BASE_URL */
import FlightFormDialog from './FlightFormDialog'
import API from '../API'

export default {
  name: 'Flight',
  components: { FlightFormDialog },
  data () {
    return {
      showAlert: false,
      actionPerformed: '',
      addFlightDialog: false,
      flights: [],
      flightIns: null,
      editFlightDialog: false,
      formAction: 'create',
      tableHeaders: [ {text: 'Flight Number', value: 'id', align: 'left'},
         {text: 'Vacancy', value: 'vacancy', align: 'left'},
         {text: 'Price', value: 'price', align: 'left'},
         {text: 'Between', value: 'between', align: 'left'},
         {text: 'Departure', value: 'departure', align: 'left'},
         {text: 'Arrival', value: 'arrival', align: 'left'},
         {text: 'Actions', value: '', align: 'left'}]
    }
  },
  beforeMount () {
    this.loadFlights()
  },
  methods: {
    openFlightDialog () {
      this.flightIns = null
      this.addFlightDialog = true
    },
    closeFlightDialog (isCreate, shouldOverride = false) {
      if (isCreate) {
        if (shouldOverride) {
          this.actionPerformed = 'added'
          this.showAlert = true
          this.addFlightDialog = false
        } else {
          this.addFlightDialog = false
        }
      } else {
        if (shouldOverride) {
          this.actionPerformed = 'edited'
          this.showAlert = true
          this.editFlightDialog = false
        } else {
          this.editFlightDialog = false
        }
        this.formAction = 'create'
        this.flightIns = null
       }
      this.loadFlights()
    },
    formatDateTime (epochTime) {
      var dateTime = new Date(epochTime)
      var dateTimeArr = dateTime.toISOString().split('T')
      var timeArr = dateTimeArr[1].split(':')
      return dateTimeArr[0] + ' ' + timeArr[0] + ':00'
    },
    cleanFlightData (rawData) {
      return {
        id: rawData.number,
        manufacturer: rawData.manufacturer,
        model: rawData.model,
        description: rawData.description,
        yearOfManufacture: rawData.yearOfManufacture,
        vacancy: rawData.seatsLeft + '/' + rawData.capacity,
        price: rawData.price,
        between: rawData.from + ' -> ' + rawData.to,
        departure: this.formatDateTime(rawData.departureTime),
        arrival: this.formatDateTime(rawData.arrivalTime)
      }
    },
    loadFlights () {
      this.flights = []
      API.call(BASE_URL, 'get', 'flight').then((response) => {
        var rawData = response.data
        this.flights = []
        for (var i = 0; i < rawData.length; i++) {
          this.flights.push(this.cleanFlightData(rawData[i]))
        }
      })
    },
    editFlight (flightId) {
      API.call(BASE_URL, 'get', 'flight/' + flightId).then((response) => {
        this.formAction = 'edit'
        this.editFlightDialog = true
        this.flightIns = this.cleanFlightData(response.data)
      })
    },
    delFlight (flightId) {
      API.call(BASE_URL, 'delete', 'flight/' + flightId).then((response) => {
        this.actionPerformed = 'deleted'
        this.showAlert = true
        this.loadFlights()
      })
    }
  }
}
</script>
