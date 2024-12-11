<template id="user-bmi-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Add BMI
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
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form>
          <div class="form-row">
            <div class="form-group col-md-4" style="display: none">
              <input type="hidden" class="form-control" v-model="userId" name="id" readonly placeholder="Id"/>
            </div>
            <div class="form-group col-md-4">
              <label for="inputweight">Weight ( kg )</label>
              <input type="text" class="form-control"  v-model="formData.weight" name="weight" placeholder="Weight ( kg )"/>
            </div>
            <div class="form-group col-md-4">
              <label for="inputheight">Height ( cm )</label>
              <input type="text" class="form-control"  v-model="formData.height" name="height" placeholder="Height ( cm )"/>
            </div>
            <div class="form-group col-md-4">
              <button @click="findbmiCalculator()" class="btn btn-primary">
                <i class="fas fa-calculator"></i>
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
    <div class="list-group list-group-flush">

      <table class="table">
        <thead>
        <tr>
          <th scope="col">Weight</th>
          <th scope="col">Height</th>
          <th scope="col">BMI</th>
          <th scope="col">Date</th>

        </tr>
        </thead>
        <tbody>
        <tr v-for="(bmi,index) in bmi" v-bind:key="index">
          <td>{{ bmi.weight }}</td>
          <td>{{ bmi.height }}</td>
          <td>{{ bmi.bmiCalculator }}</td>
          <td>{{ bmi.timestamp }}</td>
          <td>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link" @click="deleteBmi(bmi, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </app-layout>
</template>

<script>
app.component("user-bmi-overview", {
  template: "#user-bmi-overview",
  data: () => ({
    bmi: [],
    formData: [],
    userId: null,
    hideForm: true,
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/bmi`)
        .then(res => this.bmi = res.data)
        .catch(() => alert("Error while fetching bmi"));
  },
  methods: {
    deleteBmi: function (bmi, index) {
      if (confirm('Are you sure you want to delete this BMI? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const id = bmi.id;
        const url = `/api/bmi/${id}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.bmi.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    findbmiCalculator: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const bmiCalculator =0.0
      const url = `/api/bmi`;
      axios.post(url,
          {
            weight: this.formData.weight,
            height: this.formData.height,
            bmiCalculator:bmiCalculator,
            timestamp:new Date().toISOString(),
            userId: userId,

          })
          .then(response => {
            this.bmi.push(response.data)
            this.hideForm = true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>