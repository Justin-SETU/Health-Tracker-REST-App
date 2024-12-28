<template id="user-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user.</p>
      <p> View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateUser()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteUser()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="user.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="user.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="user.email" name="email" placeholder="Email"/>
          </div>
      </div>

      <div class="card-footer text-left">
        <p  v-if="activities.length === 0"> No activities yet...</p>
        <p  v-if="activities.length > 0"> Activities so far...</p>
        <ul>
          <li v-for="activity in activities">
            {{ activity.description }} for {{ activity.duration }} minutes
          </li>
        </ul>
      </div>

      <div class="card-footer text-left">
        <p  v-if="bmis.length === 0"> No bmis yet...</p>
        <p  v-if="bmis.length > 0"> BMI summary so far...</p>
        <ul>
          <li v-for="bmi in bmis">
            User have bmi value {{bmi.bmivalue}}
          </li>
        </ul>
      </div>

      <div class="card-footer text-left">
        <p  v-if="steps.length === 0"> No Step counts yet...</p>
        <p  v-if="steps.length > 0"> Step Counts so far...</p>
        <ul>
          <li v-for="step in steps">
            User have Step Count {{step.stepcount}}
          </li>
        </ul>
      </div>

      <div class="card-footer text-left">
        <p  v-if="meals.length === 0"> No Meals yet...</p>
        <p  v-if="meals.length > 0"> Meals so far...</p>
        <ul>
          <li v-for="meal in meals">
            User have a {{meal.food}}
          </li>
        </ul>
      </div>

      <div class="card-footer text-left">
        <p  v-if="sleeps.length === 0"> No Sleep counts yet...</p>
        <p  v-if="sleeps.length > 0"> Sleep counts so far...</p>
        <ul>
          <li v-for="sleep in sleeps">
            User have a sleeps of {{sleep.duration}} hours
          </li>
        </ul>
      </div>

      <div class="card-footer text-left">
        <p  v-if="waters.length === 0"> No water counts yet...</p>
        <p  v-if="waters.length > 0"> Water counts so far...</p>
        <ul>
          <li v-for="water in waters">
            User have {{water.waterintake}} glasses of water
          </li>
        </ul>
      </div>


    </div>
  </app-layout>
</template>

<script>
app.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
    noUserFound: false,
    activities: [],
    bmis:[],
    steps: [],
    meals:[],
    sleeps: [],
    waters:[],
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`
    axios.get(url)
        .then(res => this.user = res.data)
        .catch(error => {
          console.log("No user found for id passed in the path parameter: " + error)
          this.noUserFound = true
        })
    axios.get(url + `/activities`)
        .then(res => this.activities = res.data)
        .catch(error => {
          console.log("No activities added yet (this is ok): " + error)
        })
    axios.get(url + `/bmis`)
        .then(res => this.bmis = res.data)
        .catch(error => {
          console.log("No BMI value added yet (this is ok): " + error)
        })
    axios.get(url + `/steps`)
        .then(res => this.steps = res.data)
        .catch(error => {
          console.log("No step counts  added yet (this is ok): " + error)
        })
    axios.get(url + `/meal`)
        .then(res => this.meals = res.data)
        .catch(error => {
          console.log("No meals added yet (this is ok): " + error)
        })
    axios.get(url + `/sleep`)
        .then(res => this.sleeps = res.data)
        .catch(error => {
          console.log("No sleeps added yet (this is ok): " + error)
        })
    axios.get(url + `/water`)
        .then(res => this.waters = res.data)
        .catch(error => {
          console.log("No water logs added yet (this is ok): " + error)
        })


  },
  methods: {

    updateUser: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/update/${userId}`;  // Correct endpoint for updating the user
      axios.patch(url, {
        name: this.user.name,
        email: this.user.email,
        password: this.user.password
      })
          .then(response => {
            console.log(response.data); // Log response data here
            this.user = response.data; // Update user object after successful update
          })
          .catch(error => {
            console.log(error); // Log the error if the request fails
          });
      alert("User updated!");
    },

    deleteUser: function () {
      if (confirm("Do you really want to delete?")) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/delete/${userId}`; // Correctly replace {user-id} with the dynamic userId
        axios.delete(url)
            .then(response => {
              alert("User deleted");
              // Redirect to the /users endpoint
              window.location.href = '/users';
            })
            .catch(function (error) {
              console.log("Error while deleting user:", error);
            });
      }
    },

  }

});
</script>