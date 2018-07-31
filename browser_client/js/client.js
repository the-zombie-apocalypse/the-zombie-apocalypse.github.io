const KEY_ESC = 27;
const KEY_ENTER = 13;
const KEY_LEFT = 37;
const KEY_UP = 38;
const KEY_RIGHT = 39;
const KEY_DOWN = 40;

(function () {

    let screenHeight = window.innerHeight - 4; // extra 4px, prevents scroll bar, dunno why
    let screenWidth = window.innerWidth;

    let playerSettings = {
        x: screenWidth / 2,
        y: screenHeight / 2
    };

    let playground = document.getElementById('playground');
    playground.width = screenWidth;
    playground.height = screenHeight;

    let context = playground.getContext('2d');

    function limit(min, max, value) {
        return Math.min(max, Math.max(min, value));
    }

    function goDown() {
        playerSettings.y = limit(0, screenHeight, playerSettings.y + 5);
    }

    function goUp() {
        playerSettings.y = limit(0, screenHeight, playerSettings.y - 5);
    }

    function goLeft() {
        playerSettings.x = limit(0, screenWidth, playerSettings.x - 5);
    }

    function goRight() {
        playerSettings.x = limit(0, screenWidth, playerSettings.x + 5);
    }

    document.addEventListener('keydown', function (e) {
        let key = e.which || e.keyCode;

        switch (key) {
            case KEY_DOWN:
                return goDown();
            case KEY_UP:
                return goUp();
            case KEY_LEFT:
                return goLeft();
            case KEY_RIGHT:
                return goRight();
        }
    });

    (function gameLoop() {

        context.clearRect(0, 0, screenWidth, screenHeight);
        context.fillStyle = '#cdd0d6';
        context.fillRect(0, 0, screenWidth, screenHeight);
        context.beginPath();
        context.arc(playerSettings.x, playerSettings.y, 12, 0, 2 * Math.PI);
        context.stroke();

        requestAnimationFrame(gameLoop);
    })();

    window.addEventListener('resize', resize);

    function resize() {
        screenWidth = playground.width = window.innerWidth;
        screenHeight = playground.height = window.innerHeight - 4;
    }

})();
