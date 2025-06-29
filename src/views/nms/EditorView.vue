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
        <button
          class="favorite-button"
          :class="{ 'is-favorite': isFavorite }"
          @click="toggleFavorite"
          :title="isFavorite ? 'Remove from favorites' : 'Add to favorites'"
        >
          <svg
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="currentColor"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
            />
          </svg>
        </button>
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
    <div class="editor-wrapper">
      <Editor
        class="editor"
        @update:content="handleContent"
        @onChange:content="handleChange"
      />
    </div>
    <!-- Share Modal -->
    <div v-if="shareModalVisible" class="modal-overlay">
      <div class="modal-content">
        <div class="modal-header">
          <h3>Share your work</h3>
          <button class="close-btn" @click="shareModalVisible = false">
            &times;
          </button>
        </div>
        <div class="modal-body">
          <div class="share-url">
            <label>URL:</label>
            <input type="text" readonly :value="shareUrl" />
          </div>
          <div class="share-role">
            <label>Role:</label>
            <select v-model="shareRole">
              <option value="viewer">Viewer</option>
              <option value="editor">Editor</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Editor from "../../components/Editor.vue";
import api from "../../api/axios";
import { usePageStore } from "../../store/page";
import { debounce } from "lodash";
import { useHashids } from "../../utils/useHashids";
import { useFavoritesStore } from "@/store/favorites";

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
      shareModalVisible: false,
      shareRole: "viewer",
      favoritesStore: useFavoritesStore(),
    };
  },
  setup() {
    const pageStore = usePageStore();
    return {
      pageStore,
    };
  },
  computed: {
    shareUrl() {
      const { encode } = useHashids();
      return `localhost:5173/share/${encode(
        this.pageStore.selectedPage || ""
      )}`;
    },
    isFavorite() {
      return this.favoritesStore.isFavorite(this.pageStore.selectedPage);
    },
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
      this.shareModalVisible = true;
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
    toggleFavorite() {
      if (this.isFavorite) {
        this.favoritesStore.removeFavorite(this.pageStore.selectedPage);
      } else {
        this.favoritesStore.addFavorite({
          id: this.pageStore.selectedPage,
          title: this.pageStore.selectedTitle,
        });
      }
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
  position: relative;
  background: linear-gradient(to bottom right, #f8f9fa, #e9ecef);
}

.taskbar {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1.5rem;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  height: 64px;
  transition: all 0.3s ease;
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
  padding: 0.5rem 1rem;
  cursor: pointer;
  text-align: left;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.title-button:hover {
  background: rgba(0, 0, 0, 0.05);
  transform: translateY(-1px);
}

.page-title {
  margin: 0;
  font-size: 1.3rem;
  color: #2d3748;
  font-weight: 600;
  letter-spacing: -0.02em;
}

.title-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 0.5rem;
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  animation: fadeIn 0.2s ease;
}

.title-input {
  padding: 0.75rem 1rem;
  font-size: 1.2rem;
  border: none;
  outline: none;
  width: 240px;
  border-radius: 8px;
  background: #f8f9fa;
  color: #2d3748;
  transition: all 0.2s ease;
}

.title-input:focus {
  background: white;
  box-shadow: 0 0 0 2px rgba(66, 153, 225, 0.2);
}

.taskbar-right {
  display: flex;
  align-items: center;
  gap: 1.25rem;
}

.api-status {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 0.95rem;
  color: #4a5568;
  padding: 0.5rem 1rem;
  background: #f8f9fa;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.status-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #48bb78;
  transition: all 0.3s ease;
  box-shadow: 0 0 0 2px rgba(72, 187, 120, 0.2);
}

.api-status.loading .status-dot {
  background-color: #ecc94b;
  animation: pulse 1.5s infinite;
  box-shadow: 0 0 0 2px rgba(236, 201, 75, 0.2);
}

.share-button {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1.25rem;
  background: linear-gradient(135deg, #4299e1, #667eea);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(66, 153, 225, 0.2);
}

.share-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(66, 153, 225, 0.3);
  background: linear-gradient(135deg, #3182ce, #5a67d8);
}

.share-button:active {
  transform: translateY(0);
}

@keyframes pulse {
  0% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.6;
    transform: scale(1.2);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.editor-wrapper {
  padding-top: 64px;
  height: 100%;
  width: 100%;
  min-width: 100%;
  overflow-y: auto;
  position: relative;
}

.editor {
  padding: 2rem;
  height: 100%;
  width: 100%;
  min-width: 100%;
  overflow-y: auto;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  animation: fadeIn 0.3s ease;
}

.modal-content {
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 2.5rem;
  min-width: 380px;
  max-width: 90vw;
  animation: slideUp 0.3s ease;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.modal-header h3 {
  font-size: 1.5rem;
  color: #2d3748;
  font-weight: 600;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.75rem;
  color: #718096;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 50%;
  transition: all 0.2s ease;
  line-height: 1;
}

.close-btn:hover {
  background: #f7fafc;
  color: #4a5568;
  transform: rotate(90deg);
}

.modal-body label {
  font-weight: 600;
  color: #4a5568;
  margin-bottom: 0.5rem;
  display: block;
}

.share-url input {
  width: 100%;
  margin-top: 0.5rem;
  margin-bottom: 1.5rem;
  padding: 0.75rem 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
  color: #2d3748;
  background: #f8f9fa;
  transition: all 0.2s ease;
}

.share-url input:focus {
  border-color: #4299e1;
  box-shadow: 0 0 0 2px rgba(66, 153, 225, 0.2);
  outline: none;
}

.share-role select {
  padding: 0.75rem 1rem;
  border-radius: 8px;
  border: 2px solid #e2e8f0;
  width: 100%;
  font-size: 1rem;
  color: #2d3748;
  background: #f8f9fa;
  cursor: pointer;
  transition: all 0.2s ease;
}

.share-role select:focus {
  border-color: #4299e1;
  box-shadow: 0 0 0 2px rgba(66, 153, 225, 0.2);
  outline: none;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.favorite-button {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--color-background);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 0;
  transition: all 0.3s ease;
  color: var(--vt-c-text-light-2);
  margin-left: 1rem;
}

.favorite-button:hover {
  transform: scale(1.1);
  color: #ff4757;
}

.favorite-button.is-favorite {
  color: #ff4757;
}

.favorite-button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(255, 71, 87, 0.3);
}
</style>
