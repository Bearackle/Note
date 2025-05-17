<template>
  <div class="account">
    <n-avatar
      :style="{
        color: 'yellow',
        backgroundColor: 'red',
      }"
      >M</n-avatar
    >
    <n-dropdown trigger="hover" :options="options">
      <n-button>Tài khoản</n-button>
    </n-dropdown>
  </div>
  <div class="inbox">
    <n-button quaternary>
      <n-icon size="24">
        <MailUnread />
      </n-icon>
      <n-text>Inbox</n-text>
    </n-button>
  </div>
  <div class="workspace">
    <n-collapse>
      <n-collapse-item name="1">
        <template #header>
          <div class="collapse-header">
            <span>Workspace</span>
            <n-button quaternary size="small" @click.stop="handleAddWorkspace">
              <n-icon size="16">
                <Add />
              </n-icon>
            </n-button>
          </div>
        </template>
        <n-spin :show="isLoading">
          <div>
            <!-- Workspace list -->
            <div
              v-if="Array.isArray(workspaces) && workspaces.length > 0"
              class="workspace-list"
            >
              <div
                v-for="workspace in workspaces"
                :key="workspace.id"
                class="workspace-item"
              >
                <n-button
                  text
                  block
                  @click="handleWorkspaceClick(workspace.id)"
                  :class="{
                    'workspace-selected':
                      workspaceStore.selectedSpace === workspace.id,
                  }"
                >
                  <n-icon size="16" class="workspace-icon">
                    <CompassOutline />
                  </n-icon>
                  {{ workspace.name }}
                </n-button>
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
  </div>
  <div class="pages">
    <n-collapse
      :default-expanded-names="expandedNames"
      v-model:expanded-names="expandedNames"
    >
      <n-collapse-item name="1">
        <template #header>
          <div class="collapse-header">
            <span>Pages</span>
            <n-button quaternary size="small" @click.stop="handleAddPages">
              <n-icon size="16">
                <Add />
              </n-icon>
            </n-button>
          </div>
        </template>
        <n-spin :show="isLoadingPages">
          <div>
            <div
              v-if="Array.isArray(pages) && pages.length > 0"
              class="workspace-list"
            >
              <div v-for="page in pages" :key="page.id" class="page-item">
                <n-button
                  text
                  block
                  @click="handlePageClick(page.id)"
                  :class="{
                    'page-selected': pageStore.selectedPage === page.id,
                  }"
                  ><n-icon size="16" class="page-icon">
                    <DocumentTextOutline />
                  </n-icon>
                  {{ page.title }}
                </n-button>
              </div>
            </div>
          </div>
        </n-spin>
      </n-collapse-item>
    </n-collapse>
  </div>
  <div class="nailed-section">
    <n-button quaternary block>
      <n-icon size="24">
        <ShareSocial />
      </n-icon>
      <span>Chia sẻ</span>
    </n-button>
  </div>
</template>
<script>
import { defineComponent, ref, onMounted, computed } from "vue";
import {
  MailUnread,
  ShareSocial,
  Add,
  DocumentTextOutline,
  CompassOutline,
} from "@vicons/ionicons5";
import api from "../../api/axios";
import { useWorkspaceStore } from "../../store/workspace";
import { usePageStore } from "../../store/page";

export default defineComponent({
  name: "Sidebar",
  components: {
    MailUnread,
    ShareSocial,
    Add,
    DocumentTextOutline,
    CompassOutline,
  },
  setup() {
    console.log("Sidebar setup initialized");
    const showModalWorkspace = ref(false);
    const showModelPage = ref(false);
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

    const fetchWorkspaces = async () => {
      console.log("Starting fetchWorkspaces");
      try {
        isLoading.value = true;
        const response = await api.get("/workspace/list");
        workspaceStore.workspaces = response.data;
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
        }
      } catch (error) {
        console.error("Error fetching workspaces:", error);
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
      expandedNames.value = ["1"];
      fetchPagesList();
    };
    const fetchPagesList = async () => {
      try {
        isLoadingPages.value = true;
        const response = await api.get(
          "/page/list/" + workspaceStore.selectedSpace
        );
        usePageStore.pages = response.data;
        if (response && response.data) {
          if (Array.isArray(response.data)) {
            pages.value = response.data;
          } else if (response.data.data && Array.isArray(response.data.data)) {
            pages.value = response.data.data;
          } else if (typeof response.data === "object") {
            pages.value = Object.values(response.data);
          }
        }
      } catch (error) {
        console.error("Error fetching pages:", error);
      } finally {
        isLoadingPages.value = false;
      }
    };
    const handleAddPages = async () => {
      if (!workspaceStore.selectedSpace) {
        // You might want to show a message to the user that they need to select a workspace first
        return;
      }
      try {
        // First create the page in store
        const newPage = pageStore.createNewPage(workspaceStore.selectedSpace);

        // Then create it in the backend
        const response = await api.post("/page/create", {
          title: newPage.title,
          workspaceId: workspaceStore.selectedSpace,
        });

        // Refresh the pages list
        await fetchPagesList();
      } catch (error) {
        console.error("Error creating page:", error);
      }
    };
    const handlePageClick = async (pageId) => {
      pageStore.setSelectedPage(pageId);
      await pageStore.fetchPageContent(pageId);
    };
    // Initialize data
    onMounted(async () => {
      console.log("Sidebar component mounted");
      await fetchWorkspaces();
    });
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
      options: [
        {
          label: "Cài đặt",
          key: "settings",
        },
        {
          label: "Đăng xuất",
          key: "brown's hotel, london",
        },
      ],
      workspaceStore,
      pageStore,
      expandedNames,
    };
  },
});
</script>

<style scoped>
.inbox,
.account {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.workspace,
.pages {
  width: 100%;
}

/* Make buttons stretch full width */
.n-button {
  width: 100%;
  justify-content: flex-start;
}

/* Make collapse stretch full width */
:deep(.n-collapse) {
  width: 100%;
}

.inbox,
.account,
.workspace,
.pages {
  padding: 8px;
}
.nailed-section {
  position: sticky;
  bottom: 0;
  width: 100%;
  padding: 8px;
  background-color: var(--n-color);
  border-top: 1px solid var(--n-border-color);
}

.collapse-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

/* Make the add button more compact in the header */
.collapse-header .n-button {
  width: auto;
  min-width: 0;
  padding: 4px;
}

.workspace-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.workspace-item {
  width: 100%;
}

.workspace-item .n-button {
  text-align: left;
  padding: 8px;
}
.workspace-selected {
  background-color: rgba(0, 0, 0, 0.2) !important;
}
.page-icon,
.workspace-icon {
  margin-right: 8px;
}
</style>
