<template>
  <n-space vertical size="large">
    <n-layout has-sider>
      <n-layout-sider
        collapse-mode="width"
        :collapsed-width="100"
        :width="240"
        :show-collapsed-content="false"
        show-trigger="arrow-circle"
        content-style="padding: 16px 0; height: 100vh; display: flex; flex-direction: column;"
        bordered
      >
        <Sidebar></Sidebar>
      </n-layout-sider>
      <n-layout-content
        content-style="padding: 12px; flex: 1; width: 100%; height: 100%;"
      >
        <div style="height: 100%">
          <router-view style="height: 100%; width: 100%" />
        </div>
      </n-layout-content>
    </n-layout>
  </n-space>
</template>
<script>
import { defineComponent } from "vue";
import Sidebar from "@/components/Sidebar/index.vue";
export default defineComponent({
  components: {
    Sidebar,
  },
});
</script>
<style scoped>
html,
body,
#app {
  width: 100vw;
  height: 100vh;
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.n-space {
  width: 100%;
}

.n-layout {
  width: 100%;
  height: 100vh;
}

.n-layout-content {
  flex: 1;
  overflow: auto;
  height: 100vh;
}

:deep(.n-layout-sider-scroll-container) {
  height: 100% !important;
  display: flex !important;
  flex-direction: column !important;
}
</style>
