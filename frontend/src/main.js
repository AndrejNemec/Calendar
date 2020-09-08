import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify';

import VueCompositionApi from '@vue/composition-api';
import DatetimePicker from 'vuetify-datetime-picker'

Vue.use(DatetimePicker)
Vue.use(VueCompositionApi);

Vue.config.productionTip = false;

//Javascript global function (Date => toISODate)
if (!Date.prototype.toISODate) {
  //@ts-ignore
  Date.prototype.toISODate = function () {
    return this.getFullYear() + '-' +
        ('0' + (this.getMonth() + 1)).slice(-2) + '-' +
        ('0' + this.getDate()).slice(-2);
  }
}

new Vue({
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app');
