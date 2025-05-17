import { defineStore } from "pinia";

export const usePageStore = defineStore("page", {
  state: () => ({
    pages: [],
    selectedPage: null,
    currentContent: null,
  }),
  actions: {
    addPage(page) {
      this.pages.push(page);
    },
    setSelectedPage(page) {
      this.selectedPage = page;
    },
    createNewPage(workspaceId) {
      const newPage = {
        id: Date.now(), // Temporary ID until backend creates one
        title: "Untitled",
        workspaceId: workspaceId,
        content: {
          time: new Date().getTime(),
          blocks: [
            {
              type: "paragraph",
              data: {
                text: "Start writing here...",
              },
            },
          ],
          version: "2.30.0",
        },
      };
      this.addPage(newPage);
      this.setSelectedPage(newPage.id);
      this.currentContent = newPage.content;
      return newPage;
    },
    setCurrentContent(content) {
      this.currentContent = content;
    },
  },
});
