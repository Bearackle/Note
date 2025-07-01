<template>
  <div class="sidebar-container">
    <div class="sidebar-fixed-top">
      <div class="account">
        <n-dropdown
          trigger="click"
          :options="options"
          placement="bottom-start"
          @select="handleOptionClick"
        >
          <div class="account-wrapper">
            <div class="avatar-container">
              <n-avatar
                size="medium"
                style="background: linear-gradient(135deg, #2080f0, #00e676)"
              >
                {{
                  Cookies.get("username")
                    ? Cookies.get("username").charAt(0).toUpperCase()
                    : "U"
                }}
              </n-avatar>
            </div>
            <div class="account-info">
              <div class="user-details">
                <span class="username">{{
                  Cookies.get("username") || "User"
                }}</span>
                <span class="user-email">{{ userEmail }}</span>
              </div>
            </div>
          </div>
        </n-dropdown>
      </div>
      <div class="quick-actions">
        <n-tooltip placement="right" trigger="hover">
          <template #trigger>
            <n-button quaternary circle class="action-button">
              <n-icon size="18"
                ><Icon icon="material-symbols:search-rounded"
              /></n-icon>
            </n-button>
          </template>
          Quick Search
        </n-tooltip>

        <n-tooltip placement="right" trigger="hover">
          <template #trigger>
            <n-button quaternary circle class="action-button">
              <n-icon size="18"
                ><Icon icon="material-symbols:notifications-rounded"
              /></n-icon>
              <div class="notification-badge">3</div>
            </n-button>
          </template>
          Notifications
        </n-tooltip>
      </div>
      <div class="home">
        <n-button
          quaternary
          @click="handleHomeClick"
          class="nav-button primary"
        >
          <template #icon>
            <n-icon size="20">
              <Icon icon="material-symbols:home-rounded" />
            </n-icon>
          </template>
          Home
        </n-button>
      </div>
      <div class="inbox">
        <n-button
          quaternary
          class="nav-button secondary"
          @click="showInboxModal = true"
        >
          <template #icon>
            <n-icon size="20">
              <Icon icon="material-symbols:inbox-rounded" />
            </n-icon>
          </template>
          Inbox
          <div class="inbox-counter" v-if="inboxCounter > 0">
            {{ inboxCounter }}
          </div>
        </n-button>

        <!-- Inbox Modal -->
        <n-modal
          v-model:show="showInboxModal"
          title="Inbox Messages"
          preset="card"
          style="max-width: 600px"
          :bordered="false"
        >
          <div class="inbox-management">
            <div v-if="inboxMessages.length > 0" class="inbox-list">
              <div
                v-for="message in inboxMessages"
                :key="message.id"
                class="inbox-item"
              >
                <div class="inbox-info">
                  <n-icon size="20" class="inbox-icon">
                    <Icon icon="material-symbols:person-add" />
                  </n-icon>
                  <span class="inbox-message">
                    {{ message.inviterId }} invited you to
                    {{ message.objectType }}
                  </span>
                </div>
                <div class="inbox-actions">
                  <n-button
                    type="success"
                    size="small"
                    @click="handleInvitationResponse(message.id, 'accept')"
                  >
                    Accept
                  </n-button>
                  <n-button
                    type="error"
                    size="small"
                    @click="handleInvitationResponse(message.id, 'reject')"
                  >
                    Reject
                  </n-button>
                </div>
              </div>
            </div>
            <n-empty v-else description="No pending invitations" />
          </div>
        </n-modal>
      </div>
    </div>

    <div class="sidebar-scrollable">
      <div class="workspace">
        <n-collapse>
          <n-collapse-item name="2">
            <template #header>
              <div class="collapse-header">
                <div class="header-content">
                  <n-icon size="18" class="section-icon team-icon">
                    <Icon icon="material-symbols:group-rounded" />
                  </n-icon>
                  <span>Teamspace</span>
                </div>
                <n-button
                  quaternary
                  size="small"
                  @click.stop="handleAddTeam"
                  class="add-button"
                >
                  <n-icon size="16">
                    <Add />
                  </n-icon>
                </n-button>
              </div>
            </template>
            <n-spin :show="isLoadingTeams">
              <div>
                <div
                  v-if="Array.isArray(teams) && teams.length > 0"
                  class="workspace-list"
                >
                  <div
                    v-for="team in teams"
                    :key="team.workspaceId"
                    class="team-item"
                  >
                    <div class="team-header">
                      <n-popover
                        trigger="hover"
                        placement="right"
                        :delay="500"
                        :duration="0"
                      >
                        <template #trigger>
                          <n-button
                            text
                            block
                            @click="handleTeamClick(team.workspaceId)"
                            :class="{
                              'team-selected':
                                teamStore.selectedTeam === team.workspaceId,
                            }"
                          >
                            <template #icon>
                              <n-icon size="18" class="team-icon">
                                <Icon icon="material-symbols:group-rounded" />
                              </n-icon>
                            </template>
                            <span class="team-name">{{
                              team.workspaceName
                            }}</span>
                            <div class="team-meta">
                              <div class="team-indicator"></div>
                            </div>
                          </n-button>
                        </template>
                        <div class="team-description-popover">
                          <div class="popover-title">Description</div>
                          <div class="popover-content">
                            {{ team.description || "No description available" }}
                          </div>
                        </div>
                      </n-popover>
                      <n-dropdown
                        trigger="click"
                        :options="teamActionOptions"
                        placement="bottom-end"
                        @select="(key) => handleTeamAction(key, team)"
                      >
                        <n-button
                          quaternary
                          size="small"
                          class="team-actions"
                          :class="{
                            'team-actions-selected':
                              teamStore.selectedTeam === team.workspaceId,
                          }"
                          @click.stop
                        >
                          <n-icon size="16">
                            <EllipsisVertical />
                          </n-icon>
                        </n-button>
                      </n-dropdown>
                    </div>
                    <!-- Add pages under team -->
                    <div
                      v-if="teamStore.selectedTeam === team.workspaceId"
                      class="nested-pages"
                    >
                      <div
                        v-for="page in teamPages"
                        :key="page.id"
                        class="page-item"
                      >
                        <n-button
                          text
                          block
                          @click="handlePageClick(page.id)"
                          :class="{
                            'page-selected': pageStore.selectedPage === page.id,
                          }"
                        >
                          <template #icon>
                            <n-icon size="18" class="page-icon">
                              <Icon
                                icon="material-symbols:description-rounded"
                              />
                            </n-icon>
                          </template>
                          <span class="page-title">{{
                            page.title || "Untitled"
                          }}</span>
                        </n-button>
                        <n-dropdown
                          trigger="click"
                          :options="pageOptions"
                          placement="bottom-end"
                          @select="(key) => handlePageAction(key, page)"
                        >
                          <n-button
                            quaternary
                            size="small"
                            class="page-actions"
                            :class="{
                              'page-actions-selected':
                                pageStore.selectedPage === page.id,
                            }"
                            @click.stop
                          >
                            <n-icon size="16">
                              <EllipsisVertical />
                            </n-icon>
                          </n-button>
                        </n-dropdown>
                      </div>
                      <n-button
                        quaternary
                        block
                        class="add-page-button"
                        @click="handleAddPages(team.workspaceId)"
                      >
                        <template #icon>
                          <n-icon size="16">
                            <Add />
                          </n-icon>
                        </template>
                        Add a page
                      </n-button>
                    </div>
                  </div>
                </div>
                <n-empty v-else description="No teams found" />
              </div>
            </n-spin>
          </n-collapse-item>
        </n-collapse>
        <n-collapse>
          <n-collapse-item name="1">
            <template #header>
              <div class="collapse-header">
                <div class="header-content">
                  <n-icon size="18" class="section-icon">
                    <Icon icon="material-symbols:space-dashboard-rounded" />
                  </n-icon>
                  <span>Workspace</span>
                </div>
                <n-button
                  quaternary
                  size="small"
                  @click.stop="handleAddWorkspace"
                  class="add-button"
                >
                  <n-icon size="16">
                    <Add />
                  </n-icon>
                </n-button>
              </div>
            </template>
            <n-spin :show="isLoading">
              <div>
                <div
                  v-if="Array.isArray(workspaces) && workspaces.length > 0"
                  class="workspace-list"
                >
                  <div
                    v-for="workspace in workspaces"
                    :key="workspace.id"
                    class="workspace-item"
                  >
                    <div class="workspace-header">
                      <n-popover
                        trigger="hover"
                        placement="right"
                        :delay="500"
                        :duration="0"
                      >
                        <template #trigger>
                          <n-button
                            text
                            block
                            @click="handleWorkspaceClick(workspace.id)"
                            :class="{
                              'workspace-selected':
                                workspaceStore.selectedSpace === workspace.id,
                            }"
                          >
                            <template #icon>
                              <n-icon size="18" class="workspace-icon">
                                <Icon
                                  icon="material-symbols:space-dashboard-rounded"
                                />
                              </n-icon>
                            </template>
                            {{ workspace.name }}
                            <div class="workspace-meta">
                              <div class="workspace-indicator"></div>
                            </div>
                          </n-button>
                        </template>
                        <div class="workspace-description-popover">
                          <div class="popover-title">Description</div>
                          <div class="popover-content">
                            {{
                              workspace.description ||
                              "No description available"
                            }}
                          </div>
                        </div>
                      </n-popover>
                      <n-dropdown
                        trigger="click"
                        :options="workspaceActionOptions"
                        placement="bottom-end"
                        @select="(key) => handleWorkspaceAction(key, workspace)"
                      >
                        <n-button
                          quaternary
                          size="small"
                          class="workspace-actions"
                          :class="{
                            'workspace-actions-selected':
                              workspaceStore.selectedSpace === workspace.id,
                          }"
                          @click.stop
                        >
                          <n-icon size="16">
                            <EllipsisVertical />
                          </n-icon>
                        </n-button>
                      </n-dropdown>
                    </div>
                    <!-- Add pages under workspace -->
                    <div
                      v-if="workspaceStore.selectedSpace === workspace.id"
                      class="nested-pages"
                    >
                      <div
                        v-for="page in pages"
                        :key="page.id"
                        class="page-item"
                      >
                        <n-button
                          text
                          block
                          @click="handlePageClick(page.id)"
                          :class="{
                            'page-selected': pageStore.selectedPage === page.id,
                          }"
                        >
                          <template #icon>
                            <n-icon size="18" class="page-icon">
                              <Icon
                                icon="material-symbols:description-rounded"
                              />
                            </n-icon>
                          </template>
                          <span class="page-title">{{
                            page.title || "Untitled"
                          }}</span>
                        </n-button>
                        <n-dropdown
                          trigger="click"
                          :options="pageOptions"
                          placement="bottom-end"
                          @select="(key) => handlePageAction(key, page)"
                        >
                          <n-button
                            quaternary
                            size="small"
                            class="page-actions"
                            :class="{
                              'page-actions-selected':
                                pageStore.selectedPage === page.id,
                            }"
                            @click.stop
                          >
                            <n-icon size="16">
                              <EllipsisVertical />
                            </n-icon>
                          </n-button>
                        </n-dropdown>
                      </div>
                      <n-button
                        quaternary
                        block
                        class="add-page-button"
                        @click="handleAddPages(workspace.id)"
                      >
                        <template #icon>
                          <n-icon size="16">
                            <Add />
                          </n-icon>
                        </template>
                        Add a page
                      </n-button>
                    </div>
                  </div>
                </div>
                <n-empty v-else description="No workspaces found" />
              </div>
            </n-spin>
          </n-collapse-item>
        </n-collapse>
        <n-modal
          v-model:show="showModalWorkspace"
          title="Tạo workspace"
          preset="dialog"
          positive-text="Save"
          negative-text="Cancel"
          @positive-click="handleSaveWorkspace"
          @negative-click="showModalWorkspace = false"
          @keydown.enter="handleSaveWorkspace"
          @keydown.esc="showModalWorkspace = false"
        >
          <n-form ref="formRef" :model="formData" :rules="formRules">
            <n-form-item label="Name" path="name">
              <n-input v-model:value="formData.name" placeholder="Enter name" />
            </n-form-item>
            <n-form-item label="Description" path="description">
              <n-input
                v-model:value="formData.description"
                type="textarea"
                placeholder="Enter description"
              />
            </n-form-item>
          </n-form>
        </n-modal>
        <!-- Add workspace settings modal -->
        <n-modal
          v-model:show="showWorkspaceSettingsModal"
          :title="workspaceModalTitle"
          preset="dialog"
          positive-text="Save"
          negative-text="Cancel"
          @positive-click="handleSaveWorkspaceSettings"
          @negative-click="showWorkspaceSettingsModal = false"
        >
          <n-form
            ref="workspaceSettingsFormRef"
            :model="workspaceSettingsForm"
            :rules="workspaceSettingsRules"
          >
            <n-form-item
              v-if="workspaceActionType === 'rename'"
              label="Name"
              path="name"
            >
              <n-input
                v-model:value="workspaceSettingsForm.name"
                placeholder="Enter workspace name"
              />
            </n-form-item>
            <n-form-item
              v-if="workspaceActionType === 'update-description'"
              label="Description"
              path="description"
            >
              <n-input
                v-model:value="workspaceSettingsForm.description"
                type="textarea"
                placeholder="Enter workspace description"
              />
            </n-form-item>
          </n-form>
        </n-modal>
      </div>
    </div>

    <div class="sidebar-fixed-bottom">
      <div class="nailed-section">
        <n-button
          quaternary
          block
          class="utility-button invite-button"
          @click="showInviteMemberModal = true"
        >
          <template #icon>
            <n-icon size="20">
              <Icon icon="material-symbols:person-add-rounded" />
            </n-icon>
          </template>
          Invite Members
          <div class="invite-indicator pulse"></div>
        </n-button>

        <n-button
          quaternary
          block
          class="utility-button"
          @click="showTrashModal = true"
        >
          <div class="utility-content">
            <n-icon size="18">
              <Icon icon="material-symbols:delete-rounded" />
            </n-icon>
            <span>Trash</span>
          </div>
          <div v-if="trashedPages.length > 0" class="utility-badge">
            {{ trashedPages.length }}
          </div>
        </n-button>

        <n-button
          quaternary
          block
          class="utility-button"
          @click="showSettingsModal = true"
        >
          <div class="utility-content">
            <n-icon size="18">
              <Icon icon="material-symbols:settings-rounded" />
            </n-icon>
            <span>Settings</span>
          </div>
        </n-button>
      </div>
    </div>

    <!-- Add delete confirmation modal -->
    <n-modal
      v-model:show="showDeleteModal"
      title="Xác nhận xóa"
      preset="dialog"
      positive-text="Xóa"
      negative-text="Hủy"
      @positive-click="confirmDeletePage"
      @negative-click="showDeleteModal = false"
      @keydown.enter="confirmDeletePage"
      @keydown.esc="showDeleteModal = false"
    >
      <n-text>Bạn có chắc chắn muốn xóa trang này không?</n-text>
    </n-modal>

    <!-- Add update title modal -->
    <n-modal
      v-model:show="showUpdateTitleModal"
      title="Cập nhật tiêu đề"
      preset="dialog"
      positive-text="Cập nhật"
      negative-text="Hủy"
      @positive-click="confirmUpdateTitle"
      @negative-click="showUpdateTitleModal = false"
      @keydown.enter="confirmUpdateTitle"
      @keydown.esc="showUpdateTitleModal = false"
    >
      <n-form
        ref="updateTitleFormRef"
        :model="updateTitleForm"
        :rules="updateTitleRules"
      >
        <n-form-item label="Tiêu đề" path="title">
          <n-input
            v-model:value="updateTitleForm.title"
            placeholder="Nhập tiêu đề mới"
          />
        </n-form-item>
      </n-form>
    </n-modal>

    <!-- Settings Modal -->
    <n-modal
      v-model:show="showSettingsModal"
      title="Cài đặt"
      preset="dialog"
      positive-text="Lưu"
      negative-text="Hủy"
      @positive-click="handleSaveSettings"
      @negative-click="showSettingsModal = false"
    >
      <n-form
        ref="settingsFormRef"
        :model="settingsForm"
        :rules="settingsRules"
      >
        <n-form-item label="Tên người dùng" path="username">
          <n-input
            v-model:value="settingsForm.username"
            placeholder="Nhập tên người dùng"
          />
        </n-form-item>
        <n-form-item label="Email" path="email">
          <n-input
            v-model:value="settingsForm.email"
            placeholder="Nhập email"
          />
        </n-form-item>
      </n-form>
    </n-modal>

    <!-- Team Management Modals -->
    <n-modal
      v-model:show="showTeamModal"
      title="Create Team"
      preset="dialog"
      positive-text="Create"
      negative-text="Cancel"
      @positive-click="handleSaveTeam"
      @negative-click="showTeamModal = false"
    >
      <n-form ref="teamFormRef" :model="teamForm" :rules="teamFormRules">
        <n-form-item label="Team Name" path="name">
          <n-input
            v-model:value="teamForm.name"
            placeholder="Enter team name"
          />
        </n-form-item>
        <n-form-item label="Description" path="description">
          <n-input
            v-model:value="teamForm.description"
            type="textarea"
            placeholder="Enter team description"
          />
        </n-form-item>
      </n-form>
    </n-modal>

    <n-modal
      v-model:show="showMemberModal"
      title="Manage Team Members"
      preset="dialog"
      style="max-width: 600px"
    >
      <div class="member-management">
        <div class="member-list">
          <div
            v-for="member in teamMembers"
            :key="member.id"
            class="member-item"
          >
            <div class="member-info">
              <n-avatar
                size="small"
                :src="member.avatar"
                :fallback-src="member.avatar"
              >
                {{ member.username.charAt(0).toUpperCase() }}
              </n-avatar>
              <span class="member-name">{{ member.username }}</span>
              <n-tag
                size="small"
                :type="member.role === 'admin' ? 'success' : 'default'"
              >
                {{ member.role }}
              </n-tag>
            </div>
            <n-button
              quaternary
              size="small"
              @click="handleRemoveMember(member)"
              v-if="member.role !== 'admin'"
            >
              <n-icon size="16">
                <Icon icon="material-symbols:person-remove" />
              </n-icon>
            </n-button>
          </div>
        </div>
        <div class="add-member-section">
          <n-input-group>
            <n-input
              v-model:value="newMemberEmail"
              placeholder="Enter email address"
            />
            <n-button type="primary" @click="handleInviteMember">
              Invite
            </n-button>
          </n-input-group>
        </div>
      </div>
    </n-modal>

    <n-modal
      v-model:show="showTeamSettingsModal"
      title="Team Settings"
      preset="dialog"
      positive-text="Save"
      negative-text="Cancel"
      @positive-click="handleSaveTeamSettings"
      @negative-click="showTeamSettingsModal = false"
    >
      <n-form
        ref="teamSettingsFormRef"
        :model="teamSettingsForm"
        :rules="teamSettingsRules"
      >
        <n-form-item label="Team Name" path="name">
          <n-input
            v-model:value="teamSettingsForm.name"
            placeholder="Enter team name"
          />
        </n-form-item>
        <n-form-item label="Description" path="description">
          <n-input
            v-model:value="teamSettingsForm.description"
            type="textarea"
            placeholder="Enter team description"
          />
        </n-form-item>
        <n-form-item label="Team Visibility" path="visibility">
          <n-select
            v-model:value="teamSettingsForm.visibility"
            :options="[
              { label: 'Private', value: 'private' },
              { label: 'Public', value: 'public' },
            ]"
          />
        </n-form-item>
      </n-form>
    </n-modal>

    <n-modal
      v-model:show="showDeleteTeamModal"
      title="Delete Team"
      preset="dialog"
      positive-text="Delete"
      negative-text="Cancel"
      @positive-click="handleConfirmDeleteTeam"
      @negative-click="showDeleteTeamModal = false"
    >
      <p>
        Are you sure you want to delete this team? This action cannot be undone.
      </p>
    </n-modal>

    <!-- Add Trash Modal -->
    <n-modal
      v-model:show="showTrashModal"
      title="Trash"
      preset="card"
      style="max-width: 480px"
      :bordered="false"
    >
      <div class="trash-management">
        <div class="trash-search">
          <n-input
            v-model:value="trashSearchQuery"
            placeholder="Search pages in Trash"
            clearable
          >
            <template #prefix>
              <n-icon size="18">
                <Icon icon="material-symbols:search-rounded" />
              </n-icon>
            </template>
          </n-input>
        </div>
        <div v-if="filteredTrashPages.length > 0" class="trash-list">
          <div
            v-for="page in filteredTrashPages"
            :key="page.id"
            class="trash-item"
          >
            <div class="trash-info">
              <n-icon size="18" class="trash-icon">
                <Icon icon="material-symbols:description-rounded" />
              </n-icon>
              <span class="trash-title">{{ page.title || "Untitled" }}</span>
            </div>
            <div class="trash-actions">
              <n-button
                quaternary
                circle
                size="small"
                @click="restoreFromTrash(page)"
              >
                <n-icon size="16">
                  <Icon icon="material-symbols:restore-rounded" />
                </n-icon>
              </n-button>
              <n-button
                quaternary
                circle
                size="small"
                @click="deleteFromTrash(page)"
              >
                <n-icon size="16">
                  <Icon icon="material-symbols:delete-forever-rounded" />
                </n-icon>
              </n-button>
            </div>
          </div>
        </div>
        <n-empty v-else description="No items in trash" />
      </div>
    </n-modal>

    <!-- Add Invite Member Modal -->
    <n-modal
      v-model:show="showInviteMemberModal"
      title="Invite Member"
      preset="dialog"
      positive-text="Invite"
      negative-text="Cancel"
      @positive-click="handleInviteMemberSubmit"
      @negative-click="showInviteMemberModal = false"
    >
      <n-form
        ref="inviteMemberFormRef"
        :model="inviteMemberForm"
        :rules="inviteMemberRules"
      >
        <n-form-item label="Email" path="email">
          <n-input
            v-model:value="inviteMemberForm.email"
            placeholder="Enter email address"
          />
        </n-form-item>
        <n-form-item label="Team" path="teamId">
          <n-select
            v-model:value="inviteMemberForm.teamId"
            :options="teamOptions"
            placeholder="Select team"
            :loading="isLoadingTeams"
          />
        </n-form-item>
      </n-form>
    </n-modal>
  </div>
