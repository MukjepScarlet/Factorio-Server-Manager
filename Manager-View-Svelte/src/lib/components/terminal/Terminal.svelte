<script lang="ts">
    import NetUtil from '$lib/util/net';
    import { onMount } from 'svelte';
    import { success, warning } from '../alert/alert';

    export let config: ServerConfig;

    let class_ = '';
    export { class_ as class };

    let message = '';
    let textareaRef: HTMLTextAreaElement;

    let ws: WebSocket;

    const submitHandler = () => {
        send = send.trim();
        if (!send.length) return;

        if (ws?.readyState === WebSocket.OPEN) {
            ws.send(send);
            success('发送成功!');
        } else {
            warning('发送失败, WebSocket 连接未建立或已关闭!');
        }
    };

    onMount(() => {
        ws = NetUtil.createWSClient(config.path);
        ws.onmessage = (e: MessageEvent) => {
            message += '\n';
            message += e.data;
            textareaRef.scrollTop = textareaRef.scrollHeight;
        };
        return () => ws?.close();
    });

    let send = '';
</script>

<div class="{class_} grid join join-vertical">
    <textarea class="w-full h-[25vh] border join-item font-mono resize-none" readonly bind:value={message} bind:this={textareaRef} />

    <form class="w-full flex justify-between items-center border join-item" on:submit|preventDefault={submitHandler}>
        <label class="input w-full flex items-center">
            <i class="fa-solid fa-terminal"></i>
            <input type="text" class="w-full px-4 font-mono" bind:value={send} />
            <button class="btn btn-circle btn-ghost">
                <i class="fa-solid fa-paper-plane"></i>
            </button>
        </label>
    </form>
</div>
