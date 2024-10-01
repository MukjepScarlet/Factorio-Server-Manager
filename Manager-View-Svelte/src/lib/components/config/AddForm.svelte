<script lang="ts">
    import NetUtil from '$lib/util/net';
    import { createEventDispatcher } from 'svelte';
    import { error, success } from '../alert/alert';

    let name = '';
    let path = '';

    const dispatcher = createEventDispatcher<{ success: null }>();

    async function handler() {
        const result = await NetUtil.addConfig(name, path);
        if (result.code !== 0) {
            error(result.message);
        } else {
            success('添加成功!');
            dispatcher('success', null);
        }
    }
</script>

<form class="join" on:submit|preventDefault={handler}>
    <div class="join join-vertical w-full join-item">
        <label class="label input gap-2 input-bordered join-item">
            <i class="fa-solid fa-tag"></i>
            <input type="text" class="grow" placeholder="输入配置名称..." bind:value={name} />
        </label>
        <label class="label input gap-2 input-bordered join-item">
            <i class="fa-solid fa-folder-open"></i>
            <input type="text" class="grow font-mono" placeholder="游戏根目录" bind:value={path} />
        </label>
    </div>
    <button class="btn btn-primary h-full join-item">
        <i class="fa-solid fa-plus fa-xl"></i>
    </button>
</form>
