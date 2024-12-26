<template id="activity-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Activities
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
           v-for="(activity,index) in activities" v-bind:key="index">
        <div class="mr-auto p-2">
          <span>User {{ activity.userId }} done {{ activity.description }} for {{ activity.duration }} minutes with {{ activity.calories}} calories burned on {{ activity.started}}</span>
        </div>

      </div>
    </div>
  </app-layout>
</template>
<script>
app.component("activity-overview", {
  template: "#activity-overview",
  data: () => ({
    users: [],
    formData: [],
    hideForm :true,
    activities: []
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/activities")
          .then(res => this.activities = res.data)
          .catch(() => console.log("Error while fetching activities"));
    },
  }
});
</script>