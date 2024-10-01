<script lang="ts">
    let class_ = '';

    export { class_ as class };
    export let value: string;
    export let readonly = false;

    let inputWidth = 'auto';

    let inputElement: HTMLInputElement;

    $: if (inputElement) {
        const computedStyle = window.getComputedStyle(inputElement);

        const span = document.createElement('span');
        span.style.visibility = 'hidden';
        span.style.whiteSpace = 'nowrap';
        span.style.font = computedStyle.font;
        span.style.padding = computedStyle.padding;
        span.style.border = computedStyle.border;
        span.style.boxSizing = 'border-box';
        span.textContent = value || ' ';

        document.body.appendChild(span);
        inputWidth = `${span.offsetWidth}px`;
        document.body.removeChild(span);
    }
</script>

<input type="text" class={class_} bind:value {readonly} style="width: {inputWidth}" bind:this={inputElement} />
