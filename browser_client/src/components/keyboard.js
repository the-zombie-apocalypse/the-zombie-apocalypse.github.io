const ARROW_LEFT = 'ArrowLeft';
const ARROW_UP = 'ArrowUp';
const ARROW_RIGHT = 'ArrowRight';
const ARROW_DOWN = 'ArrowDown';
const KEY_A = 'a';
const KEY_W = 'w';
const KEY_D = 'd';
const KEY_S = 's';

function getKeyDownAction(key, server) {
    switch (key) {
        case ARROW_UP:
        case KEY_W:
            return server.sendPlayerMoveUp;
        case ARROW_DOWN:
        case KEY_S:
            return server.sendPlayerMoveDown;
        case ARROW_LEFT:
        case KEY_A:
            return server.sendPlayerMoveLeft;
        case ARROW_RIGHT:
        case KEY_D:
            return server.sendPlayerMoveRight;
        default:
            return function () {
            }
    }
}

function getKeyUpAction(key, server) {
    switch (key) {
        case ARROW_UP:
        case KEY_W:
            return server.sendPlayerStopMoveUp;
        case ARROW_DOWN:
        case KEY_S:
            return server.sendPlayerStopMoveDown;
        case ARROW_LEFT:
        case KEY_A:
            return server.sendPlayerStopMoveLeft;
        case ARROW_RIGHT:
        case KEY_D:
            return server.sendPlayerStopMoveRight;
        default:
            return function () {
            }
    }
}

export default class KeyboardListener {
    constructor(document, server) {
        this._document = document;
        this._server = server;
    }

    init() {
        this.initKeyDown();
        this.initKeyUp();
    }

    initKeyDown() {
        this._document.addEventListener('keydown', (e) => {
            getKeyDownAction(e.key, this._server).call(this._server);
        });
    }

    initKeyUp() {
        this._document.addEventListener('keyup', (e) => {
            getKeyUpAction(e.key, this._server).call(this._server);
        });
    }
}
