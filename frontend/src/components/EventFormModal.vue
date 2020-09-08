<template>
    <div>

        <slot name="activator" :open="open"></slot>

        <VDialog v-model="dialog" width="450">

            <VCard>
                <VCardTitle>
                    {{ !planUpdate ? 'New' : 'Update'}} event
                </VCardTitle>
                <VCardText>
                    <VForm @submit.prevent="submit" ref="form">

                        <VTextField :rules="[
        v => !!v || 'Title is required',
        v => (v && v.length > 3 && v.length < 16) || 'Username must be lower than 16 and higher than 3 characters',
      ]" filled label="Title" :loading="loading" :readonly="loading"
                                    v-model="plan.title"></VTextField>

                        <VTextarea filled label="Description" :loading="loading" :readonly="loading"
                                   v-model="plan.description"></VTextarea>

                        <v-datetime-picker :date-picker-props="{'first-day-of-week': 1}" :loading="loading" :readonly="loading" label="Event start date"
                                           v-model="plan.start">
                            <template #dateIcon>
                                <v-icon>mdi-calendar</v-icon>
                            </template>

                            <template #timeIcon>
                                <v-icon>mdi-clock</v-icon>
                            </template>
                        </v-datetime-picker>
                        <v-datetime-picker :date-picker-props="{weekdays: [1, 2, 3, 4, 5, 6, 0]}" date :loading="loading" :readonly="loading" label="Event end date"
                                           v-model="plan.end">
                            <template #dateIcon>
                                <v-icon>mdi-calendar</v-icon>
                            </template>

                            <template #timeIcon>
                                <v-icon>mdi-clock</v-icon>
                            </template>
                        </v-datetime-picker>

                        <VRadioGroup v-model="plan.color" row>
                            <VRadio
                                    label="Red"
                                    color="red"
                                    value="red"
                            />
                            <VRadio
                                    label="Blue"
                                    color="blue"
                                    value="blue"
                            />
                            <VRadio
                                    label="Green"
                                    color="green"
                                    value="green"
                            />
                            <VRadio
                                    label="Indigo"
                                    color="indigo"
                                    value="indigo"
                            ></VRadio>
                            <VRadio
                                    label="Orange"
                                    color="orange"
                                    value="orange"
                            />
                            <VRadio
                                    label="Yellow"
                                    color="yellow"
                                    value="yellow"
                            />
                            <VRadio
                                    label="Cyan"
                                    color="cyan"
                                    value="cyan"
                            />
                            <VRadio
                                    label="Purple"
                                    color="purple"
                                    value="purple"
                            />
                        </VRadioGroup>

                        <p v-if="data === null  && !(!error) && !loading" class="red--text">
                            Server error!
                        </p>

                        <VCardActions>
                            <VSpacer/>
                            <VBtn color="primary" :loading="loading" type="submit">SAVE</VBtn>
                        </VCardActions>

                    </VForm>
                </VCardText>
            </VCard>

        </VDialog>
    </div>
</template>

<script>

    import {reactive, watch, ref, computed} from "@vue/composition-api";
    import {usePlan} from "../composition/plan";

    export default {

        props: {
            planPropData: Object
        },

        setup(props, context) {

            const planUpdate = props.planPropData;

            console.log(planUpdate);

            const dialog = ref(false);

            const open = () => {
                dialog.value = true;
            };

            const plan = !(!planUpdate) ? reactive({
                    id: planUpdate.id,
                    userId: planUpdate.userId,
                    title: planUpdate.title,
                    description: planUpdate.description,
                    color: planUpdate.color,
                    start: new Date(planUpdate.start),
                    end: new Date(planUpdate.end)
                }) :
                reactive({title: '', description: null, color: 'red', start: new Date(), end: new Date()});

            const {savePlan, savePlanData, savingPlanError, savingPlan, updatePlan, updatePlanData, updatingPlanError, updatingPlan} = usePlan();

            const submit = () => {
                if (!context.refs.form.validate()) return;


                if (!planUpdate) savePlan(plan);
                else updatePlan(plan.id, plan);

            };

            const loading = ref(false);
            const error = ref(false);
            const data = ref(null);

            watch([savingPlan, updatingPlan, savingPlanError, updatingPlanError, savePlanData, updatePlanData], ([a, b, aError, bError, aData, bData]) => {
                if (!(!planUpdate)) {
                    loading.value = b;
                    error.value = bError;
                    data.value = bData;

                } else {
                    loading.value = a;
                    error.value = aError;
                    data.value = aData;
                }

                if (data.value !== null) {
                    window.location.reload();
                }

            });
            return {plan, loading, data, error, submit, dialog, open, planUpdate};
        }
    }
</script>
