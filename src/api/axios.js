import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/", // Đổi thành URL API của bạn
  headers: {
    "Content-Type": "application/json",
  },
});
api.interceptors.request.use(
  (config) => {
    // Nếu cần gắn token
    //const token = localStorage.getItem("token");
    const token =
      "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuZ3V5ZW5kaW5oaHVhbiIsImNyZWF0ZWQiOiJTYXQgTWF5IDE3IDE1OjI4OjA4IElDVCAyMDI1IiwiZXhwIjoxNzQ4MDc1Mjg4fQ.Ct3_ey6NvH0MAxQFPrJVnFgSAo-tq_QbwVuq-NJEL_WZn6kXXm7scEuUm-6AwezyNlETZxEW8nXmh7RaXS5naQ";
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);
export default api;
