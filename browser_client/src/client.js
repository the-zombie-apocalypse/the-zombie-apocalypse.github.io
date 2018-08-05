import {AxesHelper, BoxGeometry, Mesh, MeshLambertMaterial} from 'three'
import WebSocket from './mock_server'
import './style.css'
import Graphon from './graphics/graphon'
import ScreenSizer from './components/screen_sizer'

export default function gameLoop() {
    const KEY_LEFT = 37;
    const KEY_UP = 38;
    const KEY_RIGHT = 39;
    const KEY_DOWN = 40;

    const walkStepTime = 0; // move to player settings

    const sizer = ScreenSizer.getInstance(window);
    const graphon = new Graphon(window, sizer);
    graphon.initGraph();

    let playerSettings = {
        x: 0,
        y: 0,
        isMovingX: false,
        isMovingY: false,
    };

    let geometry = new BoxGeometry(30, 30, 30);
    let material = new MeshLambertMaterial({color: 0x00ff00});

    let cube = new Mesh(geometry, material);
    graphon.addToScene(cube);

    let axesHelper = new AxesHelper(150);
    graphon.addToScene(axesHelper);

    function limit(min, max, value) {
        return Math.min(max, Math.max(min, value));
    }

    const ws = new WebSocket('ws://localhost:8080/ws');

    const topicToAction = {
        moveX: function (delta) {
            if (playerSettings.isMovingX) return;

            playerSettings.isMovingX = true;

            setTimeout(function moveX() {
                if (playerSettings.isMovingX) {
                    playerSettings.x = limit(-sizer.halfScreenWidth, sizer.halfScreenWidth, playerSettings.x + delta);
                    setTimeout(moveX, walkStepTime);
                }
            }, walkStepTime);
        },
        moveY: function (delta) {
            if (playerSettings.isMovingY) return;

            playerSettings.isMovingY = true;

            setTimeout(function moveY() {
                if (playerSettings.isMovingY) {
                    playerSettings.y = limit(-sizer.halfScreenHeight, sizer.halfScreenHeight, playerSettings.y - delta);
                    setTimeout(moveY, walkStepTime);
                }
            }, walkStepTime);
        },
        stopMoveX: function () {
            playerSettings.isMovingX = false;
        },
        stopMoveY: function () {
            playerSettings.isMovingY = false;
        }
    };

    ws.onmessage = function (event) {
        topicToAction[event.data.topic](event.data.payload);
    };

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

    function sendByte(value) {
        const buffer = new ArrayBuffer(1);
        buffer[0] = value;
        ws.send(buffer);
    }

    function sendUp() {
        playerSettings.isMovingY || sendByte(controlBytes.goUp);
    }

    function sendDown() {
        playerSettings.isMovingY || sendByte(controlBytes.goDown);
    }

    function sendLeft() {
        playerSettings.isMovingX || sendByte(controlBytes.goLeft);
    }

    function sendRight() {
        playerSettings.isMovingX || sendByte(controlBytes.goRight);
    }

    function sendStopUp() {
        sendByte(controlBytes.stopUp);
    }

    function sendStopDown() {
        sendByte(controlBytes.stopDown);
    }

    function sendStopLeft() {
        sendByte(controlBytes.stopLeft);
    }

    function sendStopRight() {
        sendByte(controlBytes.stopRight);
    }

    const movingKeysHolder = {
        moveStart: {
            38: sendUp,
            40: sendDown,
            37: sendLeft,
            39: sendRight,
        },
        moveStop: {
            38: sendStopUp,
            40: sendStopDown,
            37: sendStopLeft,
            39: sendStopRight,
        },
        isMoveKey: function (key) {
            return (key === KEY_DOWN)
                || (key === KEY_UP)
                || (key === KEY_LEFT)
                || (key === KEY_RIGHT)
        },
        getMove: function (key) {
            return this.moveStart[key];
        },
        getMoveStop: function (key) {
            return this.moveStop[key];
        }
    };

    document.addEventListener('keydown', function (e) {
        let key = e.which || e.keyCode;
        if (!movingKeysHolder.isMoveKey(key)) return;
        movingKeysHolder.getMove(key)();
    });

    document.addEventListener('keyup', function (e) {
        let key = e.which || e.keyCode;
        if (!movingKeysHolder.isMoveKey(key)) return;
        movingKeysHolder.getMoveStop(key)();
    });

    (function gameLoop() {
        graphon.update();

        cube.position.x = playerSettings.x;
        cube.position.y = playerSettings.y;

        cube.rotation.x += 0.01;
        cube.rotation.y += 0.01;

        requestAnimationFrame(gameLoop);
    })();

}
