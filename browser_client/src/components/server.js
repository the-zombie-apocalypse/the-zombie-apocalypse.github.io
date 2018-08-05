import WebSocket from "../mock_server";
import global from "../entities/global";

const controlBytes = {
    goUp: 1,
    goDown: 2,
    goLeft: 3,
    goRight: 4,
    stopUp: 5,
    stopDown: 6,
    stopLeft: 7,
    stopRight: 8
};

export default class Server {

    constructor(connectionURL, onMessage) {
        this._socket = new WebSocket(connectionURL);
        this._socket.onmessage = onMessage;
    }

    sendByte(value) {
        const buffer = new ArrayBuffer(1);
        buffer[0] = value;
        this._socket.send(buffer);
    }

    sendPlayerMoveUp() {
        global.playerSettings.isMovingY || this.sendByte(controlBytes.goUp);
    }

    sendPlayerMoveDown() {
        global.playerSettings.isMovingY || this.sendByte(controlBytes.goDown);
    }

    sendPlayerMoveLeft() {
        global.playerSettings.isMovingX || this.sendByte(controlBytes.goLeft);
    }

    sendPlayerMoveRight() {
        global.playerSettings.isMovingX || this.sendByte(controlBytes.goRight);
    }

    sendPlayerStopMoveUp() {
        this.sendByte(controlBytes.stopUp);
    }

    sendPlayerStopMoveDown() {
        this.sendByte(controlBytes.stopDown);
    }

    sendPlayerStopMoveLeft() {
        this.sendByte(controlBytes.stopLeft);
    }

    sendPlayerStopMoveRight() {
        this.sendByte(controlBytes.stopRight);
    }
}
