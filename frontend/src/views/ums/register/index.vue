<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">MemoHub.</div>
      <div class="login-subtitle">Đăng ký tài khoản của bạn</div>
      <n-space vertical :size="12" class="login-buttons">
        <n-button block secondary icon="logos:google"
          >Tiếp tục với google</n-button
        >
      </n-space>
      <n-divider>or</n-divider>
      <n-input
        v-model:value="username"
        placeholder="Tên người dùng..."
        clearable
        class="login-input"
      />
      <n-input
        v-model:value="email"
        placeholder="Nhập email của bạn..."
        class="login-input"
        type="email"
      />
      <n-input
        v-model:value="password"
        placeholder="Mật khẩu..."
        class="login-input"
        type="password"
        showPasswordOn="click"
      />
      <n-input
        v-model:value="confirmPassword"
        placeholder="Nhập lại mật khẩu..."
        class="login-input"
        type="password"
        showPasswordOn="click"
      />
      <n-button
        block
        type="primary"
        class="login-continue"
        @click="handleRegister"
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
import api from "../../../api/axios";
import { useMessage } from "naive-ui";

export default {
  name: "Register",
  setup() {
    const message = useMessage();
    return {
      message,
    };
  },
  data() {
    return {
      email: "",
      username: "",
      password: "",
      confirmPassword: "",
    };
  },
  methods: {
    async handleRegister() {
      if (
        !this.username ||
        !this.email ||
        !this.password ||
        !this.confirmPassword
      ) {
        this.message.error("Vui lòng điền đầy đủ thông tin");
        return;
      }

      if (this.password !== this.confirmPassword) {
        this.message.error("Mật khẩu không khớp");
        return;
      }

      try {
        const response = await api.post("/user/register", {
          username: this.username,
          email: this.email,
          password: this.password,
        });
        console.log(response);
        if (response.data.code === 200) {
          this.message.success("Đăng ký thành công");
          this.$router.push("/login");
        } else {
          this.message.error("Đăng ký thất bại");
        }
      } catch (error) {
        this.message.error(error.response?.data?.message || "Đăng ký thất bại");
      }
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
