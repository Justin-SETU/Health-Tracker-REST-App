<template id="user-bmi-overview">
  <div>
    <h3>BMI Summary </h3>
    <ul>
      <li v-for="bmi in bmis">
        {User {bmi.id}} have bmi value {{bmi.description}}
      </li>
    </ul>
  </div>
</template>

<script>
app.component("user-bmi-overview",{
  template: "#user-bmi-overview",
  data: () => ({
    bmis: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/bmi/{user-id}`)
        .then(res => this.bmis = res.data)
        .catch(() => alert("Error while fetching activities"));
  }
});
</script>