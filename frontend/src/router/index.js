import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Flight from '@/components/Flight'
import Passenger from '@/components/Passenger'
import Reservation from '@/components/Reservation'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/flight',
      name: 'flight',
      component: Flight
    },
    {
      path: '/passenger',
      name: 'passenger',
      component: Passenger
    },
    {
      path: '/reservation',
      name: 'reservation',
      component: Reservation
    }
  ]
})
