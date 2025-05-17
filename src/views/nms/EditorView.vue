<template>
  <div class="editor-container">
    <Editor
      class="editor"
      @update:content="handleContent"
      @onChange:content="handleChange"
    />
  </div>
</template>

<script>
import Editor from "../../components/Editor.vue";
import api from "../../api/axios";

export default {
  components: { Editor },
  methods: {
    handleContent(content) {
      console.log("Nội dung: ", content);
    },
    handleChange(content) {
      console.log("Nội dung:", content);
    },
  },
};
</script>
<style scoped>
.editor-container {
  width: 100%;
  min-width: 100%;
  height: 100vh;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.editor {
  flex: 1;
  width: 100%;
  height: 100%;
  min-width: 100%;
}
</style>
