<template id="steps-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Steps Summary
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>

    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(step,index) in steps" v-bind:key="index">
        <div class="mr-auto p-2">
          <span>User {{ step.userId }} covered {{ step.distance }} meters in  {{ step.stepcount}} steps</span>
        </div>

      </div>
    </div>
  </app-layout>
</template>
<script>
app.component("steps-overview", {
  template: "#steps-overview",
  data: () => ({
    users: [],
    formData: [],
    hideForm :true,
    steps: []
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/steps")
          .then(res => this.steps = res.data)
          .catch(() => console.log("Error while fetching stepcount"));
    },
  }
});
</script>