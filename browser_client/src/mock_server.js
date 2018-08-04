const walkStepPx = 1;

const controlByteActions = {
    1: function (socket) { // goUp
        socket.onmessage({
            data: {
                topic: 'moveY',
                payload: -walkStepPx
            }
        })
    },
    2: function (socket) { // goDown
        socket.onmessage({
            data: {
                topic: 'moveY',
                payload: +walkStepPx
            }
        })
    },
    3: function (socket) { // goLeft
        socket.onmessage({
            data: {
                topic: 'moveX',
                payload: -walkStepPx
            }
        })
    },
    4: function (socket) { // goRight
        socket.onmessage({
            data: {
                topic: 'moveX',
                payload: +walkStepPx
            }
        })
    },
    5: function (socket) { // stopUp
        socket.onmessage({
            data: {
                topic: 'stopMoveY'
            }
        })
    },
    6: function (socket) { // stopDown
        socket.onmessage({
            data: {
                topic: 'stopMoveY'
            }
        })
    },
    7: function (socket) { // stopLeft
        socket.onmessage({
            data: {
                topic: 'stopMoveX'
            }
        })
    },
    8: function (socket) { // stopRight
        socket.onmessage({
            data: {
                topic: 'stopMoveX'
            }
        })
    }
};


WebSocket = function (url) {
    this.url = url;
};

WebSocket.prototype = {
    onmessage: function (event) {
    },
    send: function (data) {
        console.log(data);
        controlByteActions[data[0]](this);
    }
};

module.exports = WebSocket;
