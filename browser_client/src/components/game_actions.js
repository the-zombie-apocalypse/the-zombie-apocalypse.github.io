import KeyboardListener from "./keyboard";
import Server from './server'
import global from '../entities/global'

const playerSettings = global.playerSettings;

function moveX(delta) {
    if (playerSettings.isMovingX) return;

    playerSettings.isMovingX = true;

    setTimeout(function moveX() {
        if (playerSettings.isMovingX) {
            playerSettings.x = playerSettings.x + delta;
            setTimeout(moveX, playerSettings.walkStepTime);
        }
    }, playerSettings.walkStepTime);
}

function moveY(delta) {
    if (playerSettings.isMovingY) return;

    playerSettings.isMovingY = true;

    setTimeout(function moveY() {
        if (playerSettings.isMovingY) {
            playerSettings.y = playerSettings.y - delta;
            setTimeout(moveY, playerSettings.walkStepTime);
        }
    }, playerSettings.walkStepTime);
}

function stopMoveX() {
    playerSettings.isMovingX = false;
}

function stopMoveY() {
    playerSettings.isMovingY = false;
}

function topicToAction(topic) {
    switch (topic) {
        case 'moveX':
            return moveX;
        case 'moveY':
            return moveY;
        case 'stopMoveX':
            return stopMoveX;
        case 'stopMoveY':
            return stopMoveY;
        default:
            return function () {
            };
    }
}

const gameActions = {
    connectToServer: function () {
        this._server = new Server('ws://localhost:8080/ws', this.onMessage);
    },
    initKeyboardListener: function (document) {
        new KeyboardListener(document, this._server).init();
    },
    onMessage: function (message) {
        topicToAction(message.data.topic)(message.data.payload);
    }
};

export default gameActions
