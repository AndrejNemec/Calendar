<template>
  <div class="home">
    <v-container v-if="!fetchingAllPlans">
      <v-container class="grey lighten-3">
        <h4 class="headline text-center">Your events</h4>
        <v-row>

          <!-- Set today in calendar - button -->
          <v-col class="text-left">
            <v-btn class="ma-2" color="primary" outlined @click="today">
              Today
            </v-btn>
          </v-col>

          <!-- Select calendar type -->
          <v-col class="text-center">
            <v-select class="ma-2" outlined dense label="Type" v-model="calendarType"
                      :items="calendarTypes"/>
          </v-col>

          <!-- Add new plan to calendar (Dialog/Button)-->
          <v-col>
            <div class="text-right">
              <!-- Dialog -->
              <EventFormModal>
                <template #activator="{open}">
                  <!-- Button -->
                  <v-btn color="primary" outlined @click="open()" right class="ma-2">
                    <v-icon>mdi-plus</v-icon>
                  </v-btn>
                </template>
              </EventFormModal>
            </div>
          </v-col>
        </v-row>

        <v-divider/>

        <v-row>
          <!-- Calendar previous button -->
          <v-col cols="4" class="text-center">
            <v-btn
                icon color="primary"
                @click.native="calendar.prev()">
              <v-icon>mdi-chevron-left</v-icon>
            </v-btn>
          </v-col>

          <!-- Calendar title (Month and year)-->
          <v-col cols="4" class="text-center">
            <v-chip color="primary" dark>{{ title }}</v-chip>
          </v-col>

          <!-- Calendar next button -->
          <v-col cols="4" class="text-center">
            <v-btn
                icon color="primary"
                @click.native="calendar.next()">
              <v-icon>mdi-chevron-right</v-icon>
            </v-btn>
          </v-col>
        </v-row>

      </v-container>

      <v-sheet height="600">

        <!-- Vuetify calendar -->
        <v-calendar :weekdays="[1, 2, 3, 4, 5, 6, 0]" v-model="focus" :type="calendarType"
                    ref="calendar" event-overlap-mode="stack"
                    event-overlap-threshold="30"
                    :events="plans" :event-color="getEventColor" @click:event="showEvent" @click:more="viewDay"
                    @click:date="viewDay"></v-calendar>

        <!-- Event info menu -->
        <v-menu
            v-model="selectedEventOpen"
            :close-on-content-click="false"
            :activator="selectedEventElement"
            offset-x
        >
          <v-card
              color="grey lighten-4"
              min-width="350px"
              flat
          >
            <v-toolbar
                :color="selectedEventObject.color"
                dark
            >

              <!-- Event edit dialog -->
              <EventFormModal v-if="selectedEventOpen === true"
                              :plan-prop-data="selectedEventObject.original">
                <template #activator="{open}">
                  <!-- Button (pencil icon)-->
                  <v-btn @click="open()" icon>
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                </template>
              </EventFormModal>

              <!-- Event name -->
              <v-toolbar-title v-html="selectedEventObject.name"></v-toolbar-title>

              <v-spacer></v-spacer>

              <!-- Event delete button -->
              <v-btn icon @click="deletePlanFromEvents">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </v-toolbar>

            <!-- Event details (description)-->
            <v-card-text>
              <span v-html="selectedEventObject.details"></span>
            </v-card-text>

            <v-card-actions>
              <!-- Close menu -->
              <v-btn
                  text
                  color="secondary"
                  @click="selectedEventOpen = false"
              >
                Cancel
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-menu>
      </v-sheet>
    </v-container>
  </div>
</template>

<script>

import {usePlan} from "../composition/plan";
import {computed, onMounted, reactive, ref} from "@vue/composition-api";
import EventFormModal from "../components/EventFormModal";

export default {

  components: {
    EventFormModal
  },

  setup(props, context) {

    const calendar = ref(null);
    const calendarTypes = ref(['month', 'week', 'day', '4day']);
    const calendarType = ref('month');

    const selectedEventObject = ref({});
    const selectedEventOpen = ref(null);
    const selectedEventElement = ref(null);


    const focus = ref('');

    const formatDate = (a, withTime) => {
      return withTime
          ? `${a.getFullYear()}-${a.getMonth() + 1}-${a.getDate()} ${a.getHours()}:${a.getMinutes()}`
          : `${a.getFullYear()}-${a.getMonth() + 1}-${a.getDate()}`
    };

    const today = () => {
      focus.value = formatDate(new Date(), false);
    };

    const title = computed(() => {

      let date = new Date(focus.value);

      return date.toUTCString().split(" ")[2] + " " + date.toUTCString().split(" ")[3];

    });

    const {fetchAllPlans, fetchingAllPlans, allPlansFetchingError, allPlansData, deletePlan, deletingPlanSuccess} = usePlan();

    onMounted(() => {
      today();
      fetchAllPlans();
    });

    const plans = computed(() => allPlansData.value.map(item => {
      return {
        original: item,
        name: item.title,
        details: item.description,
        start: formatDate(new Date(item.start), true),
        end: formatDate(new Date(item.end), true),
        color: item.color
      }
    }));

    const getEventColor = (event) => {
      return event.color;
    };

    const showEvent = ({nativeEvent, event}) => {
      const open = () => {
        selectedEventObject.value = event
        selectedEventElement.value = nativeEvent.target
        setTimeout(() => selectedEventOpen.value = true, 10)
      }

      if (selectedEventOpen.value) {
        selectedEventOpen.value = false
        setTimeout(open, 10)
      } else {
        open()
      }

      nativeEvent.stopPropagation()
    };

    const viewDay = ({date}) => {
      focus.value = date;
      calendarType.value = 'day'
    };

    const deletePlanFromEvents = () => {
      if (!selectedEventOpen) return;
      let id = selectedEventObject.value.original.id;

      if (!confirm('Are you wish to remove this plan?')) return;

      deletePlan(id);

      if (deletingPlanSuccess) window.location.reload();

    };

    return {
      plans,
      fetchingAllPlans,
      getEventColor,
      calendar,
      focus,
      title,
      today,
      calendarTypes,
      calendarType,
      selectedEventObject,
      selectedEventElement,
      selectedEventOpen,
      showEvent,
      viewDay,
      deletePlanFromEvents
    };

  }
};
</script>
