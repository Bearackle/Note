<template>
  <div class="home-container">
    <div class="welcome-section">
      <div class="welcome-icon">
        <Vue3Lottie
          :animation-data="animationData"
          :height="180"
          :width="180"
          :loop="true"
          :autoplay="true"
        />
      </div>
      <h1 class="welcome-text">Hello, how do you do</h1>
    </div>

    <div class="search-section">
      <input
        type="text"
        class="search-input"
        placeholder="Find anything from your workspace..."
        v-model="searchQuery"
      />
      <div v-if="searchResults.length > 0" class="search-popup">
        <ul>
          <li
            v-for="(item, idx) in searchResults"
            :key="item.pageId"
            class="search-result-item"
            @click="handlePageClick(item.pageId, item.title)"
          >
            <div class="result-title">{{ item.title }}</div>
            <div class="result-content">{{ item.blockContent }}</div>
            <div class="result-meta">{{ item.workspaceName }}</div>
          </li>
        </ul>
      </div>
    </div>

    <div class="recently-visited">
      <h2 class="section-title">Recently visited</h2>
      <div class="cards-container">
        <div
          v-for="(page, index) in recentPages"
          :key="index"
          class="page-card"
          @click="handleRecentPageClick(page.id, page.title)"
        >
          <div class="card-icon">
            <img :src="'/icons/document.svg'" :alt="page.title" />
          </div>
          <div class="card-title">{{ page.title }}</div>
        </div>
      </div>
      <button class="nav-button prev-button" @click="scrollPrev">
        <svg
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M15 18L9 12L15 6"
            stroke="#808080"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
        </svg>
      </button>
      <button class="nav-button next-button" @click="scrollNext">
        <svg
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M9 18L15 12L9 6"
            stroke="#808080"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
        </svg>
      </button>
    </div>
  </div>
</template>

<script>
import api from "../../api/axios";
import { usePageStore } from "@/store/page";
import { useBlockStore } from "@/store/block";
import { useRouter } from "vue-router";
import { useRecentSelectionsStore } from "@/store/recentSelections";
import { Vue3Lottie } from "vue3-lottie";
import animationData from "../../assets/animation/hello-human.json";

export default {
  name: "HomeView",
  components: {
    Vue3Lottie,
  },
  data() {
    return {
      searchQuery: "",
      recentPages: [],
      debouncedSearch: null,
      searchResults: [],
      pageStore: usePageStore(),
      blockStore: useBlockStore(),
      router: useRouter(),
      recentSelectionsStore: useRecentSelectionsStore(),
      animationData: animationData,
    };
  },
  methods: {
    debounce(func, wait) {
      let timeout;
      return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => {
          func.apply(this, args);
        }, wait);
      };
    },
    fulltextSearch(query) {
      console.log("debounced search", query);
      api
        .get("/block/search", {
          params: {
            keyword: query,
          },
        })
        .then((response) => {
          console.log("API response:", response);
          this.searchResults = Array.isArray(response.data)
            ? response.data
            : response.data && Array.isArray(response.data.data)
            ? response.data.data
            : [];
          console.log("Set searchResults:", this.searchResults);
        });
    },
    async handlePageClick(pageId) {
      this.pageStore.setSelectedPage(pageId);
      this.pageStore.setSelectedTitle(
        this.searchResults.find((page) => page.pageId === pageId)?.pageTitle
      );
      console.log("Selected title:", this.pageStore.selectedTitle);
      const response = await api.get("/block/list/" + pageId);
      console.log("Response from block/list:", response.data);
      if (response.data.data == null) {
        useBlockStore.selectedBlockIndex = 0;
      }
      // Access the nested data array from response.data.data
      const blocks = Array.isArray(response.data.data)
        ? response.data.data.map((element) => ({
            type: element.type,
            data: JSON.parse(element.content),
            id: element.idBlock,
          }))
        : [];

      console.log("Transformed blocks:", blocks);

      this.pageStore.setCurrentContent({
        time: new Date().getTime(),
        version: "2.30.0",
        blocks: blocks,
      });
      console.log("Store content after update:", this.pageStore.currentContent);

      // Navigate to /note/edit route
      this.router.push("/note/edit");
    },
    async handleRecentPageClick(pageId, pageTitle) {
      try {
        await this.pageStore.setSelectedPage(pageId);
        await this.pageStore.setSelectedTitle(pageTitle);
        const response = await api.get("/block/list/" + pageId);
        console.log("Response from block/list:", response.data);

        if (response.data.data == null) {
          this.blockStore.selectedBlockIndex = 0;
        }

        const blocks = Array.isArray(response.data.data)
          ? response.data.data.map((element) => ({
              type: element.type,
              data: JSON.parse(element.content),
              id: element.idBlock,
            }))
          : [];

        console.log("Transformed blocks:", blocks);

        this.pageStore.setCurrentContent({
          time: new Date().getTime(),
          version: "2.30.0",
          blocks: blocks,
        });

        // Add to recent selections with full item data
        const pageItem = {
          id: pageId,
          title: this.pageStore.selectedTitle || "Untitled",
          timestamp: new Date().getTime(),
        };
        this.recentSelectionsStore.addSelection(pageItem);

        // Navigate to editor
        this.router.push("/note/edit");
      } catch (error) {
        console.error("Error handling page click:", error);
      }
    },
    scrollNext() {
      const container = document.querySelector(".cards-container");
      container.scrollBy({ left: 200, behavior: "smooth" });
    },
    scrollPrev() {
      const container = document.querySelector(".cards-container");
      container.scrollBy({ left: -200, behavior: "smooth" });
    },
  },
  async mounted() {
    this.debouncedSearch = this.debounce(this.fulltextSearch, 300);
    await this.recentSelectionsStore.loadFromStorage();
    this.recentPages = this.recentSelectionsStore.items;

    // Watch for changes in the store
    this.$watch(
      () => this.recentSelectionsStore.items,
      (newItems) => {
        this.recentPages = newItems;
      }
    );
  },
  watch: {
    searchQuery(newQuery) {
      this.debouncedSearch(newQuery);
    },
  },
};
</script>

