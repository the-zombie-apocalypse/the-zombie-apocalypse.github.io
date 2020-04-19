const W_KEY_CODE = 87;
const A_KEY_CODE = 65;
const S_KEY_CODE = 83;
const D_KEY_CODE = 68;
const ARROW_LEFT_KEY_CODE = 37;
const ARROW_UP_KEY_CODE = 38;
const ARROW_RIGHT_KEY_CODE = 39;
const ARROW_DOWN_KEY_CODE = 40;

function getKeyDownAction(keyCode, server) {
    switch (keyCode) {
        case ARROW_UP_KEY_CODE:
        case W_KEY_CODE:
            return server.sendPlayerMoveNorth;
        case ARROW_DOWN_KEY_CODE:
        case S_KEY_CODE:
            return server.sendPlayerMoveSouth;
        case ARROW_LEFT_KEY_CODE:
        case A_KEY_CODE:
            return server.sendPlayerMoveWest;
        case ARROW_RIGHT_KEY_CODE:
        case D_KEY_CODE:
            return server.sendPlayerMoveEast;
        default:
            return new Function();
    }
}

function getKeyUpAction(key, server) {
    switch (key) {
        case ARROW_UP_KEY_CODE:
        case W_KEY_CODE:
            return server.sendPlayerStopMoveNorth;
        case ARROW_DOWN_KEY_CODE:
        case S_KEY_CODE:
            return server.sendPlayerStopMoveSouth;
        case ARROW_LEFT_KEY_CODE:
        case A_KEY_CODE:
            return server.sendPlayerStopMoveWest;
        case ARROW_RIGHT_KEY_CODE:
        case D_KEY_CODE:
            return server.sendPlayerStopMoveEast;
        default:
            return new Function();
    }
}

const keysDown = {};

export default class KeyboardListener {

    constructor(document, server) {
        this._document = document;
        this._server = server;

        this.initKeyDown();
        this.initKeyUp();
    }

    _onKeyDown({keyCode}) {
        if (keysDown[keyCode]) return;
        keysDown[keyCode] = keyCode;

        getKeyDownAction(keyCode, this._server).call(this._server);
    }

    _onKeyUp({keyCode}) {
        delete keysDown[keyCode];
        getKeyUpAction(keyCode, this._server).call(this._server);
    }

    initKeyDown() {
        this._document.addEventListener('keydown', this._onKeyDown.bind(this));
    }

    initKeyUp() {
        this._document.addEventListener('keyup', this._onKeyUp.bind(this));
    }

    shutDown() {
        this._document.removeEventListener('keydown', this._onKeyDown.bind(this));
        this._document.removeEventListener('keyup', this._onKeyUp.bind(this));
    }
}
