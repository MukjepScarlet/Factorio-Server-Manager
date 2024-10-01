interface Temp {
    key: keyof ServerSettings,
    labelText: string,
    labelTextAlt: string,
    editor: 'string' | 'number' | 'boolean',
}[]

export const SERVER_SETTINGS_TEXT = [
    {
        key: 'name',
        labelText: '名称',
        labelTextAlt: 'name',
        editor: 'string',
    },
    {
        key: 'description',
        labelText: '简介',
        labelTextAlt: 'description',
        editor: 'string',
    },
    {
        key: 'maxPlayers',
        labelText: '最多玩家',
        labelTextAlt: 'max_players',
        editor: 'number',
    },
    {
        key: 'gamePassword',
        labelText: '房间密码',
        labelTextAlt: 'game_password',
        editor: 'string',
    },
    {
        key: 'maxUploadInKilobytesPerSecond',
        labelText: '最大流量 (KB/s)',
        labelTextAlt: 'max_upload_in_kilobytes_per_second',
        editor: 'number',
    },
    {
        key: 'requireUserVerification',
        labelText: '正版验证',
        labelTextAlt: 'require_user_verification',
        editor: 'boolean',
    },
    // {
    //     key: 'visibility.public',
    //     labelText: '大厅公开',
    //     labelTextAlt: 'visibility.public',
    //     editor: 'boolean',
    // },
    // {
    //     key: 'visibility.lan',
    //     labelText: '内网公开',
    //     labelTextAlt: 'visibility.lan',
    //     editor: 'boolean',
    // },
    {
        key: 'autosaveInterval',
        labelText: '保存间隔 (分钟)',
        labelTextAlt: 'autosave_interval',
        editor: 'number',
    },
    {
        key: 'autosaveSlots',
        labelText: '自动存档数',
        labelTextAlt: 'autosave_slots',
        editor: 'number',
    },
    {
        key: 'afkAutokickInterval',
        labelText: '挂机踢出延迟 (分钟)',
        labelTextAlt: 'afk_autokick_interval',
        editor: 'number',
    },
    {
        key: 'autoPause',
        labelText: '自动暂停',
        labelTextAlt: 'auto_pause',
        editor: 'boolean',
    },
    {
        key: 'onlyAdminsCanPauseTheGame',
        labelText: '仅管理员暂停',
        labelTextAlt: 'only_admins_can_pause_the_game',
        editor: 'boolean',
    },
] as const;
