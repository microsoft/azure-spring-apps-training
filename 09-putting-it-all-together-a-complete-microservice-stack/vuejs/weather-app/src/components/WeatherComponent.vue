<template>
  <v-container
    id="input-usage"
    fluid
  >
    <v-row>
        <v-col cols="3">
             <v-icon size="100">mdi-cloud</v-icon>
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
      It is cloudy {{ apiurl }}
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    data: function () {
        return {
            apiurl: "https://azure-spring-cloud-training-gateway.azureapps.io"
        }
    },
    props: {

    },
    methods: {
      getWeather() {
          this.apiurl = "yuolo"
      }
    },
  }
</script>

<style>

</style>