<style scoped>
.home-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

.welcome-section {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.welcome-icon {
  width: 120px;
  height: 120px;
  background: #f5f5f5;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  overflow: hidden;
}

.welcome-icon :deep(.vue3-lottie) {
  width: 100% !important;
  height: 100% !important;
  transform: scale(1.8);
}

.welcome-text {
  font-size: 2rem;
  font-weight: 500;
  color: #1a1a1a;
  margin: 0;
}

.search-section {
  margin-bottom: 2rem;
  position: relative;
}

.search-input {
  width: 100%;
  padding: 1rem 1.5rem;
  font-size: 1rem;
  color: #1a1a1a;
  background: #f5f5f5;
  border: none;
  border-radius: 12px;
  transition: background-color 0.2s ease;
}

.search-input::placeholder {
  color: #666;
}

.search-input:focus {
  outline: none;
  background: #ebebeb;
}

.search-popup {
  position: absolute;
  left: 0;
  right: 0;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
  margin-top: 0.5rem;
  max-height: 300px;
  overflow-y: auto;
}

.search-result-item {
  padding: 1rem;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.search-result-item:last-child {
  border-bottom: none;
}

.result-title {
  font-weight: bold;
  color: #2c3e50;
}

.result-content {
  color: #555;
  margin-top: 0.25rem;
}

.result-meta {
  color: #888;
  font-size: 0.85rem;
  margin-top: 0.25rem;
}

.recently-visited {
  margin-top: 2rem;
  padding: 1rem;
  position: relative;
}

.section-title {
  color: #8b8b8b;
  font-size: 0.9rem;
  margin-bottom: 1rem;
  font-weight: normal;
}

.cards-container {
  display: flex;
  gap: 1rem;
  overflow-x: auto;
  padding-bottom: 1rem;
  padding-right: 3rem;
  position: relative;
  scroll-behavior: smooth;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.cards-container::-webkit-scrollbar {
  display: none; /* Chrome, Safari and Opera */
}

.page-card {
  min-width: 150px;
  height: 150px;
  border-radius: 12px;
  padding: 0;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  font-family: "Inter", sans-serif;
  position: relative;
  overflow: hidden;
}

.page-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 50%;
  background: #f5f5f5;
  border-radius: 12px 12px 0 0;
}

.page-card::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50%;
  background: #ebebeb;
  border-radius: 0 0 12px 12px;
}

.card-icon {
  position: absolute;
  left: 1rem;
  top: 1rem;
  transform: none;
  width: 40px;
  height: 40px;
  background: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 2;
}

.card-icon img {
  width: 24px;
  height: 24px;
  object-fit: contain;
}

.card-title {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 1rem;
  color: #0a0a0a;
  font-size: 0.9rem;
  font-weight: 500;
  text-align: center;
  word-break: break-word;
  z-index: 1;
}

.nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 3;
  padding: 0;
  transition: all 0.2s ease;
}

.prev-button {
  left: 1rem;
}

.next-button {
  right: 1rem;
}

.nav-button:hover {
  background: #f5f5f5;
  transform: translateY(-50%) scale(1.1);
}

.nav-button:focus {
  outline: none;
  box-shadow: 0 0 0 2px #e0e0e0;
}
</style>
