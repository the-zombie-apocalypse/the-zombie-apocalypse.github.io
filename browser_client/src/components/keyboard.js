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
            return server.sendPlayerMoveNorth;
        case ARROW_DOWN:
        case KEY_S:
            return server.sendPlayerMoveSouth;
        case ARROW_LEFT:
        case KEY_A:
            return server.sendPlayerMoveWest;
        case ARROW_RIGHT:
        case KEY_D:
            return server.sendPlayerMoveEast;
        default:
            return new Function();
    }
}

function getKeyUpAction(key, server) {
    switch (key) {
        case ARROW_UP:
        case KEY_W:
            return server.sendPlayerStopMoveNorth;
        case ARROW_DOWN:
        case KEY_S:
            return server.sendPlayerStopMoveSouth;
        case ARROW_LEFT:
        case KEY_A:
            return server.sendPlayerStopMoveWest;
        case ARROW_RIGHT:
        case KEY_D:
            return server.sendPlayerStopMoveEast;
        default:
            return new Function();
    }
}

export default class KeyboardListener {

    constructor(document, server) {
        this._document = document;
        this._server = server;

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
