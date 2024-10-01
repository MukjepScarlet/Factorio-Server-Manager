<script lang="ts">
    import BaseInput from "./BaseInput.svelte";

    export let labelText: string;
    export let labelTextAlt: string;

    export let value: any;
</script>

<BaseInput {labelText} {labelTextAlt}>
    <input type="text" class="input input-bordered" bind:value />
</BaseInput>
