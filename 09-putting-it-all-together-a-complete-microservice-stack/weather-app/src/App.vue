<template>
  <v-app id="inspire">
    <v-app-bar
      app
      color="indigo"
      dark
    >
      <v-icon>mdi-weather-partly-cloudy</v-icon>&nbsp;
      <v-toolbar-title>What is the weather today?</v-toolbar-title>
    </v-app-bar>

    <v-content>
      <v-container
        fluid
      >
        <v-row
          align="center"
          justify="center"
        >
          <v-col class="text-center">
            <WeatherComponent/>
          </v-col>
        </v-row>
      </v-container>
    </v-content>
    <v-footer
      color="indigo"
      app
    >
      <span class="white--text"><a  class="white--text" href="https://github.com/microsoft/azure-spring-cloud-training">Azure Spring Cloud training</a></span>
    </v-footer>
  </v-app>
</template>

<script>
  import WeatherComponent from './components/WeatherComponent'

  export default {
    components: {
      WeatherComponent
    },
    props: {
      source: String
    },
    data: () => ({
    }),
  }
</script>