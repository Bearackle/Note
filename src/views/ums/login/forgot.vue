<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">MemoHub.</div>
      <div class="login-subtitle">Restore your account</div>
      <n-form ref="formRef" :model="formData" :rules="rules">
        <n-input
          v-model:value="formData.email"
          placeholder="Enter your email..."
          class="login-input"
          type="email"
        />
        <n-button
          v-if="!showOtp"
          block
          type="primary"
          class="login-continue"
          :loading="loading"
          @click="handleEmailSubmit"
          >Confirm</n-button
        >

        <template v-if="showOtp">
          <div class="otp-container">
            <n-input-group>
              <n-input
                v-for="(digit, index) in 6"
                :key="index"
                v-model:value="formData.otp[index]"
                :maxlength="1"
                class="otp-input"
                @input="handleOtpInput($event, index)"
                @keydown.delete="handleOtpDelete($event, index)"
                @keydown.left="focusPrevInput(index)"
                @keydown.right="focusNextInput(index)"
              />
            </n-input-group>
            <div class="otp-hint">Enter OTP code sent to your email</div>
          </div>
          <n-button
            block
            type="primary"
            class="login-continue"
            :loading="loading"
            @click="handleOtpSubmit"
            >Continue</n-button
          >
        </template>

        <div class="login-footer">
          <n-button text @click="$router.push('/login')">
            Back to login
          </n-button>
        </div>
      </n-form>
    </div>
  </div>
</template>

<script>
import api from "@/api/axios";
import { useMessage } from "naive-ui";

export default {
  name: "ForgotPassword",
  setup() {
    const message = useMessage();
    return {
      message,
    };
  },
  data() {
    return {
      loading: false,
      showOtp: false,
      formData: {
        email: "",
        otp: Array(6).fill(""),
      },
      rules: {
        email: [
          {
            required: true,
            message: "Please enter your email",
            trigger: "blur",
          },
          {
            type: "email",
            message: "Invalid email format",
            trigger: ["blur", "input"],
          },
        ],
      },
    };
  },
  methods: {
    async handleEmailSubmit() {
      try {
        this.loading = true;
        await this.$refs.formRef.validate();

        const response = await api.post("/user/send-otp", {
          email: this.formData.email,
        });

        if (response.data.code === 200) {
          this.message.success("OTP code has been sent to your email");
          this.showOtp = true;
        } else {
          this.message.error("Failed to send OTP code");
        }
      } catch (error) {
        if (error?.message) {
          this.message.error(error.message);
        } else {
          this.message.error("An error occurred, please try again");
        }
      } finally {
        this.loading = false;
      }
    },
    handleOtpInput(event, index) {
      const value = event.target.value;
      if (value && index < 5) {
        this.$nextTick(() => {
          const nextInput = document.querySelector(
            `.otp-input:nth-child(${index + 2}) input`
          );
          if (nextInput) nextInput.focus();
        });
      }
    },
    handleOtpDelete(event, index) {
      if (!this.formData.otp[index] && index > 0) {
        this.$nextTick(() => {
          const prevInput = document.querySelector(
            `.otp-input:nth-child(${index}) input`
          );
          if (prevInput) prevInput.focus();
        });
      }
    },
    focusPrevInput(index) {
      if (index > 0) {
        this.$nextTick(() => {
          const prevInput = document.querySelector(
            `.otp-input:nth-child(${index}) input`
          );
          if (prevInput) prevInput.focus();
        });
      }
    },
    focusNextInput(index) {
      if (index < 5) {
        this.$nextTick(() => {
          const nextInput = document.querySelector(
            `.otp-input:nth-child(${index + 2}) input`
          );
          if (nextInput) nextInput.focus();
        });
      }
    },
    async handleOtpSubmit() {
      try {
        this.loading = true;
        const otp = this.formData.otp.join("");
        if (!otp || otp.length !== 6) {
          this.message.error("Please enter the complete OTP code");
          return;
        }

        const response = await api.post("/user/verify-otp", {
          email: this.formData.email,
          otp: otp,
        });

        if (response.data.code === 200) {
          this.message.success("Verification successful");
          this.$router.push("/reset-password");
        } else {
          this.message.error("Invalid OTP code");
        }
      } catch (error) {
        this.message.error("An error occurred, please try again");
      } finally {
        this.loading = false;
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

.otp-container {
  width: 100%;
  margin-bottom: 16px;
}

.otp-input {
  width: calc(100% / 6 - 8px);
  margin: 0 4px;
  text-align: center;
}

.otp-input :deep(input) {
  text-align: center;
  font-size: 1.2rem;
  padding: 8px 0;
  border-radius: 8px;
  background-color: #f5f5f5;
}

.otp-hint {
  font-size: 0.85rem;
  color: #888;
  text-align: center;
  margin-top: 8px;
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
</style>
