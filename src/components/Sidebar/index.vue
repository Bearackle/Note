<template>
  <div class="account">
    <n-avatar
      round
      size="medium"
      style="background-color: #2080f0; color: white"
    >
      M
    </n-avatar>
    <n-dropdown trigger="hover" :options="options">
      <div class="account-wrapper">
        <n-button>Tài khoản</n-button>
      </div>
    </n-dropdown>
  </div>
  <div class="home">
    <n-button quaternary>
      <n-icon size="24" style="margin-right: 20px">
        <Home />
      </n-icon>
      <n-text>Home</n-text>
    </n-button>
  </div>
  <div class="inbox">
    <n-button quaternary>
      <n-icon size="24" style="margin-right: 20px">
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
                    <PlanetOutline />
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
                <div class="page-content">
                  <n-button
                    text
                    block
                    @click="handlePageClick(page.id)"
                    :class="{
                      'page-selected': pageStore.selectedPage === page.id,
                    }"
                  >
                    <n-icon size="16" class="page-icon">
                      <DocumentTextOutline />
                    </n-icon>
                    {{ page.title }}
                  </n-button>
                </div>
                <n-dropdown
                  trigger="click"
                  :options="[
                    {
                      label: 'Cập nhật tiêu đề',
                      key: 'update',
                      props: {
                        onClick: () => {
                          console.log('Update clicked for page:', page);
                          handleUpdatePageTitle(page);
                        },
                      },
                    },
                    {
                      label: 'Xóa trang',
                      key: 'delete',
                      props: {
                        onClick: () => handleDeletePage(page.id),
                      },
                    },
                  ]"
                  placement="bottom-end"
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
            </div>
          </div>
        </n-spin>
      </n-collapse-item>
    </n-collapse>
  </div>
  <div class="nailed-section">
    <n-button quaternary block>
      <n-icon size="24" style="margin-right: 16px">
        <PersonAdd />
      </n-icon>
      <span>Mời thành viên</span>
    </n-button>
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
</template>
<script>
import { defineComponent, ref, onMounted, computed } from "vue";
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
        pageStore.setPages(response.data.data);
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
      handleUpdatePageTitle,
      handleDeletePage,
      confirmDeletePage,
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
      showDeleteModal,
      showUpdateTitleModal,
      updateTitleFormRef,
      updateTitleForm,
      updateTitleRules,
      confirmUpdateTitle,
    };
  },
});
</script>

<style scoped>
.inbox,
.home,
.account {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.account-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.workspace,
.pages {
  width: 100%;
}

/* Add background color to the entire sidebar */
:deep(.n-layout-sider) {
  background-color: #f8f8f7;
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

.workspace-item .n-button,
.page-item .n-button {
  text-align: left;
  padding: 8px;
  color: inherit !important;
}

.workspace-item .n-button:hover,
.page-item .n-button:hover {
  color: inherit !important;
  background-color: rgba(0, 0, 0, 0.1) !important;
}

.workspace-selected,
.page-selected {
  background-color: rgba(0, 0, 0, 0.2) !important;
  color: inherit !important;
}

.page-icon,
.workspace-icon {
  margin-right: 8px;
}
.page-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 0;
  width: 100%;
}

.page-content {
  flex: 1;
  min-width: 0; /* This prevents flex item from growing beyond container */
}

.page-content .n-button {
  width: 100%;
  text-align: left;
  padding: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.page-actions {
  opacity: 0;
  flex-shrink: 0;
  width: 32px;
  height: 32px;
  padding: 0;
  margin-left: 0px;
  border-radius: 0 !important;
  background-color: transparent !important;
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

.page-actions-selected {
  opacity: 1 !important;
  border-radius: 0 !important;
  background-color: rgba(0, 0, 0, 0) !important;
}

.page-item:hover .page-actions {
  /* opacity: 1; */
  background-color: rgba(0, 0, 0, 0.1) !important;
}

.page-item:hover .page-actions:hover {
  background-color: rgba(0, 0, 0, 0.1) !important;
}

.page-icon {
  margin-right: 8px;
}
</style>
