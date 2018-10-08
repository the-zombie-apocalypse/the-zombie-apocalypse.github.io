const ARROW_LEFT = 'ArrowLeft';
const ARROW_UP = 'ArrowUp';
const ARROW_RIGHT = 'ArrowRight';
const ARROW_DOWN = 'ArrowDown';
const KEY_A = 'a';
const KEY_W = 'w';
const KEY_D = 'd';
const KEY_S = 's';
const BIG_KEY_A = 'A';
const BIG_KEY_W = 'W';
const BIG_KEY_D = 'D';
const BIG_KEY_S = 'S';

function getKeyDownAction(key, server) {
    switch (key) {
        case ARROW_UP:
        case KEY_W:
        case BIG_KEY_W:
            return server.sendPlayerMoveNorth;
        case ARROW_DOWN:
        case KEY_S:
        case BIG_KEY_S:
            return server.sendPlayerMoveSouth;
        case ARROW_LEFT:
        case KEY_A:
        case BIG_KEY_A:
            return server.sendPlayerMoveWest;
        case ARROW_RIGHT:
        case KEY_D:
        case BIG_KEY_D:
            return server.sendPlayerMoveEast;
        default:
            return new Function();
    }
}

function getKeyUpAction(key, server) {
    switch (key) {
        case ARROW_UP:
        case KEY_W:
        case BIG_KEY_W:
            return server.sendPlayerStopMoveNorth;
        case ARROW_DOWN:
        case KEY_S:
        case BIG_KEY_S:
            return server.sendPlayerStopMoveSouth;
        case ARROW_LEFT:
        case KEY_A:
        case BIG_KEY_A:
            return server.sendPlayerStopMoveWest;
        case ARROW_RIGHT:
        case KEY_D:
        case BIG_KEY_D:
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

    _onKeyDown({key}) {
        if (keysDown[key]) return;
        keysDown[key] = key;

        getKeyDownAction(key, this._server).call(this._server);
    }

    _onKeyUp({key}) {
        delete keysDown[key];
        getKeyUpAction(key, this._server).call(this._server);
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
