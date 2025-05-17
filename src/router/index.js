import { createRouter, createWebHistory } from "vue-router";
import Layout from "@/views/layout/Layout.vue";
import Editor from "@/views/nms/EditorView.vue";
import Login from "@/views/ums/login/index.vue";

const routes = [
  {
    path: "/note",
    component: Layout,
    children: [
      {
        path: "",
        component: Editor,
      },
    ],
  },
  {
    path: "/login",
    component: Login,
  },
];
const router = createRouter({
  history: createWebHistory(),
  routes: routes,
});

export default router;
