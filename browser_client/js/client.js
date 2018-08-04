window.onload = function start() {
    function main() {

        const KEY_ESC = 27;
        const KEY_ENTER = 13;
        const KEY_LEFT = 37;
        const KEY_UP = 38;
        const KEY_RIGHT = 39;
        const KEY_DOWN = 40;

        const walkStepTime = 0; // move to player settings

        const VIEW_ANGLE = 35;
        const NEAR = 0.1;
        const FAR = 5000;

        let screenWidth = window.innerWidth;
        let screenHeight = window.innerHeight;

        function getHalfScreenWidth() {
            return screenWidth * 0.5;
        }

        function getHalfScreenHeight() {
            return screenHeight * 0.5;
        }

        let halfScreenWidth = getHalfScreenWidth();
        let halfScreenHeight = getHalfScreenHeight();

        let renderer = new THREE.WebGLRenderer({antialias: true});
        renderer.setSize(screenWidth, screenHeight);

        document.body.appendChild(renderer.domElement);

        let playerSettings = {
            x: 0,
            y: 0,
            isMovingX: false,
            isMovingY: false,
        };

        let playground = renderer.domElement;

        let geometry = new THREE.BoxGeometry(30, 30, 30);
        let material = new THREE.MeshLambertMaterial({color: 0x00ff00});

        let cube = new THREE.Mesh(geometry, material);
        let scene = new THREE.Scene();
        scene.add(cube);

        let axesHelper = new THREE.AxesHelper(150);
        scene.add(axesHelper);

        let camera = new THREE.PerspectiveCamera(VIEW_ANGLE, screenWidth / screenHeight, NEAR, FAR);
        camera.position.z = 1500;
        camera.position.x = 150;
        camera.position.y = 50;

        renderer.setClearColor(0xdddddd, 1);

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
                        playerSettings.x = limit(-halfScreenWidth, halfScreenWidth, playerSettings.x + delta);
                        setTimeout(moveX, walkStepTime);
                    }
                }, walkStepTime);
            },
            moveY: function (delta) {
                if (playerSettings.isMovingY) return;

                playerSettings.isMovingY = true;

                setTimeout(function moveY() {
                    if (playerSettings.isMovingY) {
                        playerSettings.y = limit(-halfScreenHeight, halfScreenHeight, playerSettings.y + delta);
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
            renderer.render(scene, camera);

            cube.position.x = playerSettings.x;
            cube.position.y = playerSettings.y;

            cube.rotation.x += 0.01;
            cube.rotation.y += 0.01;

            requestAnimationFrame(gameLoop);
        })();

        window.addEventListener('resize', resize);

        function resize() {
            screenWidth = playground.width = window.innerWidth;
            screenHeight = playground.height = window.innerHeight;

            halfScreenWidth = getHalfScreenWidth();
            halfScreenHeight = getHalfScreenHeight();

            renderer.setSize(screenWidth, screenHeight);
            camera.aspect = screenWidth / screenHeight;
            camera.updateProjectionMatrix();
        }
    }

    if (Detector.webgl) {
        // Initiate function or other initializations here
        main();
    } else {
        var warning = Detector.getWebGLErrorMessage();
        document.getElementById('container').appendChild(warning);
    }
};
