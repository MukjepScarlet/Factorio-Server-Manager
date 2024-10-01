<script lang="ts">
    import BaseInput from "./BaseInput.svelte";

    export let labelText: string;
    export let labelTextAlt: string;

    export let checked: any;
</script>

<BaseInput {labelText} {labelTextAlt}>
    <input type="checkbox" class="toggle m-auto" bind:checked />
</BaseInput>
