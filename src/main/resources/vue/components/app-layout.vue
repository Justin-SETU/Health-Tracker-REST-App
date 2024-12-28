<template id="app-layout">
  <div class="app-layout">
    <div class="container">
      <!-- Start of navbar -->
      <nav class="navbar navbar-expand-lg navbar-light">
        <a class="navbar-brand" href="/">Health Tracker</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto">
            <li class="nav-item active">
              <a class="nav-link" href="/users">Users</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/activities">Activities</a>
            </li>
          </ul>
        </div>
      </nav>
      <!--End of nav bar-->
      <!--Start of main content area-->
      <div class="content mt-3">
        <div class="container-fluid">
          <slot></slot>
        </div>
      </div>
      <!--End of main content area-->
      <!-- Start of footer -->
      <footer class="footer mt-5">
        <div class="container text-center py-3">
          <p>&copy; 2024 Health Tracker. All rights reserved.</p>
          <p>Made by Justin, MSc ISP</p>
        </div>
      </footer>
      <!-- End of footer -->
    </div>
  </div>
</template>


<script>
app.component("app-layout", {template: "#app-layout"});
</script>

<style>
.navbar {
  background-color: #fff9c4;
}

.content {
  background-color: #fff8e1;
}

.footer {
  background-color: #fff7d6;
}

</style>