<script lang="ts">
    import NetUtil from '$lib/util/net';
    import { createEventDispatcher } from 'svelte';
    import { readable } from 'svelte/store';
    import { fade } from 'svelte/transition';

    export let config: ServerConfig;

    export let active = false;

    const running = readable(false, (set) => {
        const interval = setInterval(() => {
            NetUtil.status(config.path).then((r) => set(r.content ?? false));
        }, 5000);

        return () => clearInterval(interval);
    });

    const dispatcher = createEventDispatcher<{ remove: null; start: null; stop: null }>();

    const startHandler = async () => {
        await NetUtil.start(config.path);
    };

    const stopHandler = async () => {
        await NetUtil.stop(config.path);
    };

    const removeHandler = async () => {
        stopHandler();
        await NetUtil.removeConfig(config.name, config.path);
        dispatcher('remove', null);
    };
</script>

<label class="bg-base-100 w-full p-4 rounded-xl shadow-xl transition-[background-color] duration-200 {active ? 'border-primary border-2' : ''}" transition:fade>
    <slot><!-- optional fallback --></slot>
    <div class="flex flex-col gap-2 w-full">
        <div class="flex justify-between">
            <span class="flex gap-2 items-end">
                <h2 class="text-xl font-semibold">{config.name}</h2>
                <span class="font-mono">{config.port}</span>
            </span>
            <span>
                <button class="btn btn-sm btn-circle btn-ghost" disabled={$running} on:click={startHandler}>
                    <i class="fa-solid fa-play"></i>
                </button>
                <button class="btn btn-sm btn-circle btn-ghost" disabled={!$running} on:click={stopHandler}>
                    <i class="fa-solid fa-stop"></i>
                </button>
                <button class="btn btn-sm btn-circle btn-ghost" on:click={removeHandler}>
                    <i class="fa-solid fa-trash text-error"></i>
                </button>
            </span>
        </div>
        <p class="font-mono">{config.path}</p>
    </div>
</label>