</template>
<script>
import {
  defineComponent,
  ref,
  onMounted,
  computed,
  watch,
  h,
  onBeforeUnmount,
} from "vue";
import { useRouter, useRoute } from "vue-router";
import {
  MailUnread,
  ShareSocial,
  Add,
  DocumentTextOutline,
  CompassOutline,
  EllipsisVertical,
  PlanetOutline,
  Home,
  PersonAdd,
} from "@vicons/ionicons5";
import api from "../../api/axios";
import { useWorkspaceStore } from "../../store/workspace";
import { usePageStore } from "../../store/page";
import { useBlockStore } from "../../store/block";
import { useUserStore } from "../../store/user";
import Cookies from "js-cookie";
import { Icon } from "@iconify/vue";
import { useRecentSelectionsStore } from "@/store/recentSelections";
import { useTeamStore } from "../../store/team";
import webSocketService from "../../utils/websocket";

export default defineComponent({
  name: "Sidebar",
  components: {
    MailUnread,
    ShareSocial,
    Add,
    Home,
    DocumentTextOutline,
    CompassOutline,
    EllipsisVertical,
    PlanetOutline,
    PersonAdd,
    Icon,
  },
  setup() {
    const router = useRouter();
    const route = useRoute();
    console.log("Sidebar setup initialized");
    const showModalWorkspace = ref(false);
    const showModelPage = ref(false);
    const showSettingsModal = ref(false);
    const formRefPage = ref(null);
    const formDataPage = ref({
      name: "",
      description: "",
    });
    const formRef = ref(null);
    const formData = ref({
      name: "",
      description: "",
    });
    const workspaces = ref([]);
    const pages = ref([]);
    const isLoading = ref(false);
    const isLoadingPages = ref(false);
    const workspaceStore = useWorkspaceStore();
    const pageStore = usePageStore();
    const userStore = useUserStore();
    const formRules = {
      name: {
        required: true,
        message: "Please input workspace name",
        trigger: "blur",
      },
      description: {
        required: true,
        message: "Please input workspace description",
        trigger: "blur",
      },
    };

    const expandedNames = ref([]);
    const showDeleteModal = ref(false);
    const pageToDelete = ref(null);
    const showUpdateTitleModal = ref(false);
    const pageToUpdate = ref(null);
    const updateTitleFormRef = ref(null);
    const updateTitleForm = ref({
      title: "",
    });
    const updateTitleRules = {
      title: {
        required: true,
        message: "Vui lòng nhập tiêu đề",
        trigger: "blur",
      },
    };

    const settingsFormRef = ref(null);
    const settingsForm = ref({
      username: Cookies.get("username") || "",
      email: "",
    });
    const settingsRules = {
      username: {
        required: true,
        message: "Vui lòng nhập tên người dùng",
        trigger: "blur",
      },
      email: {
        required: true,
        message: "Vui lòng nhập email",
        trigger: "blur",
      },
    };
    const teams = ref([]);
    const isLoadingTeams = ref(false);
    const teamStore = useTeamStore();

    const teamOptions = computed(() => {
      return teams.value.map((team) => ({
        label: team.workspaceName,
        value: team.workspaceId,
      }));
    });

    const teamActionOptions = [
      {
        label: "Manage Members",
        key: "manage-members",
      },
      {
        label: "Team Settings",
        key: "team-settings",
      },
      {
        label: "Delete Team",
        key: "delete-team",
      },
    ];

    const handleTeamAction = (key, team) => {
      switch (key) {
        case "manage-members":
          showMemberModal.value = true;
          selectedTeam.value = team;
          break;
        case "team-settings":
          showTeamSettingsModal.value = true;
          selectedTeam.value = team;
          break;
        case "delete-team":
          showDeleteTeamModal.value = true;
          selectedTeam.value = team;
          break;
      }
    };

    const pageOptions = [
      {
        label: "Rename",
        key: "rename",
      },
      {
        label: "Move to Trash",
        key: "trash",
      },
      {
        label: "Delete",
        key: "delete",
      },
    ];

    const fetchUser = async () => {
      try {
        const response = await api.get("/user/info");
        if (response.data) {
          settingsForm.value = {
            username: response.data.data.username || "",
            email: response.data.data.email || "",
          };
          console.log("User data fetched:", response.data);
        }
      } catch (error) {
        console.error("Error fetching user info:", error);
      }
    };
    const fetchWorkspaces = async () => {
      console.log("Starting fetchWorkspaces");
      try {
        isLoading.value = true;
        const response = await api.get("/workspace/list");
        workspaceStore.workspaces = response.data;
        console.log("Workspaces fetched:", response.data.data);
        // Ensure we're setting an array
        if (response && response.data) {
          // If response.data is the array
          if (Array.isArray(response.data)) {
            workspaces.value = response.data;
          }
          // If response.data contains the array in a property
          else if (response.data.data && Array.isArray(response.data.data)) {
            workspaces.value = response.data.data;
          }
          // If we need to transform the data
          else if (typeof response.data === "object") {
            workspaces.value = Object.values(response.data);
          }
        } else {
          workspaces.value = [];
        }
      } catch (error) {
        console.error("Error fetching workspaces:", error);
        workspaces.value = [];
      } finally {
        isLoading.value = false;
      }
    };

    const handleAddWorkspace = () => {
      showModalWorkspace.value = true;
    };

    const handleSaveWorkspace = async () => {
      try {
        await formRef.value?.validate();
        const response = await api.post("/workspace/create", {
          name: formData.value.name,
          description: formData.value.description,
        });
        console.log("Workspace created:", response);
        showModalWorkspace.value = false;
        formData.value = {
          name: "",
          description: "",
        };
        await fetchWorkspaces();
      } catch (error) {
        console.error("Error creating workspace:", error);
      }
    };
    const handleWorkspaceClick = async (workspaceId) => {
      workspaceStore.setSelectedSpace(workspaceId);
      teamStore.setSelectedTeam(null); // Clear team selection
      await fetchPagesList(workspaceId);
    };
    const fetchPagesList = async (workspaceId) => {
      try {
        isLoadingPages.value = true;
        const response = await api.get("/page/list/" + workspaceId);
        if (response && response.data) {
          pages.value = Array.isArray(response.data)
            ? response.data
            : response.data.data;
        }
        if (trashedPages != null) {
          const idTrash = new Set(trashedPages.value.map((page) => page.id));
          pages.value = pages.value.filter((page) => !idTrash.has(page.id));
        }
        pageStore.setPages(pages.value);
      } catch (error) {
        console.error("Error fetching pages:", error);
        pages.value = [];
      } finally {
        isLoadingPages.value = false;
      }
    };
    const handleAddPages = async (parentId) => {
      if (!parentId) {
        return;
      }

      try {
        const newPage = pageStore.createNewPage(parentId);
        const response = await api.post("/page/create", {
          title: newPage.title,
          workspaceId: parentId,
        });

        // Refresh the pages list based on whether it's a team or workspace
        if (teamStore.selectedTeam === parentId) {
          await fetchTeamPages(parentId);
        } else {
          await fetchPagesList(parentId);
        }
      } catch (error) {
        console.error("Error creating page:", error);
      }
    };
    const handlePageClick = async (pageId) => {
      console.log("Page clicked with ID:", pageId);
      pageStore.setSelectedPage(pageId);
      pageStore.setSelectedTitle(
        pageStore.pages.find((page) => page.id === pageId)?.title
      );
      console.log("Selected title:", pageStore.selectedTitle);
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

      pageStore.setCurrentContent({
        time: new Date().getTime(),
        version: "2.30.0",
        blocks: blocks,
      });
      console.log("Store content after update:", pageStore.currentContent);

      // Navigate to /note/edit route
      useRecentSelectionsStore().addSelection({
        id: pageId,
        title: pageStore.selectedTitle,
      });
      router.push("/note/edit");
    };
    const handleUpdatePageTitle = (page) => {
      console.log("Opening update modal for page:", page);
      pageToUpdate.value = page;
      updateTitleForm.value.title = page.title;
      showUpdateTitleModal.value = true;
    };

    const confirmUpdateTitle = async () => {
      if (!pageToUpdate.value) {
        console.error("No page selected for update");
        return;
      }
      try {
        await updateTitleFormRef.value?.validate();
        console.log(
          "Updating page:",
          pageToUpdate.value.id,
          "with title:",
          updateTitleForm.value.title
        );
        const response = await api.put(`/page/${pageToUpdate.value.id}`, {
          title: updateTitleForm.value.title,
        });
        console.log("Response from update page:", response);
        await fetchPagesList();
        showUpdateTitleModal.value = false;
        pageToUpdate.value = null;
        updateTitleForm.value.title = "";
      } catch (error) {
        console.error("Error updating page title:", error);
      }
    };

    const handleDeletePage = (pageId) => {
      pageToDelete.value = pageId;
      showDeleteModal.value = true;
    };

    const confirmDeletePage = async () => {
      try {
        await api.delete(`/page/${pageToDelete.value}`);
        await fetchPagesList();
        if (pageStore.selectedPage === pageToDelete.value) {
          pageStore.setSelectedPage(null);
        }
        showDeleteModal.value = false;
        pageToDelete.value = null;
      } catch (error) {
        console.error("Error deleting page:", error);
      }
    };

    const handleHomeClick = () => {
      // Clear selections
      workspaceStore.setSelectedSpace(null);
      pageStore.setSelectedPage(null);
      pageStore.setSelectedTitle(null);
      pageStore.setCurrentContent(null);
      router.push("/note");
    };

    const handleSaveSettings = async () => {
      try {
        await settingsFormRef.value?.validate();
        // Here you would typically make an API call to save the settings
        console.log("Saving settings:", settingsForm.value);
        showSettingsModal.value = false;
      } catch (error) {
        console.error("Error saving settings:", error);
      }
    };

    const renderIcon = (icon) => {
      return () => h(Icon, { icon });
    };

    const options = [
      {
        label: "Settings",
        key: "settings",
        icon: renderIcon("material-symbols:settings-rounded"),
      },
      {
        type: "divider",
        key: "divider",
      },
      {
        label: "Log out",
        key: "logout",
        icon: renderIcon("material-symbols:logout-rounded"),
      },
    ];

    const userEmail = ref(Cookies.get("email") || "");
    const inboxCounter = ref(0);
    const showInboxModal = ref(false);
    const inboxMessages = ref([]);

    const handleOptionClick = async (key) => {
      if (key === "settings") {
        showSettingsModal.value = true;
        await fetchUser();
      } else if (key === "logout") {
        Cookies.remove("username");
        Cookies.remove("email");
        Cookies.remove("token"); // Remove any other auth-related cookies if they exist
        router.push("/login");
      }
    };

    // Watch for route changes
    watch(
      () => route.path,
      (newPath) => {
        if (newPath === "/note") {
          // Clear selections when on home route
          workspaceStore.setSelectedSpace(null);
          pageStore.setSelectedPage(null);
          pageStore.setSelectedTitle(null);
          pageStore.setCurrentContent(null);
        }
      }
    );

    const fetchTeams = async () => {
      try {
        isLoadingTeams.value = true;
        const response = await api.get("/workspace/teamspace");
        if (response && response.data) {
          teams.value = Array.isArray(response.data)
            ? response.data
            : response.data.data;
        }
      } catch (error) {
        console.error("Error fetching teamspace:", error);
        teams.value = [];
      } finally {
        isLoadingTeams.value = false;
      }
    };

    const handleAddTeam = () => {
      showTeamModal.value = true;
    };

    const handleTeamClick = async (teamId) => {
      console.log("Team clicked with ID:", teamId);
      teamStore.setSelectedTeam(teamId);
      workspaceStore.setSelectedSpace(null); // Clear workspace selection
      await fetchTeamPages(teamId);
    };
    const fetchInboxMessage = async () => {
      try {
        const response = await api.get("/workspace/member/invitations");
        inboxMessages.value = response.data.data;
        if (response.data.data != null) {
          inboxCounter.value = inboxMessages.value.filter(
            (item) => item.status === "pending"
          ).length;
        }
      } catch (error) {
        console.error("Error fetching inbox messages:", error);
      }
    };

    const handleInvitationResponse = async (invitationId, action) => {
      try {
        if (action == "accept") {
          const response = await api.post(
            `/workspace/member/accept/invitation`,
            {
              id: invitationId,
            }
          );
          if (response.data.code === 200) {
            // Remove the invitation from the list
            inboxMessages.value = inboxMessages.value.filter(
              (msg) => msg.id !== invitationId
            );
          }
        } else if (action == "reject") {
          const response = await api.post(
            `/workspace/member/invitation/update-status`,
            {
              id: invitationId,
              status: "rejected",
            }
          );
          if (response.data.code === 200) {
            // Remove the invitation from the list
            inboxMessages.value = inboxMessages.value.filter(
              (msg) => msg.id !== invitationId
            );
            // Update counter
            inboxCounter.value = inboxMessages.value.filter(
              (item) => item.status === "pending"
            ).length;
          }
        }
      } catch (error) {
        console.error(`Error ${action}ing invitation:`, error);
      }
    };
    // Team Management State
    const showTeamModal = ref(false);
    const showMemberModal = ref(false);
    const showTeamSettingsModal = ref(false);
    const showDeleteTeamModal = ref(false);
    const selectedTeam = ref(null);
    const teamMembers = ref([]);
    const newMemberEmail = ref("");
    const teamFormRef = ref(null);

    const teamForm = ref({
      name: "",
      description: "",
    });

    const teamFormRules = {
      name: {
        required: true,
        message: "Please enter team name",
        trigger: "blur",
      },
      description: {
        required: true,
        message: "Please enter team description",
        trigger: "blur",
      },
    };

    const teamSettingsForm = ref({
      name: "",
      description: "",
      visibility: "private",
    });

    const teamSettingsRules = {
      name: {
        required: true,
        message: "Please enter team name",
        trigger: "blur",
      },
    };

    const handleSaveTeam = async () => {
      try {
        await teamFormRef.value?.validate();
        const response = await api.post("/workspace/create-team", {
          name: teamForm.value.name,
          description: teamForm.value.description,
        });
        if (response.data) {
          teamStore.addTeam(response.data);
          showTeamModal.value = false;
          teamForm.value = { name: "", description: "" };
        }
      } catch (error) {
        console.error("Error creating team:", error);
      }
    };

    const handleInviteMember = async () => {
      if (!selectedTeam.value || !newMemberEmail.value) return;

      try {
        const response = await api.post(
          `/team/${selectedTeam.value.id}/invite`,
          {
            email: newMemberEmail.value,
          }
        );
        if (response.data) {
          teamMembers.value.push(response.data);
          newMemberEmail.value = "";
        }
      } catch (error) {
        console.error("Error inviting member:", error);
      }
    };

    const handleRemoveMember = async (member) => {
      if (!selectedTeam.value) return;

      try {
        await api.delete(`/team/${selectedTeam.value.id}/member/${member.id}`);
        teamMembers.value = teamMembers.value.filter((m) => m.id !== member.id);
      } catch (error) {
        console.error("Error removing member:", error);
      }
    };

    const handleSaveTeamSettings = async () => {
      if (!selectedTeam.value) return;

      try {
        // await teamSettingsFormRef.value?.validate();
        console.log(selectedTeam.value.workspaceId);
        const response = await api.post(
          `/workspace/update/${selectedTeam.value.workspaceId}`,
          teamSettingsForm.value
        );
        if (response.data) {
          teamStore.updateTeam(selectedTeam.value.id, response.data);
          showTeamSettingsModal.value = false;
        }
      } catch (error) {
        console.error("Error updating team settings:", error);
      }
    };

    const handleConfirmDeleteTeam = async () => {
      if (!selectedTeam.value) return;

      try {
        await api.delete(`/team/${selectedTeam.value.id}`);
        teamStore.removeTeam(selectedTeam.value.id);
        showDeleteTeamModal.value = false;
        selectedTeam.value = null;
      } catch (error) {
        console.error("Error deleting team:", error);
      }
    };

    const showTrashModal = ref(false);
    const trashedPages = ref([]);

    const moveToTrash = (page) => {
      // Add to trash
      trashedPages.value.push({
        id: page.id,
        title: page.title,
        workspaceId: workspaceStore.selectedSpace,
        trashedAt: new Date().toISOString(),
      });

      // Save to localStorage
      localStorage.setItem("trashedPages", JSON.stringify(trashedPages.value));

      // Remove from current pages list
      pages.value = pages.value.filter((p) => p.id !== page.id);
    };

    const restoreFromTrash = async (page) => {
      try {
        // Remove from trash
        trashedPages.value = trashedPages.value.filter((p) => p.id !== page.id);
        localStorage.setItem(
          "trashedPages",
          JSON.stringify(trashedPages.value)
        );

        // Refresh pages list
        await fetchPagesList(page.workspaceId);
      } catch (error) {
        console.error("Error restoring page:", error);
      }
    };

    const deleteFromTrash = async (page) => {
      try {
        // Remove from trash
        trashedPages.value = trashedPages.value.filter((p) => p.id !== page.id);
        localStorage.setItem(
          "trashedPages",
          JSON.stringify(trashedPages.value)
        );

        // Delete from backend
        await api.delete(`/page/${page.id}`);
      } catch (error) {
        console.error("Error deleting page:", error);
      }
    };

    const handlePageAction = (key, page) => {
      switch (key) {
        case "rename":
          handleUpdatePageTitle(page);
          break;
        case "trash":
          moveToTrash(page);
          break;
        case "delete":
          handleDeletePage(page.id);
          break;
      }
    };

    // Initialize data
    onMounted(async () => {
      console.log("Sidebar component mounted");
      // Check if username cookie exists
      if (!Cookies.get("username")) {
        router.push("/login");
        return;
      }
      await fetchWorkspaces();
      await fetchTeams();
      await fetchInboxMessage();
      const storedTrash = localStorage.getItem("trashedPages");
      if (storedTrash) {
        trashedPages.value = JSON.parse(storedTrash);
      }
      webSocketService.connect();
      // Subscribe to notifications
      webSocketService.on("notification", (payload) => {
        console.log("Received notification from server", payload);
        // Refresh inbox messages when receiving a notification
        fetchInboxMessage();
      });
    });
    onBeforeUnmount(() => {
      webSocketService.disconnect();
    });
    const trashSearchQuery = ref("");
    const filteredTrashPages = computed(() => {
      if (!trashSearchQuery.value) return trashedPages.value;
      const query = trashSearchQuery.value.toLowerCase();
      return trashedPages.value.filter((page) =>
        page.title.toLowerCase().includes(query)
      );
    });

    const showInviteMemberModal = ref(false);
    const inviteMemberFormRef = ref(null);
    const inviteMemberForm = ref({
      email: "",
      teamId: null,
    });
    const inviteMemberRules = {
      email: {
        required: true,
        message: "Please enter a valid email",
        trigger: "blur",
      },
      teamId: {
        required: true,
        message: "Please select a team",
        trigger: "change",
      },
    };

    const handleInviteMemberSubmit = async () => {
      try {
        //    await inviteMemberFormRef.value?.validate();
        console.log(inviteMemberForm.value);
        const response = await api.post("/workspace/member/invitation", {
          inviteeEmail: inviteMemberForm.value.email,
          objectId: inviteMemberForm.value.teamId,
          role: "teamspace_member",
        });
        console.log(response);
        if (response.data.code === 200) {
          inviteMemberForm.value = {
            email: "",
            teamId: null,
          };
          showInviteMemberModal.value = false;
        }
      } catch (error) {
        console.error("Error inviting member:", error);
      }
    };

    const teamPages = ref([]);
    const isLoadingTeamPages = ref(false);

    const fetchTeamPages = async (teamId) => {
      try {
        isLoadingTeamPages.value = true;
        const response = await api.get(`/page/list/${teamId}`);
        console.log("Team pages response:", response.data.data);
        if (response && response.data) {
          teamPages.value = response.data.data;
        }
        pageStore.setPages(teamPages.value);
      } catch (error) {
        console.error("Error fetching team pages:", error);
        teamPages.value = [];
      } finally {
        isLoadingTeamPages.value = false;
      }
    };

    // Workspace Management State
    const showWorkspaceSettingsModal = ref(false);
    const selectedWorkspace = ref(null);
    const workspaceSettingsFormRef = ref(null);
    const workspaceActionType = ref("");
    const workspaceModalTitle = computed(() => {
      return workspaceActionType.value === "rename"
        ? "Rename Workspace"
        : "Update Workspace Description";
    });

    const workspaceSettingsForm = ref({
      name: "",
      description: "",
    });

    const workspaceSettingsRules = {
      name: {
        required: true,
        message: "Please enter workspace name",
        trigger: "blur",
      },
      description: {
        required: true,
        message: "Please enter workspace description",
        trigger: "blur",
      },
    };

    const workspaceActionOptions = [
      {
        label: "Rename",
        key: "rename",
      },
      {
        label: "Update Description",
        key: "update-description",
      },
    ];

    const handleWorkspaceAction = async (key, workspace) => {
      selectedWorkspace.value = workspace;
      workspaceActionType.value = key;
      workspaceSettingsForm.value = {
        name: workspace.name || "",
        description: workspace.description || "",
      };
      showWorkspaceSettingsModal.value = true;
    };

    const handleSaveWorkspaceSettings = async () => {
      if (!selectedWorkspace.value) return;

      try {
        await workspaceSettingsFormRef.value?.validate();

        const updateData =
          workspaceActionType.value === "rename"
            ? { name: workspaceSettingsForm.value.name }
            : { description: workspaceSettingsForm.value.description };

        const response = await api.post(
          `/workspace/update/${selectedWorkspace.value.id}`,
          updateData
        );

        if (response.data) {
          // Update the workspace in the list
          const index = workspaces.value.findIndex(
            (w) => w.id === selectedWorkspace.value.id
          );
          if (index !== -1) {
            workspaces.value[index] = {
              ...workspaces.value[index],
              ...updateData,
            };
          }
          showWorkspaceSettingsModal.value = false;
          selectedWorkspace.value = null;
        }
      } catch (error) {
        console.error("Error updating workspace settings:", error);
      }
    };

    return {
      showModalWorkspace,
      formRef,
      formData,
      formRules,
      workspaces,
      pages,
      isLoading,
      isLoadingPages,
      handleAddWorkspace,
      handleSaveWorkspace,
      handleWorkspaceClick,
      handleAddPages,
      handlePageClick,
      handleUpdatePageTitle,
      handleDeletePage,
      confirmDeletePage,
      handleHomeClick,
      options,
      handleOptionClick,
      workspaceStore,
      pageStore,
      userStore,
      Cookies,
      expandedNames,
      showDeleteModal,
      showUpdateTitleModal,
      updateTitleFormRef,
      updateTitleForm,
      updateTitleRules,
      confirmUpdateTitle,
      showSettingsModal,
      settingsFormRef,
      settingsForm,
      settingsRules,
      handleSaveSettings,
      teams,
      isLoadingTeams,
      teamStore,
      teamOptions,
      teamActionOptions,
      handleAddTeam,
      handleTeamClick,
      handleTeamAction,
      showTeamModal,
      showMemberModal,
      showTeamSettingsModal,
      showDeleteTeamModal,
      selectedTeam,
      teamMembers,
      newMemberEmail,
      teamForm,
      teamFormRef,
      teamFormRules,
      teamSettingsForm,
      teamSettingsRules,
      handleSaveTeam,
      handleInviteMember,
      handleRemoveMember,
      handleSaveTeamSettings,
      handleConfirmDeleteTeam,
      pageOptions,
      handlePageAction,
      showTrashModal,
      trashedPages,
      moveToTrash,
      restoreFromTrash,
      deleteFromTrash,
      trashSearchQuery,
      filteredTrashPages,
      userEmail,
      showInviteMemberModal,
      inviteMemberFormRef,
      inviteMemberForm,
      inviteMemberRules,
      handleInviteMemberSubmit,
      inboxCounter,
      showInboxModal,
      inboxMessages,
      handleInvitationResponse,
      teamPages,
      isLoadingTeamPages,
      showWorkspaceSettingsModal,
      workspaceSettingsFormRef,
      workspaceSettingsForm,
      workspaceSettingsRules,
      workspaceActionOptions,
      workspaceActionType,
      workspaceModalTitle,
      handleWorkspaceAction,
      handleSaveWorkspaceSettings,
    };
  },
});
</script>

