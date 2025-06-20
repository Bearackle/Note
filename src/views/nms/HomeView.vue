<template>
  <div class="home-container">
    <h1 class="welcome-text">Hello, how do you do</h1>

    <div class="search-section">
      <input
        type="text"
        class="search-bar"
        placeholder="Search..."
        v-model="searchQuery"
      />
      <div v-if="searchResults.length > 0" class="search-popup">
        <ul>
          <li
            v-for="(item, idx) in searchResults"
            :key="item.pageId"
            class="search-result-item"
            @click="handlePageClick(item.pageId)"
          >
            <div class="result-title">{{ item.pageTitle }}</div>
            <div class="result-content">{{ item.blockContent }}</div>
            <div class="result-meta">{{ item.workspaceName }}</div>
          </li>
        </ul>
      </div>
    </div>

    <div class="recent-pages">
      <h2>Recent Pages</h2>
      <ul class="recent-list">
        <li
          v-for="(page, index) in recentPages"
          :key="index"
          class="recent-item"
        >
          <span class="page-name">{{ page.name }}</span>
          <span class="page-time">{{ page.lastOpened }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import api from "../../api/axios";
import { usePageStore } from "@/store/page";
import { useBlockStore } from "@/store/block";
import { useRouter } from "vue-router";
export default {
  name: "HomeView",
  data() {
    return {
      searchQuery: "",
      recentPages: [
        { name: "Dashboard", lastOpened: "2 hours ago" },
        { name: "Settings", lastOpened: "Yesterday" },
        { name: "Profile", lastOpened: "3 days ago" },
      ],
      debouncedSearch: null,
      searchResults: [],
      pageStore: usePageStore(),
      blockStore: useBlockStore(),
      router: useRouter(),
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
  },
  mounted() {
    this.debouncedSearch = this.debounce(this.fulltextSearch, 300);
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

.welcome-text {
  font-size: 3.5rem;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 2rem;
  text-align: center;
}

.search-section {
  margin-bottom: 2rem;
  position: relative;
}

.search-bar {
  width: 100%;
  padding: 1rem;
  font-size: 1.1rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  transition: border-color 0.3s ease;
}

.search-bar:focus {
  outline: none;
  border-color: #42b983;
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

.recent-pages {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 1.5rem;
}

.recent-pages h2 {
  color: #2c3e50;
  margin-bottom: 1rem;
  font-size: 1.5rem;
}

.recent-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.recent-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid #e0e0e0;
}

.recent-item:last-child {
  border-bottom: none;
}

.page-name {
  font-weight: 500;
  color: #2c3e50;
}

.page-time {
  color: #666;
  font-size: 0.9rem;
}
</style>
