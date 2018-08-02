const KEY_ESC = 27;
const KEY_ENTER = 13;
const KEY_LEFT = 37;
const KEY_UP = 38;
const KEY_RIGHT = 39;
const KEY_DOWN = 40;

const walkStepTime = 0;

(function () {

    let screenHeight = window.innerHeight - 4; // extra 4px, prevents scroll bar, dunno why
    let screenWidth = window.innerWidth;

    let playerSettings = {
        x: screenWidth / 2,
        y: screenHeight / 2,
        isMovingX: false,
        isMovingY: false,
    };

    let playground = document.getElementById('playground');
    playground.width = screenWidth;
    playground.height = screenHeight;

    let context = playground.getContext('2d');

    function limit(min, max, value) {
        return Math.min(max, Math.max(min, value));
    }

    let ws = new WebSocket('ws://localhost:8080/ws');

    const topicToAction = {
        moveX: function (delta) {
            if (playerSettings.isMovingX) return;

            playerSettings.isMovingX = true;

            setTimeout(function moveX() {
                if (playerSettings.isMovingX) {
                    playerSettings.x = limit(0, screenWidth, playerSettings.x + delta);
                    setTimeout(moveX, walkStepTime);
                }
            }, walkStepTime);
        },
        moveY: function (delta) {
            if (playerSettings.isMovingY) return;

            playerSettings.isMovingY = true;

            setTimeout(function moveY() {
                if (playerSettings.isMovingY) {
                    playerSettings.y = limit(0, screenHeight, playerSettings.y + delta);
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

        context.clearRect(0, 0, screenWidth, screenHeight);
        context.fillStyle = '#cdd0d6';
        context.fillRect(0, 0, screenWidth, screenHeight);
        context.beginPath();
        context.arc(playerSettings.x, playerSettings.y, 10, 0, 2 * Math.PI);
        context.stroke();

        requestAnimationFrame(gameLoop);
    })();

    window.addEventListener('resize', resize);

    function resize() {
        screenWidth = playground.width = window.innerWidth;
        screenHeight = playground.height = window.innerHeight - 4;
    }

})();
