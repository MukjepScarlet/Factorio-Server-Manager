interface ServerConfig {
    name: string;
    path: string;
    port: number;
    processParams: string[];
    adminList: string[];
    banList: { username: string, reason?: string }[];
    settings: ServerSettings;
};

interface ServerSettings {
    name: string;
    description: string;
    tags: string[];

    maxPlayers: number;

    visibility: {
        public: boolean;
        lan: boolean;
    };

    username: string;
    password: string;
    token: string;

    gamePassword: string;

    requireUserVerification: boolean;

    maxUploadInKilobytesPerSecond: number;
    maxUploadSlots: number;
    minimumLatencyInTicks: number;
    maxHeartbeatsPerSecond: number;
    ignorePlayerLimitForReturningPlayers: boolean;

    allowCommands: 'admins-only' | boolean;

    autosaveInterval: number;
    autosaveSlots: number;

    afkAutokickInterval: number;

    autoPause: boolean;

    onlyAdminsCanPauseTheGame: boolean;

    autosaveOnlyOnServer: boolean;

    nonBlockingSaving: boolean;

    minimumSegmentSize: number;
    minimumSegmentSizePeerCount: number;
    maximumSegmentSize: number;
    maximumSegmentSizePeerCount: number;
}

interface ApiResponse<T> {
    code: number;
    content: T | null;
    message: string;
};
