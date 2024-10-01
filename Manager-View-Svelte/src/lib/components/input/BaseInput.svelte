<script lang="ts">
    export let labelText: string;
    export let labelTextAlt: string;
</script>

<label class="form-control">
    <div class="label">
        <span class="label-text text-nowrap overflow-x-hidden grow">{labelText}</span>
        <!-- <span class="label-text-alt text-nowrap overflow-x-hidden">{labelTextAlt}</span> -->
    </div>
    <slot />
</label>
