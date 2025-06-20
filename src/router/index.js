import { createRouter, createWebHistory } from "vue-router";
import Layout from "@/views/layout/Layout.vue";
import Editor from "@/views/nms/EditorView.vue";
import Login from "@/views/ums/login/index.vue";
import HomeView from "@/views/nms/HomeView.vue";
import Register from "@/views/ums/register/index.vue";
const routes = [
  {
    path: "/note",
    component: Layout,
    children: [
      {
        path: "",
        component: HomeView,
      },
      {
        path: "edit",
        component: Editor,
      },
    ],
  },
  {
    path: "/login",
    component: Login,
  },
  {
    path: "/register",
    component: Register,
  },
];
const router = createRouter({
  history: createWebHistory(),
  routes: routes,
});

export default router;
