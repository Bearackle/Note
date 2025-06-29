import { defineStore } from "pinia";

export const useFavoritesStore = defineStore("favorites", {
  state: () => ({
    items: [],
  }),

  actions: {
    addFavorite(page) {
      if (!this.items.find((item) => item.id === page.id)) {
        this.items.push({
          id: page.id,
          title: page.title,
          timestamp: new Date().getTime(),
        });
        this.saveToStorage();
      }
    },

    removeFavorite(pageId) {
      this.items = this.items.filter((item) => item.id !== pageId);
      this.saveToStorage();
    },

    isFavorite(pageId) {
      return this.items.some((item) => item.id === pageId);
    },

    saveToStorage() {
      localStorage.setItem("favorites", JSON.stringify(this.items));
    },

    async loadFromStorage() {
      const stored = localStorage.getItem("favorites");
      if (stored) {
        this.items = JSON.parse(stored);
      }
    },

    clearFavorites() {
      this.items = [];
      this.saveToStorage();
    },
  },
});
