<template>
  <div class="home-container">
    <h1 class="welcome-text">Hello Huan</h1>

    <div class="search-section">
      <input
        type="text"
        class="search-bar"
        placeholder="Search..."
        v-model="searchQuery"
      />
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
    };
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
