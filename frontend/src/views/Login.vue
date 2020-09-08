<template>
  <div class="login">
    <VContainer>
      <VCard class="mx-auto"
             max-width="450">

        <!-- Title -->
        <VCardTitle>
          Login
        </VCardTitle>
        <VCardText>

          <!-- Login form -->
          <VForm @submit.prevent="submit" ref="form">

            <!-- Username -->
            <VTextField :rules="loginRules" filled label="Username" :loading="isLogging" :readonly="isLogging"
                        v-model="username"></VTextField>

            <!-- Password -->
            <VTextField :rules="passwordRules" type="password" filled label="Password" v-model="password"
                        :loading="isLogging"
                        :readonly="isLogging"></VTextField>

            <!-- Error message -->
            <p v-if="loggingFinished && error" class="red--text">
              {{ error }}
            </p>

            <VCardActions>
              <!-- Submit button -->
              <VBtn color="success" :loading="isLogging" type="submit">Login</VBtn>
            </VCardActions>

          </VForm>
        </VCardText>
      </VCard>
    </VContainer>
  </div>
</template>

<script>

import {computed, reactive, ref, toRefs, watch} from '@vue/composition-api';
import {useAuthentication} from "../composition/authentication";

export default {
  setup(props, context) {

    const username = ref(null);
    const password = ref(null);

    const loginRules = ref([
      v => !!v || 'Username is required',
      v => (v && v.length > 3 && v.length < 19) || 'Username must be lower than 19 and higher than 3 characters',
    ]);

    const passwordRules = ref([
      v => !!v || 'Password is required',
      v => (v && v.length > 5) || 'Password must be higher than 6 characters',
    ]);

    const {login, isLogging, loginSuccess, loggingFinished, loginError} = useAuthentication();

    const submit = () => {
      if (!context.refs.form.validate()) return;
      login(username.value, password.value);
    };

    watch(loginSuccess, (newCount) => {
      if (newCount) window.location.replace("/");
    });


    const error = computed(() => {
      if (!loginError.value) return null;
      if (loginError.value.message) return loginError.value.message;
      else return loginError.value;
    });


    return {submit, username, password, isLogging, loggingFinished, error, loginRules, passwordRules};

  }
};
</script>