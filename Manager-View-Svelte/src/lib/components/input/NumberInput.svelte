<script lang="ts">
    import BaseInput from "./BaseInput.svelte";

    export let labelText: string;
    export let labelTextAlt: string;

    export let value: any;

    export let step: number = 1;

    export let max: number | undefined = undefined;
    export let min: number | undefined = undefined;

    $: if (max !== undefined && value > max) value = max;
    $: if (min !== undefined && value < min) value = min;
</script>

<BaseInput {labelText} {labelTextAlt}>
    <input type="number" class="input input-bordered" bind:value {step} />
</BaseInput>
