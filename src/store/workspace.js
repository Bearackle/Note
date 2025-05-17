import { defineStore } from "pinia";
export const useWorkspaceStore = defineStore("workspace", {
  state: () => ({
    spaces: [],
    selectedSpace: null,
  }),
  actions: {
    addWorkspace(workspace) {
      this.spaces.push(workspace);
    },
    removeWorkspace(workspaceId) {
      this.spaces = this.spaces.filter((space) => space.id !== workspaceId);
    },
    setSelectedSpace(spaceId) {
      this.selectedSpace = spaceId;
    },
  },
});