<style scoped>
.sidebar-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.sidebar-fixed-top {
  flex-shrink: 0;
}

.sidebar-scrollable {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  min-height: 0;
  margin-bottom: 8px;
  /* Notion-like scrollbar */
  scrollbar-width: thin;
  scrollbar-color: rgba(55, 53, 47, 0.1) transparent;
}

.sidebar-scrollable::-webkit-scrollbar {
  width: 7px;
}

.sidebar-scrollable::-webkit-scrollbar-track {
  background-color: transparent;
  border-radius: 3px;
}

.sidebar-scrollable::-webkit-scrollbar-thumb {
  background-color: transparent;
  border-radius: 3px;
  transition: background-color 0.2s ease;
}

.sidebar-scrollable:hover::-webkit-scrollbar-thumb {
  background-color: rgba(55, 53, 47, 0.2);
}

.sidebar-scrollable::-webkit-scrollbar-thumb:hover {
  background-color: rgba(55, 53, 47, 0.4);
}

.sidebar-fixed-bottom {
  flex-shrink: 0;
  position: relative;
}

/* Base styles */
:deep(*) {
  font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
    Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
  font-weight: 400;
}

/* Account section */
.account {
  padding: 12px;
  margin-bottom: 12px;
}

.account-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 8px;
  border-radius: 8px;
  transition: background-color 0.2s ease;
}

