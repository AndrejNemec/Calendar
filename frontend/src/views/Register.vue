<template>
  <div class="register">
    <VContainer>
      <VCard class="mx-auto"
             max-width="450">

        <!-- Title -->
        <VCardTitle>
          Register
        </VCardTitle>


        <VCardText>
          <VForm @submit.prevent="submit" ref="form">

            <!-- Username -->
            <VTextField filled label="Username" :rules="usernameRules" :loading="isRegistering"
                        :readonly="isRegistering"
                        v-model="username"></VTextField>

            <!-- First name -->
            <VTextField filled label="First name" :loading="isRegistering" :readonly="isRegistering"
                        v-model="firstName"></VTextField>

            <!-- Last name -->
            <VTextField filled label="Last name" :loading="isRegistering" :readonly="isRegistering"
                        v-model="lastName"></VTextField>

            <!-- Password -->
            <VTextField :rules="passwordRules" type="password" filled label="Password" v-model="password"
                        :loading="isRegistering"
                        :readonly="isRegistering"></VTextField>

            <!-- Confirm password -->
            <VTextField :rules="confirmPasswordRules" type="password" filled label="Confirm password"
                        v-model="confirmPassword"
                        :loading="isRegistering"
                        :readonly="isRegistering"></VTextField>

            <!-- Error message -->
            <p v-if="registeringFinished && error" class="red--text">
              {{ error }}
            </p>

            <VCardActions>

              <!-- Submit button -->
              <VBtn color="success" :loading="isRegistering" type="submit">Register</VBtn>
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
    const firstName = ref(null);
    const lastName = ref(null);
    const password = ref(null);
    const confirmPassword = ref(null);

    const usernameRules = ref([
      v => !!v || 'Username is required',
      v => (v && v.length > 3 && v.length < 19) || 'Username must be lower than 19 and higher than 3 characters',
    ]);

    const passwordRules = ref([
      v => !!v || 'Password is required',
      v => (v && v.length > 5) || 'Password must be higher than 6 characters',
    ]);

    const confirmPasswordRules = ref([
      v => !!v || 'Confirm password is required',
      v => (v && v.length > 5) || 'Confirm password must be higher than 6 characters',
    ]);


    const {register, registerSuccess, registeringFinished, isRegistering, registerError} = useAuthentication();

    const submit = () => {
      if (!context.refs.form.validate()) return;
      register(username.value, password.value, confirmPassword.value, firstName.value, lastName.value);
    };

    watch(registerSuccess, (newRegister) => {
      if (newRegister) window.location.replace("/login");
    });

    const error = computed(() => {
      if (!registerError.value) return null;
      if (registerError.value.message) return registerError.value.message;
      else return registerError.value;
    });

    return {
      submit,
      username,
      password,
      confirmPassword,
      firstName,
      lastName,
      isRegistering,
      registeringFinished,
      error,
      usernameRules,
      passwordRules,
      confirmPasswordRules
    };

  }
};
</script>