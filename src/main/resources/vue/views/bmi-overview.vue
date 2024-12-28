<template id="bmi-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            BMI Summary
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
           v-for="(bmi,index) in bmis" v-bind:key="index">
        <div class="mr-auto p-2">
          <span>User {{ bmi.userId }} have BMI Value {{ bmi.bmivalue }} on {{ bmi.started}}</span>
        </div>

      </div>
    </div>
  </app-layout>
</template>
<script>
app.component("bmi-overview", {
  template: "#bmi-overview",
  data: () => ({
    users: [],
    formData: [],
    hideForm :true,
    bmis: []
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/bmi")
          .then(res => this.bmis = res.data)
          .catch(() => console.log("Error while fetching bmies"));
    },
  }
});
</script>