import { defineStore } from "pinia";

export const useRecentSelectionsStore = defineStore("recentSelections", {
  state: () => ({
    items: [],
  }),

  actions: {
    loadFromStorage(storageKey = "recentlySelected") {
      const stored = localStorage.getItem(storageKey);
      if (stored) {
        try {
          this.items = JSON.parse(stored);
        } catch (e) {
          console.error("Failed to parse recent selections", e);
          this.items = [];
        }
      }
    },

    saveToStorage(storageKey = "recentlySelected") {
      localStorage.setItem(storageKey, JSON.stringify(this.items));
    },

    addSelection(item, limit = 10, storageKey = "recentlySelected") {
      if (!item || !item.id) {
        console.error("Invalid item:", item);
        return;
      }

      // Remove if already exists
      this.items = this.items.filter((i) => i.id !== item.id);

      // Add to front
      this.items.unshift(item);

      // Limit the number of items
      if (this.items.length > limit) {
        this.items = this.items.slice(0, limit);
      }

      this.saveToStorage(storageKey);
    },

    removeSelection(id, storageKey = "recentlySelected") {
      this.items = this.items.filter((item) => item.id !== id);
      this.saveToStorage(storageKey);
    },

    clearSelections(storageKey = "recentlySelected") {
      this.items = [];
      localStorage.removeItem(storageKey);
    },
  },
});