.account-wrapper:hover {
  background: rgba(0, 0, 0, 0.04);
}

.avatar-container {
  position: relative;
}

.account-info {
  flex: 1;
  min-width: 0;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: var(--n-text-color);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-email {
  font-size: 12px;
  color: var(--n-text-color-3);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

:deep(.n-dropdown-menu) {
  min-width: 200px !important;
}

:deep(.n-dropdown-option) {
  padding: 8px 12px !important;
}

:deep(.n-dropdown-option-body) {
  display: flex;
  align-items: center;
  gap: 8px;
}

:deep(.n-dropdown-option-body__prefix) {
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.n-dropdown-divider) {
  margin: 4px 0 !important;
}

/* Navigation sections */
.home,
.inbox {
  padding: 4px 12px;
  margin-bottom: 8px;
}

.home .n-button,
.inbox .n-button {
  border-radius: 12px;
  padding: 10px 16px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.5);
  border: 1px solid transparent;
}

.home .n-button:hover,
.inbox .n-button:hover {
  background: rgba(255, 255, 255, 0.9);
  transform: translateX(4px);
  border-color: rgba(32, 128, 240, 0.2);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* Workspace and Pages sections */
.workspace,
.pages {
  padding: 0 12px;
}

:deep(.n-collapse) {
  width: 100%;
  background: transparent;
  border: none;
}

:deep(.n-collapse-item__header) {
  padding: 10px;
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.5);
}

:deep(.n-collapse-item__header:hover) {
  background: rgba(255, 255, 255, 0.9);
}

.collapse-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  font-weight: 600;
  letter-spacing: -0.01em;
}

