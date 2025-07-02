<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">MemoHub.</div>
      <div class="login-subtitle">Change your password</div>
      <n-input
        v-model:value="newPassword"
        placeholder="New password..."
        class="login-input"
        type="password"
      />
      <n-button
        block
        type="primary"
        class="login-continue"
        @click="handleChangePassword"
        >Confirm</n-button
      >
    </div>
  </div>
</template>
<script>
import api from "@/api/axios";
import { useMessage } from "naive-ui";

export default {
  name: "ChangePassword",
  setup() {
    const message = useMessage();
    return {
      message,
    };
  },
  data() {
    return {
      newPassword: "",
    };
  },
  methods: {
    async handleChangePassword() {
      try {
        const response = await api.post("/user/change-password", {
          newPassword: this.newPassword,
        });
        if (response.data.code === 200) {
          this.message.success("Đổi mật khẩu thành công");
          this.$router.push("/login");
        } else {
          this.message.error("Đổi mật khẩu thất bại");
        }
      } catch (error) {
        this.message.error("Đổi mật khẩu thất bại");
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
.login-input {
  width: 100%;
  margin-bottom: 16px;
}
.login-continue {
  width: 100%;
  margin-bottom: 16px;
}
</style>
