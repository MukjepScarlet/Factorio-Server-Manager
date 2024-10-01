<script lang="ts">
    import type { EventHandler } from 'svelte/elements';

    export let title: string | undefined = undefined;

    export let ui: HTMLDialogElement;

    export let style: string | undefined = undefined;

    export let onClose: EventHandler<Event, HTMLDialogElement> | undefined = undefined;
</script>

<dialog class="modal modal-bottom sm:modal-middle" bind:this={ui} on:close={onClose}>
    <div class="modal-box min-w-fit" {style}>
        <form method="dialog">
            <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
        </form>
        <h3 class="inline-flex gap-2 lg:gap-4 font-bold text-lg mb-4">
            <slot name="title">
                {#if title}
                    {title}
                {/if}
            </slot>
        </h3>

        <slot name="content"></slot>

        <slot name="action"></slot>
    </div>
</dialog>