.collapse-header .n-button {
  width: 28px;
  height: 28px;
  min-width: 28px;
  padding: 0;
  border-radius: 8px;
  opacity: 0.7;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.collapse-header .n-button:hover {
  opacity: 1;
  background: rgba(32, 128, 240, 0.1);
  color: #2080f0;
  transform: rotate(90deg);
}

/* Workspace and Page items */
.workspace-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 6px 4px;
}

.workspace-item,
.page-item {
  border-radius: 10px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.workspace-item .n-button,
.page-item .n-button {
  text-align: left;
  padding: 10px 14px;
  border-radius: 10px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid transparent;
  position: relative;
  overflow: hidden;
  justify-content: flex-start;
  width: 100%;
}

.workspace-item .n-button:hover,
.page-item .n-button:hover {
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.9),
    rgba(255, 255, 255, 0.8)
  ) !important;
  transform: translateX(4px);
}

.workspace-item .n-button::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: linear-gradient(to bottom, #2080f0, #00e676);
  opacity: 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 3px;
}

.workspace-item .n-button:hover::before {
  opacity: 0.5;
}

.workspace-selected::before {
  opacity: 1 !important;
}

.workspace-meta {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 8px;
}

.page-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  padding: 2px 8px;
  z-index: 1; /* Add z-index to ensure proper stacking */
}

