import { defineStore } from "pinia";

export const useTeamStore = defineStore("team", {
  state: () => ({
    selectedTeam: null,
    teams: [],
    teamMembers: {},
  }),

  actions: {
    setSelectedTeam(teamId) {
      this.selectedTeam = teamId;
    },

    setTeams(teams) {
      this.teams = teams;
    },

    setTeamMembers(teamId, members) {
      this.teamMembers[teamId] = members;
    },

    addTeam(team) {
      this.teams.push(team);
    },

    updateTeam(teamId, updatedTeam) {
      const index = this.teams.findIndex((t) => t.id === teamId);
      if (index !== -1) {
        this.teams[index] = { ...this.teams[index], ...updatedTeam };
      }
    },

    removeTeam(teamId) {
      this.teams = this.teams.filter((t) => t.id !== teamId);
      delete this.teamMembers[teamId];
      if (this.selectedTeam === teamId) {
        this.selectedTeam = null;
      }
    },

    addMember(teamId, member) {
      if (!this.teamMembers[teamId]) {
        this.teamMembers[teamId] = [];
      }
      this.teamMembers[teamId].push(member);
    },

    removeMember(teamId, memberId) {
      if (this.teamMembers[teamId]) {
        this.teamMembers[teamId] = this.teamMembers[teamId].filter(
          (m) => m.id !== memberId
        );
      }
    },
  },

  getters: {
    currentTeam: (state) => {
      return state.teams.find((team) => team.id === state.selectedTeam);
    },

    currentTeamMembers: (state) => {
      return state.selectedTeam
        ? state.teamMembers[state.selectedTeam] || []
        : [];
    },
  },
});
