import {useAuthorizationRequest} from "./authorization";
import {ref} from "@vue/composition-api";

const API_ALL_PLANS_ENDPOINT = '/api/plan/all';
const API_PLAN_BY_ID_ENDPOINT = '/api/plan/';
const API_PLAN_BY_MONTH_AND_YEAR_ENDPOINT = '/all/by-month-and-year/';

const API_SAVE_PLAN_ENDPOINT = '/api/plan/';
const API_UPDATE_PLAN_ENDPOINT = '/api/plan/';
const API_DELETE_PLAN_ENDPOINT = '/api/plan/';


export function usePlan() {

    const {request} = useAuthorizationRequest();

    //FETCH ALL PLANS

    let fetchingAllPlans = ref(false);
    let allPlansData = ref([]);
    let allPlansFetchingError = ref(null);

    function fetchAllPlans() {
        //RESET
        fetchingAllPlans.value = false;
        allPlansData.value = [];
        allPlansFetchingError.value = null;

        fetchingAllPlans.value = true;

        request().get(API_ALL_PLANS_ENDPOINT)
            .then(response => {
                    if (response.status === 200) allPlansData.value = response.data;
                }
            )
            .catch(error => {
                if (error.response.data) {
                    allPlansFetchingError.value = true;
                    return;
                }
                allPlansFetchingError.value = error.response.data;
            })
            .finally(() => {
                fetchingAllPlans.value = false;
            });

        return true;
    }

    //#####################################################################
    //#####################################################################
    //#####################################################################

    //FETCH PLAN BY ID

    let fetchingPlanById = ref(false);
    let planByIdData = ref(null);
    let planByIdFetchingError = ref(null);

    function fetchPlanById(id) {
        //RESET
        fetchingPlanById.value = false;
        planByIdData.value = null;
        planByIdFetchingError.value = null;

        fetchingPlanById.value = true;

        request().get(API_PLAN_BY_ID_ENDPOINT)
            .then(response => {
                    if (response.status === 200) planByIdData.value = response.data;
                }
            )
            .catch(error => {
                if (error.response.data) {
                    planByIdFetchingError.value = true;
                    return;
                }
                planByIdFetchingError.value = error.response.data;
            })
            .finally(() => {
                fetchingPlanById.value = false;
            });

        return true;
    }

    //#####################################################################
    //#####################################################################
    //#####################################################################

    //FETCH PLAN BY MONTH AND YEAR

    let fetchingPlanByMonthAndYear = ref(false);
    let planByMonthAndYearData = ref(null);
    let planByMonthAndYearFetchingError = ref(null);

    function fetchPlanByMonthAndYear(month, year) {
        //RESET
        fetchingPlanByMonthAndYear.value = false;
        planByMonthAndYearData.value = null;
        planByMonthAndYearFetchingError.value = null;

        fetchingPlanByMonthAndYear.value = true;

        request().get(API_PLAN_BY_MONTH_AND_YEAR_ENDPOINT)
            .then(response => {
                    if (response.status === 200) planByMonthAndYearData.value = response.data;
                }
            )
            .catch(error => {
                if (error.response.data) {
                    planByMonthAndYearFetchingError.value = true;
                    return;
                }
                planByMonthAndYearFetchingError.value = error.response.data;
            })
            .finally(() => {
                fetchingPlanByMonthAndYear.value = false;
            });

        return true;
    }

    //#####################################################################
    //#####################################################################
    //#####################################################################

    //SAVE NEW PLAN

    let savingPlan = ref(false);
    let savePlanData = ref(null);
    let savingPlanError = ref(null);

    function savePlan(plan) {
        //RESET
        savingPlan.value = false;
        savePlanData.value = null;
        savingPlanError.value = null;

        savingPlan.value = true;
        if (!(!plan.start)) plan.start.setHours(plan.start.getHours() + 1);
        if (!(!plan.end)) plan.end.setHours(plan.end.getHours() + 1);


        request().post(API_SAVE_PLAN_ENDPOINT, plan)
            .then(response => {
                    if (response.status === 200) savePlanData.value = response.data;
                }
            )
            .catch(error => {
                if (error.response.data) {
                    savingPlanError.value = true;
                    return;
                }
                savingPlanError.value = error.response.data;
            })
            .finally(() => {
                savingPlan.value = false;
            });

        return true;
    }

    //#####################################################################
    //#####################################################################
    //#####################################################################

    //UPDATE PLAN

    let updatingPlan = ref(false);
    let updatePlanData = ref(null);
    let updatingPlanError = ref(null);

    function updatePlan(id, plan) {
        //RESET
        updatingPlan.value = false;
        updatePlanData.value = null;
        updatingPlanError.value = null;

        updatingPlan.value = true;

        request().put(API_UPDATE_PLAN_ENDPOINT + id, plan)
            .then(response => {
                    if (response.status === 200) updatePlanData.value = response.data;
                }
            )
            .catch(error => {
                if (error.response.data) {
                    updatingPlanError.value = true;
                    return;
                }
                updatingPlanError.value = error.response.data;
            })
            .finally(() => {
                updatingPlan.value = false;
            });

        return true;
    }

    //#####################################################################
    //#####################################################################
    //#####################################################################

    //DELETE PLAN

    let deletingPlan = ref(false);
    let deletingPlanError = ref(null);
    let deletingPlanSuccess = ref(false);

    function deletePlan(id) {
        //RESET
        deletingPlan.value = false;
        deletingPlanSuccess.value = false;
        deletingPlanError.value = null;

        deletingPlan.value = true;

        request().delete(API_DELETE_PLAN_ENDPOINT + id)
            .then(response => {
                    if (response.status === 200) deletingPlanSuccess = true;
                    else deletingPlanSuccess = false;
                }
            )
            .catch(error => {
                deletingPlanSuccess = false;
                if (error.response.data) {
                    deletingPlanError.value = true;
                    return;
                }
                deletingPlanError.value = error.response.data;
            })
            .finally(() => {
                deletingPlan.value = false;
            });

        return true;
    }

    //#####################################################################
    //#####################################################################
    //#####################################################################


    return {
        fetchAllPlans, fetchingAllPlans, allPlansData, allPlansFetchingError,
        fetchPlanById, fetchingPlanById, planByIdData, planByIdFetchingError,
        fetchPlanByMonthAndYear, fetchingPlanByMonthAndYear, planByMonthAndYearData, planByMonthAndYearFetchingError,

        savePlan, savingPlan, savePlanData, savingPlanError,
        updatePlan, updatingPlan, updatePlanData, updatingPlanError,
        deletePlan, deletingPlan, deletingPlanSuccess, deletingPlanError

    };

}