.page-item .n-button {
  text-align: left;
  padding: 6px 8px;
  height: 32px;
  font-size: 13px;
}

.page-title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 32px; /* Increase margin to accommodate the button */
}

.page-content {
  flex: 1;
  min-width: 0;
}

.page-actions {
  opacity: 0;
  flex-shrink: 0;
  width: 28px;
  height: 28px;
  padding: 0;
  margin-right: 4px;
  border-radius: 8px !important;
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid transparent;
  z-index: 2;
}

.page-actions-selected,
.page-item:hover .page-actions {
  opacity: 0.9;
}

.page-actions:hover {
  opacity: 1 !important;
  background: rgba(255, 255, 255, 0.95) !important;
  border-color: rgba(32, 128, 240, 0.2);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.page-icon,
.workspace-icon {
  margin-right: 10px;
  opacity: 0.8;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.workspace-selected .workspace-icon,
.page-selected .page-icon {
  opacity: 1;
  color: #2080f0;
}

/* Bottom section */
.nailed-section {
  width: 100%;
  padding: 14px;
  background: linear-gradient(
    to bottom,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0.8) 20%,
    rgba(255, 255, 255, 0.95) 100%
  );
  backdrop-filter: blur(6px);
  display: flex;
  flex-direction: column;
  gap: 4px;
  border-top: 1px solid rgba(55, 53, 47, 0.1);
}

.utility-button {
  border-radius: 12px;
  padding: 10px 16px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.5) !important;
  border: 1px solid rgba(0, 0, 0, 0.05);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 8px;
  font-size: 14px;
  text-align: left;
}

