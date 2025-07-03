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
      <h1 class="welcome-text">Hello, how do you do?</h1>
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
      <button class="nav-button prev-button" @click="scrollPrev('recent')">
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
      <button class="nav-button next-button" @click="scrollNext('recent')">
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

    <div class="favorites-section">
      <h2 class="section-title">Your favorites</h2>
      <div class="cards-container">
        <div
          v-for="(page, index) in favoritePages"
          :key="index"
          class="page-card"
          @click="handleFavoritePageClick(page.id, page.title)"
        >
          <div class="card-icon">
            <img :src="'/icons/smile.svg'" :alt="page.title" />
          </div>
          <div class="card-title">{{ page.title }}</div>
          <button class="favorite-button" @click.stop="removeFavorite(page.id)">
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
      </div>
      <button class="nav-button prev-button" @click="scrollPrev('favorites')">
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
      <button class="nav-button next-button" @click="scrollNext('favorites')">
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
import { useFavoritesStore } from "@/store/favorites";

export default {
  name: "HomeView",
  components: {
    Vue3Lottie,
  },
  data() {
    return {
      searchQuery: "",
      recentPages: [],
      favoritePages: [],
      debouncedSearch: null,
      searchResults: [],
      pageStore: usePageStore(),
      blockStore: useBlockStore(),
      router: useRouter(),
      recentSelectionsStore: useRecentSelectionsStore(),
      favoritesStore: useFavoritesStore(),
      animationData: animationData,
    };
  },
  methods: {
    stripHtml(html) {
      const temp = document.createElement("div");
      temp.innerHTML = html;
      return temp.innerText || temp.textContent || "";
    },
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
          this.searchResults =
            response.data && Array.isArray(response.data.data)
              ? response.data.data.map((item) => ({
                  ...item,
                  blockContent: this.stripHtml(item.blockContent),
                }))
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
    scrollNext(section) {
      const container = document.querySelector(
        section === "recent"
          ? ".recently-visited .cards-container"
          : `.${section}-section .cards-container`
      );
      if (container) {
        container.scrollBy({ left: 200, behavior: "smooth" });
      }
    },
    scrollPrev(section) {
      const container = document.querySelector(
        section === "recent"
          ? ".recently-visited .cards-container"
          : `.${section}-section .cards-container`
      );
      if (container) {
        container.scrollBy({ left: -200, behavior: "smooth" });
      }
    },
    async handleFavoritePageClick(pageId, pageTitle) {
      await this.handleRecentPageClick(pageId, pageTitle);
    },
    removeFavorite(pageId) {
      this.favoritesStore.removeFavorite(pageId);
      this.favoritePages = this.favoritesStore.items;
    },
  },
  async mounted() {
    this.debouncedSearch = this.debounce(this.fulltextSearch, 300);
    await this.recentSelectionsStore.loadFromStorage();
    await this.favoritesStore.loadFromStorage();
    this.recentPages = this.recentSelectionsStore.items;
    this.favoritePages = this.favoritesStore.items;

    // Watch for changes in both stores
    this.$watch(
      () => this.recentSelectionsStore.items,
      (newItems) => {
        this.recentPages = newItems;
      }
    );
    this.$watch(
      () => this.favoritesStore.items,
      (newItems) => {
        this.favoritePages = newItems;
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
  max-width: 900px;
  margin: 0 auto;
  padding: 3rem 2rem;
  min-height: 100vh;
  background: var(--color-background);
  position: relative;
}

.welcome-section {
  display: flex;
  align-items: center;
  gap: 2rem;
  margin-bottom: 3rem;
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.welcome-icon {
  width: 140px;
  height: 140px;
  background: var(--color-background-soft);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
  overflow: hidden;
}

.welcome-icon:hover {
  transform: scale(1.05);
}

.welcome-icon :deep(.vue3-lottie) {
  width: 100% !important;
  height: 100% !important;
  transform: scale(1.8);
}

.welcome-text {
  font-size: 2.5rem;
  font-weight: 600;
  color: var(--color-heading);
  margin: 0;
  line-height: 1.2;
  animation: fadeIn 0.8s ease-out 0.3s both;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.search-section {
  margin-bottom: 3rem;
  position: relative;
  animation: fadeIn 0.6s ease-out 0.6s both;
  z-index: 50;
}

.search-input {
  width: 100%;
  padding: 1.25rem 1.75rem;
  font-size: 1.1rem;
  color: var(--color-text);
  background: var(--color-background-soft);
  border: 2px solid transparent;
  border-radius: 16px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
}

.search-input::placeholder {
  color: var(--vt-c-text-light-2);
}

.search-input:focus {
  outline: none;
  border-color: var(--vt-c-indigo);
  background: var(--color-background);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.05);
}

.search-popup {
  position: absolute;
  left: 0;
  right: 0;
  background: var(--color-background);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  z-index: 100;
  margin-top: 0.75rem;
  max-height: 400px;
  overflow-y: auto;
  animation: fadeInDown 0.3s ease-out;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.search-result-item {
  padding: 1.25rem;
  border-bottom: 1px solid var(--color-border);
  cursor: pointer;
  transition: all 0.2s ease;
}

.search-result-item:hover {
  background: var(--color-background-soft);
}

.search-result-item:last-child {
  border-bottom: none;
}

.result-title {
  font-weight: 600;
  color: var(--color-heading);
  margin-bottom: 0.5rem;
}

.result-content {
  color: var(--color-text);
  margin-top: 0.5rem;
  font-size: 0.95rem;
  line-height: 1.5;
}

.result-meta {
  color: var(--vt-c-text-light-2);
  font-size: 0.85rem;
  margin-top: 0.5rem;
}

.recently-visited {
  margin-top: 3rem;
  padding: 1rem;
  position: relative;
  animation: fadeIn 0.6s ease-out 0.9s both;
  z-index: 1;
}

.section-title {
  color: var(--vt-c-text-light-2);
  font-size: 1rem;
  margin-bottom: 1.5rem;
  font-weight: 500;
  letter-spacing: 0.5px;
}

.cards-container {
  display: flex;
  gap: 1.5rem;
  overflow-x: auto;
  padding: 0.5rem 0.5rem 1.5rem;
  margin: 0 -0.5rem;
  position: relative;
  scroll-behavior: smooth;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.cards-container::-webkit-scrollbar {
  display: none;
}

.page-card {
  min-width: 180px;
  height: 180px;
  border-radius: 16px;
  padding: 0;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  font-family: "Inter", sans-serif;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.page-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.page-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 50%;
  background: var(--color-background-soft);
  border-radius: 16px 16px 0 0;
  transition: all 0.3s ease;
}

.page-card::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50%;
  background: var(--color-background-mute);
  border-radius: 0 0 16px 16px;
  transition: all 0.3s ease;
}

.card-icon {
  position: absolute;
  left: 50%;
  top: 25%;
  transform: translate(-50%, -50%);
  width: 48px;
  height: 48px;
  background: var(--color-background);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  z-index: 2;
  transition: all 0.3s ease;
}

.page-card:hover .card-icon {
  transform: translate(-50%, -50%) scale(1.1);
}

.card-icon img {
  width: 28px;
  height: 28px;
  object-fit: contain;
  transition: all 0.3s ease;
}

.card-title {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 1.25rem;
  color: var(--color-heading);
  font-size: 1rem;
  font-weight: 500;
  text-align: center;
  word-break: break-word;
  z-index: 1;
  transition: all 0.3s ease;
}

.nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--color-background);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  z-index: 3;
  padding: 0;
  transition: all 0.3s ease;
}

.prev-button {
  left: 0;
}

.next-button {
  right: 0;
}

.nav-button:hover {
  background: var(--color-background-soft);
  transform: translateY(-50%) scale(1.1);
}

.nav-button:focus {
  outline: none;
  box-shadow: 0 0 0 3px var(--color-border-hover);
}

.nav-button svg {
  transition: all 0.3s ease;
}

.nav-button:hover svg path {
  stroke: var(--color-heading);
}

.favorites-section {
  margin-top: 3rem;
  padding: 1rem;
  position: relative;
  animation: fadeIn 0.6s ease-out 1.2s both;
  z-index: 1;
}

.favorite-button {
  position: absolute;
  top: 1rem;
  right: 1rem;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--color-background);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 3;
  padding: 0;
  transition: all 0.3s ease;
  color: #ff4757;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.favorite-button:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.favorite-button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(255, 71, 87, 0.3);
}
</style>
