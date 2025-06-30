<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">Life is short.</div>
      <div class="login-subtitle">Đăng nhập tài khoản của bạn</div>
      <n-space vertical :size="12" class="login-buttons">
        <n-button block secondary>
          <template #icon>
            <n-icon><Icon icon="devicon:google" /></n-icon>
          </template>
          Tiếp tục với google
        </n-button>
        <n-button block secondary @click="handleCreateAccount">
          <template #icon>
            <n-icon><Icon icon="twemoji:crossed-swords" /></n-icon>
          </template>
          Tạo tài khoản của bạn
        </n-button>
      </n-space>
      <n-divider>or</n-divider>
      <n-input
        v-model:value="username"
        placeholder="Tên người dùng..."
        class="login-input"
      />
      <n-input
        v-model:value="password"
        placeholder="Mật khẩu..."
        class="login-input"
        type="password"
      />
      <n-button block type="primary" class="login-continue" @click="handleLogin"
        >Continue</n-button
      >
      <div class="login-footer">
        Nếu tiếp tục, bạn cần đã chấp nhận và đồng ý với các
        <a href="#">Điều khoản và thỏa thuận</a> and
        <a href="#">Privacy Policy</a>
      </div>
    </div>
  </div>
</template>
<script>
import api from "@/api/axios";
import { useMessage } from "naive-ui";
import { useUserStore } from "@/store/user";
import Cookies from "js-cookie";
import { Icon } from "@iconify/vue";
export default {
  name: "Login",
  components: {
    Icon,
  },
  setup() {
    const message = useMessage();
    const userStore = useUserStore();
    return {
      message,
      userStore,
      Cookies,
    };
  },
  data() {
    return {
      email: "",
      username: "",
      password: "",
    };
  },
  methods: {
    async handleLogin() {
      const response = await api.post("/user/login", {
        username: this.username,
        password: this.password,
      });
      if (response.data.code === 200) {
        localStorage.setItem("token", response.data.data.token);
        this.$router.push("/note");
        this.userStore.setUser(this.username);
        const userInfo = await api.get("/user/info");
        this.Cookies.set("userId", userInfo.data.data.id, { expires: 7 });
        this.Cookies.set("username", this.username, { expires: 7 });
        this.Cookies.set("email", userInfo.data.data.email, { expires: 7 });
      } else {
        this.message.error("Đăng nhập thất bại");
      }
    },
    handleCreateAccount() {
      this.$router.push("/register");
    },
  },
};
</script>
<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fcfbf7;
}
.login-box {
  width: 350px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.06);
  padding: 40px 32px 24px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.login-title {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 8px;
  text-align: center;
}
.login-subtitle {
  font-size: 1.2rem;
  color: #888;
  margin-bottom: 24px;
  text-align: center;
}
.login-buttons {
  width: 100%;
  margin-bottom: 16px;
}
.login-input {
  width: 100%;
  margin-bottom: 16px;
}
.login-continue {
  width: 100%;
  margin-bottom: 16px;
}
.login-footer {
  font-size: 0.85rem;
  color: #888;
  text-align: center;
  margin-top: 12px;
}
.login-footer a {
  color: #3370ff;
  text-decoration: underline;
}
</style>
