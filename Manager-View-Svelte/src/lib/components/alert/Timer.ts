let timerId: number | null = null;

self.onmessage = (event: MessageEvent<number>) => {
    const interval = event.data;

    if (interval > 0) {
        // 如果接收到的 interval 是正数，则设置一个新的定时器
        if (timerId) {
            clearInterval(timerId);
        }
        timerId = setInterval(() => {
            postMessage(undefined);
        }, interval);
    } else {
        // 如果接收到的 interval 不是正数，则停止计时器
        if (timerId) {
            clearInterval(timerId);
            timerId = null;
        }
    }
};
