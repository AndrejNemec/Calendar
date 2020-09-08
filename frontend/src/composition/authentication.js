import axios from 'axios';
import {ref, toRefs, watch} from "@vue/composition-api";
import {useAuthorizationRequest} from "./authorization";

const API_LOGIN_ENDPOINT = "/login";
const API_REGISTER_ENDPOINT = "/api/user/register";
const API_USER_PROFILE_ENDPOINT = "/api/user";

export function useAuthentication() {

    let isLogging = ref(false);
    let loggingFinished = ref(false);
    let loginError = ref(null);
    let loginSuccess = ref(false);

    let isRegistering = ref(false);
    let registeringFinished = ref(false);
    let registerError = ref(null);
    let registerSuccess = ref(false);

    function login(username, password) {
        if (!username || !password) return false;

        //RESET TO DEFAULT
        isLogging.value = false;
        loggingFinished.value = false;
        loginError.value = null;
        loginSuccess.value = false;

        isLogging.value = true;

        axios.post(API_LOGIN_ENDPOINT, {username: username, password: password}, {
            'Access-Control-Expose-Headers': 'Authorization'
        })
            .then(response => {
                if (response.status === 200) {
                    loginSuccess.value = true;
                    localStorage.setItem('jwt-token', response.headers.authorization);
                } else loginSuccess.value = false;
            })
            .catch(error => {
                if (error.response.data) {
                    loginError.value = error.response.data;
                    return;
                }
                loginError.value = 'Username or password is wrong';
            })
            .finally(
                () => {
                    isLogging.value = false;
                    loggingFinished.value = true;
                }
            );
        return true;
    }

    function register(username, password, passwordRepeat, firstName = null, lastName = null) {
        if (!username || !password) return false;

        //RESET TO DEFAILT
        isRegistering.value = false;
        registeringFinished.value = false;
        registerError.value = null;
        registerSuccess.value = false;

        isRegistering.value = true;

        axios.post(API_REGISTER_ENDPOINT, {
            username: username,
            password: password,
            passwordRepeat: passwordRepeat,
            firstName: firstName,
            lastName: lastName
        })
            .then(response => {
                if (response.status === 200) registerSuccess.value = true;
                else registerSuccess.value = false;
            })
            .catch(error => {
                if (error.response.data) {
                    registerError.value = error.response.data;
                    return;
                }
                registerError.value = 'Error with registering user';
            })
            .finally(
                () => {
                    isRegistering.value = false;
                    registeringFinished.value = true;
                }
            );
        return true;
    }

    function logout(reload = true) {
        localStorage.removeItem('jwt-token');
        if (reload) window.location.replace("/login");
        return true;
    }

    const {request} = useAuthorizationRequest();

    let fetchingUserProfile = ref(false);
    let userProfileData = ref(null);
    let fetchingUserProfileError = ref(false);

    function fetchUserProfile() {
        //RESET
        fetchingUserProfile.value = false;
        userProfileData.value = null;
        fetchingUserProfileError.value = false;

        fetchingUserProfile.value = true;

        request().get(API_USER_PROFILE_ENDPOINT)
            .then(response => {
                    if (response.status === 200) userProfileData.value = response.data;
                }
            )
            .catch(error => {
                if (error.response.data) {
                    fetchingUserProfileError.value = true;
                    return;
                }
                fetchingUserProfileError.value = error.response.data;
            })
            .finally(() => {
                fetchingUserProfile.value = false;
            });

        return true;
    }

    const isLogged = localStorage.getItem('jwt-token') !== null;

    watch(() => {
        if (!localStorage.getItem('jwt-token')) isLogged.value = false;
        else isLogged.value = true;
    });

    return {
        login,
        isLogging,
        loggingFinished,
        loginError,
        loginSuccess,
        register,
        isRegistering,
        registeringFinished,
        registerError,
        registerSuccess,
        logout,
        fetchUserProfile,
        fetchingUserProfile,
        userProfileData,
        fetchingUserProfileError,
        isLogged
    };

}