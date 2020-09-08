<template>
  <v-app>
    <!-- Navigation -->
    <v-navigation-drawer
        v-model="drawer"
        app
        clipped
        dark
        color="primary"
    >

      <!-- Navigation list -->
      <v-list dense color="primary">

        <!-- Home (logged) -->
        <v-list-item link to="/" v-if="isLogged">
          <v-list-item-action>
            <v-icon>mdi-view-dashboard</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Home</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <!-- Login (non-logged) -->
        <v-list-item link to="/login" v-if="!isLogged">
          <v-list-item-action>
            <v-icon>mdi-login</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Login</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <!-- Register (non-logged) -->
        <v-list-item link to="/register" v-if="!isLogged">
          <v-list-item-action>
            <v-icon>mdi-account-plus</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Register</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

    </v-navigation-drawer>

    <!-- App bar (top)-->
    <v-app-bar color="primary"
               dark
               app
               clipped-left
    >
      <!-- Toggle menu button -->
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"/>
      <!-- App title -->
      <v-toolbar-title>Calendar</v-toolbar-title>

      <v-spacer/>

      <!-- Logout button (logged) -->
      <v-btn @click="logout" v-if="isLogged">Logout</v-btn>

    </v-app-bar>

    <!-- Content -->
    <v-content>
      <router-view/>
    </v-content>
  </v-app>
</template>

<script>
import {ref} from '@vue/composition-api';
import {useAuthentication} from "./composition/authentication";
import {useAuthorizationRequest} from "./composition/authorization";

export default {
  setup() {
    const drawer = ref(false);

    const {logout, isLogged, register} = useAuthentication();
    const {request} = useAuthorizationRequest();

    //Check is user logged (in backend)
    request().get('/api/user').catch(() => {
      if (isLogged) {
        //Logout user and reload page
        logout(false);
        setTimeout(() => window.location.reload());
      }
    });

    return {drawer, logout, isLogged};
  }
};
</script>
