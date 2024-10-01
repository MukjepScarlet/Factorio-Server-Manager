<script lang="ts">
    import NetUtil from '$lib/util/net';
    import { createEventDispatcher } from 'svelte';
    import { success } from '../alert/alert';
    import AutoTextInput from '../input/AutoTextInput.svelte';
    import StringInput from '../input/StringInput.svelte';
    import { SERVER_SETTINGS_TEXT } from '$lib/types/settings';
    import NumberInput from '../input/NumberInput.svelte';
    import BooleanInput from '../input/BooleanInput.svelte';

    export let config: ServerConfig;

    const dispatcher = createEventDispatcher<{ save: ServerConfig }>();

    const saveHandler = async () => {
        config.processParams = config.processParams.filter((it) => it.length);
        config.adminList = config.adminList.filter((it) => it.length);
        await NetUtil.updateConfig(config).then((r) => {
            if (r.code === 0) {
                success(`配置 ${config.name} 保存成功!`);
                dispatcher('save', config);
            }
        });
    };
</script>

<div class="flex flex-col gap-4 p-4 w-full h-full">
    <!-- 主要参数 -->
    <div class="flex gap-4 items-center justify-between">
        <label class="form-control">
            <span class="label-text">配置名</span>
            <input type="text" class="input input-bordered input-primary" bind:value={config.name} />
        </label>

        <button class="btn btn-primary" on:click={saveHandler}>
            <i class="fa-solid fa-floppy-disk"></i>
            保存配置
        </button>
    </div>

    <!-- 启动参数 -->
    <div class="form-control">
        <span class="label-text">启动参数</span>
        <div class="join">
            {#each ['~/factorio.exe', '--port'] as value}
                <AutoTextInput class="input input-sm input-bordered font-mono join-item" readonly {value} />
            {/each}
            <input type="number" class="input input-sm input-bordered font-mono join-item w-[calc(5rem+24px)]" bind:value={config.port} />
            {#each config.processParams as param, i (i)}
                <AutoTextInput class="input input-sm input-bordered font-mono join-item" bind:value={param} />
            {/each}
            <button class="btn btn-sm btn-primary join-item" on:click={() => (config.processParams = [...config.processParams, ''])}>
                <i class="fa-solid fa-plus fa-xl"></i>
            </button>
        </div>
    </div>
    <!-- 配置参数 -->
    <div class="grid grid-cols-6 gap-4">
        {#each SERVER_SETTINGS_TEXT as {key, labelText, labelTextAlt, editor} (key)}
            {#if editor === 'string'}
                <StringInput {labelText} {labelTextAlt} bind:value={config.settings[key]} />
            {:else if editor === 'number'}
                <NumberInput {labelText} {labelTextAlt} bind:value={config.settings[key]} />
            {:else if editor === 'boolean'}
                <BooleanInput {labelText} {labelTextAlt} bind:checked={config.settings[key]} />
            {/if}
        {/each}
    </div>
    <!-- 黑名单管理 -->
    <!-- TODO -->

    <!-- 管理员管理 -->
    <div class="form-control">
        <span class="label-text">管理员列表</span>
        <div class="join">
            <!-- TODO: 添加同步功能 -->
            <!-- <button class="btn btn-sm btn-secondary join-item">
                <i class="fa-solid fa-rotate"></i>
            </button> -->
            {#each config.adminList as name, i (i)}
                <AutoTextInput class="input input-sm input-bordered font-mono join-item" bind:value={name} />
            {/each}
            <button class="btn btn-sm btn-primary join-item" on:click={() => (config.adminList = [...config.adminList, ''])}>
                <i class="fa-solid fa-plus fa-xl"></i>
            </button>
        </div>
    </div>
</div>
