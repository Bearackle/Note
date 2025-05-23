<template>
  <div class="editor-container">
    <div class="taskbar">
      <div class="taskbar-left">
        <div class="title-container">
          <button class="title-button" @click="isEditing = true">
            <h2 class="page-title">{{ pageStore.selectedTitle }}</h2>
          </button>
          <div v-if="isEditing" class="title-dropdown">
            <input
              v-model="tempTitle"
              class="title-input"
              placeholder="Enter title"
              @keyup.enter="saveTitle"
              @blur="saveTitle"
              ref="titleInput"
            />
          </div>
        </div>
      </div>
      <div class="taskbar-right">
        <div class="api-status" :class="{ loading: isApiLoading }">
          <span class="status-dot"></span>
          <span class="status-text">{{ apiStatus }}</span>
        </div>
        <button class="share-button" @click="handleShare">
          <i class="fas fa-share-alt"></i>
          Share
        </button>
      </div>
    </div>
    <Editor
      class="editor"
      @update:content="handleContent"
      @onChange:content="handleChange"
    />
  </div>
</template>

<script>
import Editor from "../../components/Editor.vue";
import api from "../../api/axios";
import { usePageStore } from "../../store/page";
import { debounce } from "lodash";

export default {
  components: { Editor },
  data() {
    return {
      isApiLoading: false,
      apiStatus: "Ready",
      apiCallCount: 0,
      isEditing: false,
      tempTitle: "",
      statusTimeout: null,
    };
  },
  setup() {
    const pageStore = usePageStore();
    return {
      pageStore,
    };
  },
  created() {
    // Create debounced function once
    this.debouncedUpdateStatus = debounce((status) => {
      this.isApiLoading = false;
      this.apiStatus = status;
    }, 1000);

    // Intercept API calls to track status
    api.interceptors.request.use((config) => {
      this.apiCallCount++;
      this.updateApiStatus("Loading...");
      return config;
    });

    api.interceptors.response.use(
      (response) => {
        this.apiCallCount--;
        if (this.apiCallCount === 0) {
          this.debouncedUpdateStatus("Saved");
        }
        return response;
      },
      (error) => {
        this.apiCallCount--;
        if (this.apiCallCount === 0) {
          this.debouncedUpdateStatus("Error");
        }
        return Promise.reject(error);
      }
    );
  },
  methods: {
    handleContent(content) {
      this.isApiLoading = true;
      this.apiStatus = "Saving...";
      this.debouncedUpdateStatus("Saving...");
    },
    handleChange(content) {
      this.isApiLoading = true;
      this.apiStatus = "Saving...";
      this.debouncedUpdateStatus("Saved");
    },
    handleShare() {
      console.log("Share button clicked");
    },
    updateApiStatus(status) {
      this.isApiLoading = status === "loading";
      this.apiStatus = status;
    },
    saveTitle() {
      if (this.tempTitle.trim()) {
        const currentPage = this.pageStore.pages.find(
          (page) => page.id === this.pageStore.selectedPage
        );
        if (currentPage) {
          currentPage.title = this.tempTitle;
        }
      }
      this.isEditing = false;
    },
  },
  watch: {
    isEditing(newVal) {
      if (newVal) {
        const currentPage = this.pageStore.pages.find(
          (page) => page.id === this.pageStore.selectedPage
        );
        this.tempTitle = currentPage?.title || "";
        this.$nextTick(() => {
          this.$refs.titleInput?.focus();
        });
      }
    },
  },
  beforeUnmount() {
    // Cancel any pending debounced calls
    this.debouncedUpdateStatus.cancel();
  },
};
</script>

<style scoped>
.editor-container {
  width: 100%;
  min-width: 100%;
  height: 100vh;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.taskbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 1rem;
  /* background-color: #f5f5f5; */
  /* border-bottom: 1px solid #e0e0e0; */
  margin-bottom: 2rem;
}

.taskbar-left {
  display: flex;
  align-items: center;
}

.title-container {
  position: relative;
}

.title-button {
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  text-align: left;
}

.title-button:hover {
  opacity: 0.8;
}

.page-title {
  margin: 0;
  font-size: 1.2rem;
  color: #333;
}

.title-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 0.5rem;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.title-input {
  padding: 0.5rem;
  font-size: 1.2rem;
  border: none;
  outline: none;
  width: 200px;
}

.taskbar-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.api-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  color: #666;
  transition: all 0.3s ease;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #4caf50;
  transition: all 0.3s ease;
}

.api-status.loading .status-dot {
  background-color: #ffc107;
  animation: pulse 1s infinite;
}

.share-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.share-button:hover {
  background-color: #1976d2;
}

@keyframes pulse {
  0% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.5;
    transform: scale(1.2);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

.editor {
  flex: 1;
  width: 100%;
  height: 100%;
  min-width: 100%;
}
</style>
