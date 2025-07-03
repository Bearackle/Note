import { defineStore } from "pinia";

export const useBlockStore = defineStore("block", {
  state: () => ({
    order: 0,
    selectedBlockIndex: null,
  }),
  actions: {
    countUp() {
      this.order++;
    },
  },
  resetZero() {
    this.order = 0;
  },
  setSelectedBlockIndex(id) {
    this.selectedBlockId = id;
  },
  getSelectedBlockIndex() {
    return this.selectedBlockIndex;
  },
});
