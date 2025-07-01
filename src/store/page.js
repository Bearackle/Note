import { defineStore } from "pinia";

export const usePageStore = defineStore("page", {
  state: () => ({
    pages: [],
    selectedPage: null,
    currentContent: null,
    title: 0,
    selectedTitle: "",
    isReadOnly: null,
  }),
  actions: {
    setPages(pages) {
      this.pages = pages;
    },
    setSelectedPage(pageId) {
      this.selectedPage = pageId;
    },
    createNewPage(workspaceId) {
      const newPage = {
        id: Date.now(), // Temporary ID until backend creates one
        title: "Untitled #" + this.title,
        workspaceId: workspaceId,
        content: {
          time: new Date().getTime(),
          blocks: [],
          version: "2.30.0",
        },
      };
      if (this.pages != null) {
        this.setPages([...this.pages, newPage]);
      }
      this.setSelectedPage(newPage.id);
      this.currentContent = newPage.content;
      return newPage;
    },
    setCurrentContent(content) {
      this.currentContent = content;
    },
    setUndefinedTitle() {
      this.title += 1;
    },
    setSelectedTitle(title) {
      this.selectedTitle = title;
    },
    setReadOnly(isReadOnly) {
      this.isReadOnly = isReadOnly;
    },
  },
});
