import "./assets/main.css";

import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import pinia from "./store";
import naive from "naive-ui";
const app = createApp(App);

app.use(naive);
app.use(pinia);
app.use(router);

app.mount("#app");
