import { createRouter, createWebHistory } from "vue-router";
import Layout from "@/views/layout/Layout.vue";
import Editor from "@/views/nms/EditorView.vue";
import Login from "@/views/ums/login/index.vue";
import HomeView from "@/views/nms/HomeView.vue";
import Register from "@/views/ums/register/index.vue";
import ShareViewRedirect from "@/views/nms/ShareViewRedirect.vue";
import Forgot from "@/views/ums/login/forgot.vue";
import ChangePassword from "@/views/ums/login/changepassword.vue";

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
    path: "/forgot",
    component: Forgot,
  },
  {
    path: "/register",
    component: Register,
  },
  {
    path: "/change-password",
    component: ChangePassword,
  },
  {
    path: "/share/:pageId",
    component: ShareViewRedirect,
  },
];
const router = createRouter({
  history: createWebHistory(),
  routes: routes,
});

export default router;
