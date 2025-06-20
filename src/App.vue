<template>
  <n-message-provider placement="bottom-right">
    <div id="app">
      <div class="app-content">
        <router-view />
      </div>
    </div>
  </n-message-provider>
</template>

<script>
import { NMessageProvider } from "naive-ui";

export default {
  name: "App",
  components: {
    NMessageProvider,
  },
};
</script>

<style>
/* Reset default styles */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  width: 100%;
  min-width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  overflow-x: hidden;
}

#app {
  width: 100vw;
  min-width: 100vw;
  height: 100vh;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.app-content {
  flex: 1;
  width: 100%;
  min-width: 100%;
  height: 100%;
}
</style>