.utility-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.utility-button:hover {
  background: rgba(255, 255, 255, 0.9) !important;
  transform: translateX(4px);
  border-color: rgba(32, 128, 240, 0.2);
  box-shadow: 0 4px 12px rgba(32, 128, 240, 0.1);
}

.invite-button {
  background: linear-gradient(
    135deg,
    rgba(32, 128, 240, 0.1),
    rgba(0, 230, 118, 0.1)
  ) !important;
  border: 1px solid rgba(32, 128, 240, 0.2);
  padding-right: 32px; /* Add space for the indicator */
}

.utility-badge {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #ff4081;
  color: white;
  min-width: 18px;
  height: 18px;
  border-radius: 9px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 6px;
  border: 2px solid white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Modals */
:deep(.n-modal) {
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.08);
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.95),
    rgba(255, 255, 255, 0.98)
  ) !important;
  backdrop-filter: blur(12px);
}

:deep(.n-modal-header) {
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
  padding: 20px 24px;
}

:deep(.n-modal-content) {
  padding: 24px;
}

:deep(.n-input) {
  border-radius: 10px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.n-input:hover) {
  border-color: rgba(32, 128, 240, 0.3);
}

:deep(.n-input:focus) {
  border-color: #2080f0;
  box-shadow: 0 0 0 2px rgba(32, 128, 240, 0.2);
}

/* New styles for enhanced visuals */
.header-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-icon {
  color: #2080f0;
  opacity: 0.8;
}

/* Team styles */
.team-icon {
  color: #7c4dff;
  opacity: 0.8;
}

.team-item {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 2px 8px;
}

.team-header {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
}

.team-header .n-button {
  text-align: left;
  padding: 10px 14px;
  border-radius: 10px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid transparent;
  position: relative;
  overflow: hidden;
  justify-content: flex-start;
  width: 100%;
  display: flex;
  align-items: center;
}

.team-name {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 24px;
}

.team-header .n-button:hover {
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.9),
    rgba(255, 255, 255, 0.8)
  ) !important;
  transform: translateX(4px);
}

.team-header .n-button::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: linear-gradient(to bottom, #7c4dff, #2196f3);
  opacity: 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 3px;
}

