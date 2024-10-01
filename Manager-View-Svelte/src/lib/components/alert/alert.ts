import { writable } from 'svelte/store';

// 消息

export type AlertType = 'alert-info' | 'alert-error' | 'alert-warning' | 'alert-success' | undefined;

export class Alert {
    public type?: AlertType;
    public content: string;
    public time: number;

    public ttl: number;
    public createTime: number;

    constructor(content: string, type: AlertType = undefined, time: number = 3000) {
        this.content = content;
        this.type = type;
        this.ttl = this.time = time;
        this.createTime = +new Date();
    }
}

export const alertQueue = writable<Alert[]>([]);

export function info(content: string, time: number = 3000) {
    const alert = new Alert(content, 'alert-info', time);
    updateAlertQueue(alert);
}

export function error(content: string, time: number = 3000) {
    const alert = new Alert(content, 'alert-error', time);
    updateAlertQueue(alert);
}

export function warning(content: string, time: number = 3000) {
    const alert = new Alert(content, 'alert-warning', time);
    updateAlertQueue(alert);
}

export function success(content: string, time: number = 3000) {
    const alert = new Alert(content, 'alert-success', time);
    updateAlertQueue(alert);
}

function updateAlertQueue(alert: Alert) {
    alertQueue.update((queue) => [...queue, alert]);
}
