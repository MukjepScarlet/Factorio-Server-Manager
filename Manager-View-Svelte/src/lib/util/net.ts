namespace NetUtil {
    async function request(
        url: string,
        method: 'GET' | 'POST' | 'DELETE' | 'PUT' = 'GET',
        json: any | undefined = undefined
    ): Promise<any> {
        return fetch('http://localhost/api/' + url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: json && JSON.stringify(json)
        }).then(r => r.json());
    }

    export function createWSClient(path: string): WebSocket {
        return new WebSocket('ws://localhost:5775/ws/' + btoa(encodeURIComponent(path)));
    }

    export async function getAllConfig(): Promise<ApiResponse<ServerConfig[]>> {
        return request('game');
    }

    export async function addConfig(name: string, path: string): Promise<ApiResponse<ServerConfig>> {
        return request('game', 'PUT', { name, path });
    }

    export async function removeConfig(name: string, path: string): Promise<ApiResponse<null>> {
        return request('game', 'DELETE', { name, path });
    }

    export async function updateConfig(config: ServerConfig): Promise<ApiResponse<ServerConfig>> {
        return request('game', 'POST', config);
    }


    export async function status(path: string): Promise<ApiResponse<boolean>> {
        return request('process?path=' + encodeURIComponent(path));
    }

    export async function start(path: string): Promise<ApiResponse<null>> {
        return request('process/start', 'POST', { path });
    }

    export async function stop(path: string): Promise<ApiResponse<null>> {
        return request('process/stop', 'POST', { path });
    }
}

export default NetUtil;
