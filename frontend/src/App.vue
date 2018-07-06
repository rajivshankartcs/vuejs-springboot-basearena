<template>
  <v-app>
    <v-navigation-drawer
      :mini-variant="miniVariant"
      :clipped="clipped"
      v-model="drawer"
      persistent
      enable-resize-watcher
      fixed
      app
    >
      <v-list>
        <v-list-tile
          v-for="(item, i) in items"
          :key="i"
          value="true"
        >
          <router-link
            :to="{ path: item.path }"
            tag="v-list-tile"
          >
            <v-list-tile-action>
              <v-icon v-html="item.icon"/>
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title v-text="item.title"/>
            </v-list-tile-content>
          </router-link>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
    <v-toolbar
      :clipped-left="clipped"
      color="blue"
      dark
      app
    >
      <v-toolbar-side-icon @click.stop="drawer = !drawer"/>
      <v-btn
        v-show="drawer"
        icon
        @click.stop="miniVariant = !miniVariant"
      >
        <v-icon v-html="miniVariant ? 'chevron_right' : 'chevron_left'"/>
      </v-btn>
      <v-toolbar-title>
        <router-link
          to="/"
          tag="v-toolbar-title"
        >
          {{ title }}
        </router-link>
      </v-toolbar-title>
      <v-spacer/>
    </v-toolbar>
    <v-content>
      <router-view/>
    </v-content>
    <v-footer
      :fixed="fixed"
      color="blue"
      dark
      app
    >
      <span>&copy; 2018</span>
    </v-footer>
  </v-app>
</template>

<script>
export default {
  name: 'App',
  data () {
    return {
      clipped: false,
      drawer: false,
      fixed: false,
      items: [{
        path: 'flight',
        icon: 'fas fa-plane',
        title: 'Flights'
      }, {
        path: 'passenger',
        icon: 'fas fa-user-circle',
        title: 'Passengers'
      }, {
        path: 'reservation',
        icon: 'fas fa-clipboard-check',
        title: 'Reservations'
      }],
      miniVariant: false,
      right: true,
      rightDrawer: false,
      title: 'Book your flight'
    }
  }
}
</script>
