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
import { useBlockStore } from "../store/block";
import { watch, onMounted, ref, toRaw } from "vue";
import { diff } from "deep-diff";
import { debounce, method, update } from "lodash";
import api from "../api/axios";
export default {
  name: "Editor",
  props: {
    holderId: {
      type: String,
      default: "editorjs",
    },
  },
  setup() {
    const pageStore = usePageStore();
    let editor = null;
    const changes = ref([]);
    const initEditor = () => {
      editor = new EditorJS({
        holder: "editorjs",
        autofocus: true,
        tools: {
          header: Header,
          list: List,
          paragraph: Paragraph,
        },
        data: pageStore.currentContent,
        onReady: () => {
          console.log("Editor.js is ready");
        },
        onChange: (api, event) => {
          handleEditorChangeEvent(event);
        },
        //  onChange: debounce(handleEditorChange, 5000),
      });
    };
    const handleEditorChangeEvent = async (event) => {
      let prev = toRaw(pageStore.currentContent);
      const editorContent = await editor.save();
      const content = toRaw(editorContent);
      console.log("content", content);
      console.log("event", event);
      // Convert single event to array if needed
      const events = Array.isArray(event) ? event : [event];

      // Process each event
      for (const currentEvent of events) {
        const detail = currentEvent.detail;
        if (currentEvent.type == "block-added") {
          useBlockStore.selectedBlockIndex = detail.index;
          // Update prev after adding block
          usePageStore.currentContent = content;
          prev = toRaw(usePageStore.currentContent);
        }
        if (currentEvent.type == "block-changed") {
          if (
            useBlockStore.selectedBlockIndex != null &&
            useBlockStore.selectedBlockIndex == detail.index
          ) {
            //call api to add data
            console.log("create data index", detail.index);
            const payload = {
              pageId: pageStore.selectedPage,
              type: content.blocks[detail.index].type,
              content: JSON.stringify(content.blocks[detail.index].data),
              blockOrder: detail.index,
              idBlock: content.blocks[detail.index].id,
            };
            const response = await api.post("/block/create", payload);
            console.log("call to block create", response);
            useBlockStore.selectedBlockIndex = null;
          } else {
            //call api to update data
            const payload = {
              pageId: pageStore.selectedPage,
              type: content.blocks[detail.index].type,
              content: JSON.stringify(content.blocks[detail.index].data),
              idBlock: content.blocks[detail.index].id,
            };
            const response = await api.put(
              "/block/" + content.blocks[detail.index].id,
              payload
            );
          }
          // Update prev after changing block
          usePageStore.currentContent = content;
          prev = toRaw(usePageStore.currentContent);
        }
        if (currentEvent.type == "block-removed") {
          console.log("block-removed index", detail.index);
          const blockRemoved =
            prev.blocks[detail.index] || content.blocks[detail.index];
          //call api to delete data
          const response = await api.delete("/block/" + blockRemoved.id);
          // Update prev after removing block
          usePageStore.currentContent = content;
          prev = toRaw(usePageStore.currentContent);
        }
        if (currentEvent.type == "block-moved") {
          //call api to update order
          const payload1 = {
            pageId: pageStore.selectedPage,
            blockOrder: detail.toIndex,
          };
          console.log("detail.fromIndex moved", detail.fromIndex);
          console.log("prev block moved", prev.blocks);

          // Safety check for prev.blocks access
          const fromBlock =
            prev.blocks[detail.fromIndex] || content.blocks[detail.fromIndex];
          const toBlock =
            prev.blocks[detail.toIndex] || content.blocks[detail.toIndex];

          if (!fromBlock || !toBlock) {
            console.error("Invalid block indices for move operation");
            return;
          }

          const response1 = await api.put("/block/" + fromBlock.id, payload1);
          const payload2 = {
            pageId: pageStore.selectedPage,
            blockOrder: detail.fromIndex,
          };
          const response2 = await api.put("/block/" + toBlock.id, payload2);
          // Update prev after moving blocks
          usePageStore.currentContent = content;
          prev = toRaw(usePageStore.currentContent);
        }
      }
      // Final update of store content
      usePageStore.currentContent = content;
    };
    const handleEditorChange = async (event) => {
      const editorContent = await editor.save();
      const content = toRaw(editorContent);
      const prev = toRaw(pageStore.currentContent);
      console.log("prev", prev);
      console.log("content", content);
      changes.value = diff(prev.blocks, content.blocks);
      console.log(changes.value);
      const changesRaw = toRaw(changes.value);

      // Check for new blocks in the changes
      if (changes.value) {
        changesRaw.forEach(async (change) => {
          if (change.kind === "A") {
            if (change.item.kind === "N") {
              // A new block was added
              const blockIndex = change.index;
              const newBlock = content.blocks[blockIndex];
              try {
                // Debug log to see the request payload
                const payload = {
                  pageId: pageStore.selectedPage,
                  type: newBlock.type,
                  content: JSON.stringify(newBlock.data),
                  blockOrder: blockIndex,
                  idBlock: newBlock.id,
                };
                console.log("Sending request with payload:", payload);

                // Make API call to create new block
                const response = await api.post("/block/create", payload);

                if (!response.data) {
                  throw new Error("Failed to create block");
                }

                console.log("Block created:", response.data);
              } catch (error) {
                console.error("Error creating block:", error);
              }
            }
            if (change.item.kind === "D") {
              //a block has been deleted
              console.log("change.item.lhs.id", change.item.lhs.id);
              try {
                console.log("Deleting block:", change.item.lhs.id);
                const response2 = await api.delete(
                  "/block/" + change.item.lhs.id
                );
                if (!response2.data) {
                  throw new Error("Failed to delete block");
                }
                console.log("Block delete");
              } catch (error) {
                console.error("Error delete block");
              }
            }
          }
          if (change.kind === "E") {
            {
              // This is a content update
              console.log("content index data", content.blocks[change.path[0]]);
              const updateBlock = prev.blocks[change.path[0]];
              const attr = change.path[1];
              let idBlock = updateBlock.id;
              let dataBlock = updateBlock.data;
              if (attr == "data") {
                dataBlock = { [change.path[2]]: change.rhs };
              }
              if (attr == "id") {
                idBlock = change.rhs;
              }
              try {
                console.log("begin updating block");
                const payloadUpdate = {
                  pageId: pageStore.selectedPage,
                  type: updateBlock.type,
                  content: JSON.stringify(dataBlock),
                  idBlock: idBlock,
                };
                console.log("payloadUpdate", payloadUpdate);
                const responseUpdate = await api.put(
                  "/block/" + updateBlock.id,
                  payloadUpdate
                );
                if (!responseUpdate) {
                  throw new Error("Failed to update block");
                }
                console.log("block updated");
              } catch (error) {
                console.error("Error updating block");
              }
            }
          }
        });
      }
      console.log("content after delete", content);
      // Update the store with new content
      pageStore.currentContent = content;
    };
    onMounted(() => {
      initEditor();
    });

    // Watch for changes in currentContent
    watch(
      () => pageStore.currentContent,
      async (newContent) => {
        if (editor && newContent) {
          // Clear existing content and render new blocks
          await editor.render(newContent);
        }
      },
      { deep: true }
    );

    return {
      pageStore,
    };
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