.team-header .n-button:hover::before {
  opacity: 0.5;
}

.team-selected::before {
  opacity: 1 !important;
}

.team-meta {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 8px;
}

.team-indicator {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: linear-gradient(135deg, #7c4dff, #2196f3);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.team-selected .team-indicator {
  opacity: 1;
}

/* Common styles for action buttons (three dots) */
.action-button {
  opacity: 0;
  flex-shrink: 0;
  width: 28px;
  height: 28px;
  min-width: 28px;
  padding: 4px;
  border-radius: 4px !important;
  position: absolute;
  right: 4px;
  top: 50%;
  transform: translateY(-50%);
  transition: all 0.15s ease;
  background: transparent !important;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2;
  border: none !important;
}

.action-button:hover {
  background: rgba(0, 0, 0, 0.06) !important;
}

.action-button:active {
  background: rgba(0, 0, 0, 0.09) !important;
}

.action-button .n-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  color: rgba(0, 0, 0, 0.45);
  transition: color 0.15s ease;
}

.action-button:hover .n-icon {
  color: rgba(0, 0, 0, 0.85);
}

/* Team specific styles */
.team-actions {
  composes: action-button;
  right: 8px;
}

.team-header:hover .team-actions {
  opacity: 1;
}

/* Nested pages under team */
.team-item .nested-pages {
  margin-left: 24px;
  margin-top: 4px;
  padding-left: 12px;
  border-left: 1px solid rgba(55, 53, 47, 0.1);
  width: calc(100% - 24px);
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.team-item .page-item {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  min-height: 32px;
}

.team-item .page-item .n-button {
  text-align: left;
  padding: 6px 8px;
  height: 32px;
  font-size: 13px;
  width: 100%;
  display: flex;
  align-items: center;
}

.team-item .page-title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 28px;
}

/* Page specific styles */
.page-actions {
  composes: action-button;
}

.page-item:hover .page-actions {
  opacity: 1;
}

/* Workspace page styles */
.workspace-item .page-actions {
  composes: action-button;
}

.workspace-item .page-item:hover .page-actions {
  opacity: 1;
}

.workspace-item .page-title {
  margin-right: 28px;
}

/* Member management styles */
.member-management {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.member-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 300px;
  overflow-y: auto;
  padding: 4px;
}

.member-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.member-item:hover {
  background: rgba(255, 255, 255, 0.8);
  transform: translateX(4px);
  border-color: rgba(124, 77, 255, 0.2);
}

.member-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.member-name {
  font-weight: 500;
  color: var(--n-text-color);
}

.add-member-section {
  padding-top: 16px;
  border-top: 1px solid rgba(0, 0, 0, 0.08);
}

:deep(.n-input-group) {
  display: flex;
  gap: 8px;
}

:deep(.n-input-group .n-input) {
  flex: 1;
}

:deep(.n-tag) {
  text-transform: capitalize;
}

/* Navigation Buttons */
.nav-button {
  position: relative;
  overflow: hidden;
}

.nav-button.primary {
  background: linear-gradient(
    135deg,
    rgba(32, 128, 240, 0.1),
    rgba(32, 128, 240, 0.05)
  ) !important;
}

.nav-button.secondary {
  background: linear-gradient(
    135deg,
    rgba(0, 230, 118, 0.1),
    rgba(0, 230, 118, 0.05)
  ) !important;
}

.inbox-counter {
  background: rgba(255, 64, 129, 0.1);
  color: #ff4081;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  margin-left: auto;
}

.invite-indicator {
  position: absolute;
  right: 12px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #00e676;
}

.invite-indicator.pulse {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(0, 230, 118, 0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(0, 230, 118, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(0, 230, 118, 0);
  }
}

/* Trash Management Styles */
.trash-management {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.trash-search {
  padding: 0 0 8px 0;
}

.trash-search :deep(.n-input) {
  background: rgba(255, 255, 255, 0.5);
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.trash-search :deep(.n-input:hover),
.trash-search :deep(.n-input:focus) {
  border-color: #2080f0;
  background: white;
}

.trash-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
  max-height: 400px;
  overflow-y: auto;
  padding: 0 4px;
}

.trash-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  background: transparent;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.trash-item:hover {
  background: rgba(0, 0, 0, 0.04);
}

.trash-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--n-text-color);
}

.trash-title {
  font-size: 14px;
}

.trash-icon {
  opacity: 0.6;
}

.trash-actions {
  display: flex;
  gap: 4px;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.trash-item:hover .trash-actions {
  opacity: 1;
}

.trash-actions .n-button {
  width: 24px;
  height: 24px;
  padding: 0;
  color: var(--n-text-color-3);
}

.trash-actions .n-button:hover {
  color: #2080f0;
  background: rgba(32, 128, 240, 0.1);
}

/* Inbox Modal Styles */
.inbox-management {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.inbox-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 400px;
  overflow-y: auto;
  padding: 4px;
}

.inbox-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.inbox-item:hover {
  background: rgba(255, 255, 255, 0.8);
  transform: translateX(4px);
  border-color: rgba(32, 128, 240, 0.2);
}

.inbox-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
}

.inbox-message {
  font-size: 14px;
  color: var(--n-text-color);
}

.inbox-icon {
  color: #2080f0;
  opacity: 0.8;
}

.inbox-actions {
  display: flex;
  gap: 8px;
}

.inbox-actions .n-button {
  min-width: 80px;
}

/* Add these utility classes */
.account,
.quick-actions,
.home,
.inbox {
  background: #ffffff;
}

.nested-pages {
  margin-left: 24px;
  margin-top: 4px;
  border-left: 1px solid rgba(55, 53, 47, 0.1);
  padding-left: 12px;
}

.add-page-button {
  margin-top: 8px;
  font-size: 13px;
  color: rgba(55, 53, 47, 0.65);
  height: 32px;
  padding: 6px 8px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.add-page-button:hover {
  background: rgba(55, 53, 47, 0.08) !important;
  color: rgba(55, 53, 47, 0.9);
}

.page-item {
  margin-top: 2px;
}

.page-item:first-child {
  margin-top: 8px;
}

/* Workspace specific styles */
.workspace-header {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  padding-right: 36px; /* Add padding to accommodate the button */
}

.workspace-header .n-button {
  overflow: visible; /* Allow the three-dot button to be visible */
}

.workspace-actions {
  flex-shrink: 0;
  width: 28px;
  height: 28px;
  padding: 0;
  border-radius: 8px !important;
  position: absolute;
  right: 4px; /* Reduced from 8px */
  top: 50%;
  transform: translateY(-50%);
  background: transparent;
  border: 1px solid transparent;
  z-index: 2;
}

/* Update workspace meta to ensure it doesn't overlap with the button */
.workspace-meta {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 8px;
  margin-right: 4px; /* Add margin to prevent overlap with button */
}

.workspace-actions:hover {
  background: rgba(0, 0, 0, 0.06) !important;
}

.workspace-actions:active {
  background: rgba(0, 0, 0, 0.09) !important;
}

.workspace-actions .n-icon {
  color: rgba(0, 0, 0, 0.45);
}

.workspace-actions:hover .n-icon {
  color: rgba(0, 0, 0, 0.85);
}

.workspace-actions-selected {
  opacity: 1;
}

/* Add these styles at the end of the <style> section */
.workspace-description-popover {
  max-width: 300px;
  padding: 4px;
}

.popover-title {
  font-size: 12px;
  font-weight: 600;
  color: var(--n-text-color-2);
  margin-bottom: 4px;
}

.popover-content {
  font-size: 13px;
  color: var(--n-text-color);
  line-height: 1.5;
  white-space: pre-wrap;
  word-break: break-word;
}

/* Add these styles in the <style> section, near the workspace-description-popover styles */
.team-description-popover {
  max-width: 300px;
  padding: 4px;
}

.team-description-popover .popover-content {
  font-size: 13px;
  line-height: 1.5;
}
</style>
