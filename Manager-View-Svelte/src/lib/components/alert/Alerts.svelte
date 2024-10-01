<script lang="ts">
    import { onDestroy, onMount } from 'svelte';
    import { slide } from 'svelte/transition';
    import { alertQueue, error } from './alert';
    import { browser } from '$app/environment';
    import Timer from './Timer?worker';

    const interval = 20;

    let timer: Worker | undefined = undefined;
    const initWebWorker = () => {
        if (!browser || !window.Worker) return;

        timer = new Timer();
        timer.onerror = (e) => error(e.message);
        timer.onmessage = () => {
            if ($alertQueue.length) {
                $alertQueue[0].ttl -= interval;
                alertQueue.update((value) => value.filter((a) => a.ttl > 0));
            }
        };

        timer.postMessage(interval);
    };

    const terminateWorker = () => {
        timer?.postMessage(-1);
        timer?.terminate();
    };

    onMount(initWebWorker);
    onDestroy(terminateWorker);
</script>

<div class="fixed flex flex-col items-center z-[1000] left-1/2 -translate-x-1/2 lg:max-w-[75vw]">
    <!-- 队列 -->
    {#each $alertQueue as alert (alert.createTime)}
        <div role="alert" class="alert flex {alert.type ?? ''} w-fit mt-4" transition:slide>
            {#if alert.type === 'alert-info'}
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" class="stroke-current shrink-0 w-6 h-6"
                    ><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                    ></path></svg
                >
            {:else if alert.type === 'alert-success'}
                <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"
                    ><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg
                >
            {:else if alert.type === 'alert-warning'}
                <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"
                    ><path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
                    /></svg
                >
            {:else if alert.type === 'alert-error'}
                <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"
                    ><path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
                    /></svg
                >
            {:else}
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" class="stroke-info shrink-0 w-6 h-6"
                    ><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                    ></path></svg
                >
            {/if}

            <span>{alert.content}</span>

            <div class="radial-progress" style:--value={(100 * alert.ttl) / alert.time} style="--size:2rem; --thickness:3px" role="progressbar">
                <!-- 左键关闭 右键清空 -->
                <button
                    class="btn btn-sm btn-circle btn-ghost tooltip tooltip-right"
                    data-tip="左键关闭 / 右键清空"
                    on:click|preventDefault={() => (alert.ttl = 0)}
                    on:contextmenu|preventDefault={() => $alertQueue.forEach((it) => (it.ttl = 0))}
                >
                    ✕
                </button>
            </div>
        </div>
    {/each}
</div>
