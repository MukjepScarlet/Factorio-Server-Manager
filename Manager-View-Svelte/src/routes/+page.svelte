<script lang="ts">
    import AddForm from '$lib/components/config/AddForm.svelte';
    import Editor from '$lib/components/config/Editor.svelte';
    import ListItem from '$lib/components/config/ListItem.svelte';
    import Terminal from '$lib/components/terminal/Terminal.svelte';
    import NetUtil from '$lib/util/net';
    import { onMount } from 'svelte';

    let activeIndex = 0;

    let configs: ServerConfig[] = [];

    const reload = async () => {
        await NetUtil.getAllConfig().then((r) => configs = r.content!);
        // console.log(configs);
    };

    onMount(reload);
</script>

<main class="w-screen h-screen flex">
    <aside class="h-screen overflow-y-scroll w-[25vw] flex flex-col gap-4 p-4">
        {#each configs as config, i (config.path)}
            <ListItem {config} active={i === activeIndex} on:remove={reload}>
                <input type="radio" class="hidden" bind:group={activeIndex} value={i} />
            </ListItem>
        {/each}
        <AddForm on:success={reload} />
    </aside>

    <div class="w-full h-screen flex flex-col justify-between">
        {#if configs.length}
            <Editor config={configs[activeIndex]} />
            <Terminal config={configs[activeIndex]} />
        {:else}
            <!-- TODO: HINT -->
        {/if}
    </div>
</main>

<!-- flex flex-col gap-4 bg-base-100 w-full m-4 p-4 rounded-xl shadow-xl -->
<style lang="postcss">
</style>
