<template>
  <div style="width: 100%; height: 100%">
    <div :id="holderId" class="editor" ref="editorContainer"></div>
  </div>
</template>

<script>
import EditorJS from "@editorjs/editorjs";
import Header from "@editorjs/header";
import List from "@editorjs/list";
import Paragraph from "@editorjs/paragraph";
import { usePageStore } from "../store/page";
import { watch } from "vue";

export default {
  name: "Editor",
  props: {
    holderId: {
      type: String,
      default: "editorjs",
    },
  },
  mounted() {
    this.$nextTick(() => {
      if (!this.$refs.editorContainer) {
        console.error("Editor container not found");
        return;
      }
      this.editor = new EditorJS({
        holder: this.holderId,
        autofocus: true,
        tools: {
          header: Header,
          list: List,
          paragraph: Paragraph,
        },
        onReady: () => {
          console.log("Editor.js is ready");
          this.$refs.editorContainer.style.width = "100%";
          setTimeout(() => {
            window.dispatchEvent(new Event("resize"));
          }, 100);
        },
        onChange: async () => {
          const content = await this.editor.save();
          this.$emit("update:content", content);
        },
      });
    });
  },
  beforeUnmount() {
    if (this.editor && this.editor.destroy) {
      this.editor.destroy();
    }
  },
};
</script>
<style>
:host,
.editor {
  display: block;
  width: 100%;
  height: 100%;
}
</style>
