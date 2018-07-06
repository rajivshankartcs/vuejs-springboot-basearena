<template>
  <v-container fluid>
    <v-slide-y-transition mode="out-in">
      <v-layout
        column
        align-center
      >
        <img
          src="@/assets/flight-358319.jpeg"
          alt="Book your flight"
          class="mb-5"
        >
        <v-layout row>
          <router-link
            to="/flight"
            tag="v-btn"
          >
            <v-icon
              left
              dark
            >
              fas fa-plane
            </v-icon>
            Manage Flight
          </router-link>
          <router-link
            to="/passenger"
            tag="v-btn"
          >
            <v-icon
              left
              dark
            >
              fas fa-user-circle
            </v-icon>
            Manage Passenger
          </router-link>
          <router-link
            to="/reservation"
            tag="v-btn"
          >
            <v-icon
              left
              dark
            >
              fas fa-clipboard-check
            </v-icon>
            Manage Reservations
          </router-link>
        </v-layout>
      </v-layout>
    </v-slide-y-transition>
  </v-container>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
