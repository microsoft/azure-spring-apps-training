<template>
  <v-container
    id="input-usage"
    fluid
  >
    <v-row>
        <v-col cols="3">
             <v-icon size="100">mdi-weather-partly-cloudy</v-icon>
        </v-col>
        <v-col cols="6">
            <v-form ref="form">
                <v-text-field label="Spring Cloud Gateway URL" v-model="apiurl"/>
                <v-btn class="mr-4" @click="getWeather">
                    Go
                </v-btn>
            </v-form>
        </v-col>
        
    </v-row>
    <hr/>
    <v-row>
      <v-col cols="1">
      </v-col>
      <v-col cols="10">
      <v-simple-table>
       
        <template v-slot:default>
          <thead>
            <tr>
              <th class="text-left">City</th>
              <th class="text-left">Weather</th>
              <th class="text-left"></th>
            </tr>
          </thead>
          <transition name="fade">
          <tbody v-if="show">
            <tr v-for="city in cities" :key="city.name">
              <td>{{ city.name }}</td>
              <td>{{ city.description }}</td>
              <td><v-icon size="40">mdi-{{ city.icon }}</v-icon></td>
            </tr>
          </tbody>
          </transition>
        </template>
      </v-simple-table>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  import Vue from "vue";
  import axios from "axios";
  import VueAxios from "vue-axios";

  Vue.use(VueAxios, axios);

  export default {
    data: function () {
        return {
            apiurl: "https://azure-spring-cloud-training-gateway.azureapps.io",
            cities: {},
            show: true
        }
    },
    props: {
    },
    methods: {
      getWeather() {
          this.show = false;
          Vue.axios.get(this.apiurl + '/CITY-SERVICE/cities').catch(error => {
            throw new Error(`REST endpoint error when fetching the cities: ${error}`);
          }).then(response => {
            this.cities = response.data[0];
            let promises = [];
            this.cities.forEach((city) => {
              promises.push(Vue.axios.get(this.apiurl + '/WEATHER-SERVICE/weather/city?name=' + encodeURI(city.name)));
            });
            let cities = {};
            Vue.axios.all(promises).then(function(results) {
                results.forEach(function(response, index) {
                    cities[index] = new Object();
                    cities[index].name = response.data.city;
                    cities[index].description = response.data.description;
                    cities[index].icon = response.data.icon;
                })
            return cities;
            }).then((cities) => {
              this.cities = cities;
              this.show = true;
            });
          });
      }
    },
  }
</script>

<style>
.fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}
</style>