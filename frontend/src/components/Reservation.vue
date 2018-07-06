<template>
  <v-container fluid>
    <v-layout row>
      <v-snackbar
        :timeout="2000"
        :top="true"
        :multi-line="true"
        v-model="showAlert"
      >
        Reservation {{ actionPerformed }} successfully
        <v-btn
          flat
          color="pink"
          @click.native="snackbar = false"
        >
          Close
        </v-btn>
      </v-snackbar>
      <h1>Manage Reservations</h1>
      <v-btn
        fab
        dark
        small
        color="blue"
        @click.stop="openReservationDialog"
      >
        <v-icon dark>add</v-icon>
      </v-btn>
      <v-dialog
        v-model="addReservationDialog"
        persistent
        max-width="500px"
      >
        <v-card>
          <v-card-title color="blue">
            Add a reservation
          </v-card-title>
          <v-card-text>
            <ReservationFormDialog
              @close-dialog="closeReservationDialog(true)"
            ></ReservationFormDialog>
          </v-card-text>
          <v-card-actions>
            <v-btn
              color="primary"
              flat
              @click.stop="closeReservationDialog(false)"
            >
              Close
            </v-btn>
          </v-card-actions>
        </v-card>
    </v-layout>
    <v-layout column>
      <v-data-table
        :headers="tableHeaders"
        :items="reservations"
        item-key="name"
        class="elevation-1"
      >
        <template
          slot="items"
          slot-scope="props"
        >
          <tr>
            <td class="text-xs">{{ props.item.flightNumber }}</td>
            <td class="text-xs">{{ props.item.name }}</td>
            <td class="text-xs">{{ props.item.age }}</td>
            <td class="text-xs">{{ props.item.gender }}</td>
            <td>
              <v-layout row>
                <v-flex>
                  <v-btn
                    small
                    flat
                    icon
                    color="deep-orange"
                    @click.stop="delReservation(props.item.id)"
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
import ReservationFormDialog from './ReservationFormDialog'
import API from '../API'

export default {
  name: 'Reservation',
  components: { ReservationFormDialog },
  data () {
    return {
      showAlert: false,
      actionPerformed: '',
      addReservationDialog: false,
      reservations: [],
      tableHeaders: [ {
        text: 'Flight Numbers',
        value: 'flightNumbers',
        align: 'left'
      },
      {text: 'Name', value: 'name', align: 'left'},
      {text: 'Age', value: 'age', align: 'left'},
      {text: 'Gender', value: 'gender', align: 'left'},
      {text: 'Actions', value: '', align: 'left'}]
    }
  },
  beforeMount () {
    this.loadReservations()
  },
  methods: {
    openReservationDialog () {
      this.reservationIns = null
      this.addReservationDialog = true
    },
    closeReservationDialog (shouldOverride) {
      if (shouldOverride) {
        this.actionPerformed = 'created'
        this.showAlert = true
        this.addReservationDialog = false
      } else {
        this.addReservationDialog = false
      }
      this.loadReservations()
    },
    getPassengerDetails (rawReservation) {
      API.call(
        BASE_URL,
        'get',
        'passenger/' + rawReservation.passengerNumber
      ).then((passengerResponse) => {
        var reservationData = {
          flightNumber: rawReservation.flightNumber,
          name: passengerResponse.data.firstname +
            ' ' +
            passengerResponse.data.lastname,
          age: passengerResponse.data.age,
          gender: passengerResponse.data.gender,
          id: rawReservation.orderNumber
        }
        this.reservations.push(reservationData)
      })
    },
    loadReservations () {
      this.reservations = []
      API.call(BASE_URL, 'get', 'reservation').then((response) => {
        var rawReservations = response.data
        for (var i = 0;i < rawReservations.length;i++) {
          this.getPassengerDetails(rawReservations[i])
        }
      })
    },
    delReservation (reservationId) {
      API.call(BASE_URL, 'delete', 'reservation/' + reservationId).then((response) => {
        this.actionPerformed = 'deleted'
        this.showAlert = true
        this.loadReservations()
      })
    }
  }
}
</script>
