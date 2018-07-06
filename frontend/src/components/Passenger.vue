<template>
  <v-container fluid>
    <v-layout row>
      <v-snackbar
        :timeout="2000"
        :top="true"
        :multi-line="true"
        v-model="showAlert"
      >
        Passenger {{ actionPerformed }} successfully
        <v-btn
          flat
          color="pink"
          @click.native="snackbar = false"
        >
          Close
        </v-btn>
      </v-snackbar>
      <h1>Manage Passengers</h1>
      <v-btn
        fab
        dark
        small
        color="blue"
        @click.stop="openPassengerDialog"
      >
        <v-icon dark>add</v-icon>
      </v-btn>
      <v-dialog
        v-model="addPassengerDialog"
        persistent
        max-width="500px"
      >
        <v-card>
          <v-card-title color="blue">
            Add a passenger
          </v-card-title>
          <v-card-text>
            <PassengerFormDialog
              :passengerins="passengerIns"
              :actionname="formAction"
              @close-dialog="closePassengerDialog(true, true)"
            />
          </v-card-text>
          <v-card-actions>
            <v-btn
              color="primary"
              flat
              @click.stop="closePassengerDialog(true)"
            >
              Close
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-dialog
        v-model="editPassengerDialog"
        persistent
        max-width="500px"
      >
        <v-card>
          <v-card-title color="blue">
            Edit a passenger
          </v-card-title>
          <v-card-text>
            <PassengerFormDialog
              :passengerins="passengerIns"
              :actionname="formAction"
              @close-dialog="closePassengerDialog(false, true)"
            />
          </v-card-text>
          <v-card-actions>
            <v-btn
              color="primary"
              flat
              @click.stop="closePassengerDialog(false)"
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
        :items="passengers"
        item-key="name"
        class="elevation-1"
      >
        <template
          slot="items"
          slot-scope="props"
        >
          <tr>
            <td class="text-xs">{{ props.item.firstName }}</td>
            <td class="text-xs">{{ props.item.lastName }}</td>
            <td class="text-xs">{{ props.item.age }}</td>
            <td class="text-xs">{{ props.item.gender }}</td>
            <td class="text-xs">{{ props.item.phone }}</td>
            <td>
              <v-layout row>
                <v-flex>
                  <v-btn
                    small
                    flat
                    icon
                    color="blue"
                    @click.stop="editPassenger(props.item.id)"
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
                    @click.stop="delPassenger(props.item.id)"
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
import PassengerFormDialog from './PassengerFormDialog'
import API from '../API'

export default {
  name: 'Passenger',
  components: { PassengerFormDialog },
  data () {
    return {
      showAlert: false,
      actionPerformed: '',
      addPassengerDialog: false,
      passengers: [],
      passengerIns: null,
      editPassengerDialog: false,
      formAction: 'create',
      tableHeaders: [ {text: 'First name', value: 'firstName', align: 'left'},
        {text: 'Last name', value: 'lastName', align: 'left'},
        {text: 'Age', value: 'age', align: 'left'},
        {text: 'Gender', value: 'gender', align: 'left'},
        {text: 'Phone', value: 'phone', align: 'left'},
        {text: 'Actions', value: '', align: 'left'}]
    }
  },
  beforeMount () {
    this.loadPassengers()
  },
  methods: {
    openPassengerDialog () {
      this.passengerIns = null
      this.addPassengerDialog = true
    },
    closePassengerDialog (isCreate, shouldOverride = false) {
      if (isCreate) {
        if (shouldOverride) {
          this.actionPerformed = 'added'
          this.showAlert = true
          this.addPassengerDialog = false
        } else {
          this.addPassengerDialog = false
        }
      } else {
        if (shouldOverride) {
          this.actionPerformed = 'edited'
          this.showAlert = true
          this.editPassengerDialog = false
        } else {
          this.editPassengerDialog = false
        }
        this.formAction = 'create'
        this.passengerIns = null
      }
      this.loadPassengers()
    },
    cleanPassengerData (rawData) {
      return {
        id: rawData.id,
        firstName: rawData.firstname,
        lastName: rawData.lastname,
        age: rawData.age,
        gender: rawData.gender,
        phone: rawData.phone
      }
    },
    loadPassengers () {
      this.passengers = []
      API.call(BASE_URL, 'get', 'passenger').then((response) => {
        var rawData = response.data
        this.passengers = []
        for (var i = 0; i < rawData.length; i++) {
          this.passengers.push(this.cleanPassengerData(rawData[i]))
        }
      })
    },
    editPassenger (passengerId) {
      API.call(BASE_URL, 'get', 'passenger/' + passengerId).then((response) => {
        this.formAction = 'edit'
        this.editPassengerDialog = true
        this.passengerIns = this.cleanPassengerData(response.data)
      })
    },
    delPassenger (passengerId) {
      API.call(BASE_URL, 'delete', 'passenger/' + passengerId).then((response) => {
        this.actionPerformed = 'deleted'
        this.showAlert = true
        this.loadPassengers()
      })
    }
  }
}
</script>
