<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <h2>
        <i>{{ "Welcome to " + author + "'s work" }}</i>
      </h2>
      <h2>{{ pageTitle }}</h2>
      <div class="avatar">{{ authorInitial }}</div>
      <button class="continue-btn" @click="onContinue">Continue</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from "vue-router";
import api from "../../api/axios";
import { usePageStore } from "@/store/page";
import { useBlockStore } from "@/store/block";
import { ref, onMounted } from "vue";
import { useHashids } from "../../utils/useHashids";
const router = useRouter();
const route = useRoute();
const pageStore = usePageStore();
const blockStore = useBlockStore();

const pageTitle = ref("");
const author = ref("");
const authorInitial = ref("");

onMounted(async () => {
  try {
    const { decode } = useHashids();
    const response = await api.get(
      "/page/" + decode(route.params.pageId) + "/share-info"
    );
    if (response.data && response.data.data) {
      pageTitle.value = response.data.data.title;
      author.value = response.data.data.author;
      authorInitial.value = response.data.data.author
        ? response.data.data.author.charAt(0).toUpperCase()
        : "?";
    }
  } catch (e) {
    console.error(e);
  }
});

async function handleNoteGet(pageId) {
  pageStore.setSelectedPage(pageId);
  pageStore.setSelectedTitle(pageTitle.value);
  try {
    const response = await api.get("/block/list/" + pageId);
    if (response.data.data == null) {
      blockStore.selectedBlockIndex = 0;
    }
    const blocks = Array.isArray(response.data.data)
      ? response.data.data.map((element) => ({
          type: element.type,
          data: JSON.parse(element.content),
          id: element.idBlock,
        }))
      : [];
    pageStore.setCurrentContent({
      time: new Date().getTime(),
      version: "2.30.0",
      blocks: blocks,
    });
  } catch (e) {
    console.error(e);
  }
}
async function handleApiPermission(pageId) {
  const response = await api.get("/page/get-permission/" + pageId);
  if (response.data.data) {
    pageStore.isReadOnly = response.data.data.isEditable == 0;
  }
}
async function onContinue() {
  const pageId = route.params.pageId;
  const { decode } = useHashids();
  const decodePageId = decode(pageId);
  await handleNoteGet(decodePageId);
  await handleApiPermission(decodePageId);
  router.push("/note/edit");
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: #fff;
  padding: 2.5rem 2rem;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(44, 62, 80, 0.2);
  text-align: center;
  min-width: 320px;
}
.modal-content h2 {
  margin-bottom: 1rem;
  font-size: 2rem;
  color: #2c3e50;
}
.avatar {
  width: 40px;
  height: 40px;
  margin: 0 auto 0.5rem auto;
  border-radius: 50%;
  background: #2c3e50;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}
.author-name {
  font-size: 1.1rem;
  color: #888;
  margin-bottom: 2rem;
}
.continue-btn {
  padding: 0.75rem 2.5rem;
  font-size: 1.1rem;
  background: #42b983;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}
.continue-btn:hover {
  background: #369870;
}
</style>
