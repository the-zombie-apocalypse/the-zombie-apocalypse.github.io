import KeyboardListener from "./keyboard";
import Server from './server'
import global from '../entities/global'
import Greeting from '../entities/changes/greeting'

const playerSettings = global.playerSettings;

// function moveX(delta) {
//     if (playerSettings.isMovingX) return;
//
//     playerSettings.isMovingX = true;
//
//     setTimeout(function moveX() {
//         if (playerSettings.isMovingX) {
//             playerSettings.x = playerSettings.x + delta;
//             setTimeout(moveX, playerSettings.walkStepTime);
//         }
//     }, playerSettings.walkStepTime);
// }
//
// function moveY(delta) {
//     if (playerSettings.isMovingY) return;
//
//     playerSettings.isMovingY = true;
//
//     setTimeout(function moveY() {
//         if (playerSettings.isMovingY) {
//             playerSettings.y = playerSettings.y - delta;
//             setTimeout(moveY, playerSettings.walkStepTime);
//         }
//     }, playerSettings.walkStepTime);
// }
//
// function stopMoveX() {
//     playerSettings.isMovingX = false;
// }
//
// function stopMoveY() {
//     playerSettings.isMovingY = false;
// }
//
// function topicToAction(topic) {
//     switch (topic) {
//         case 'moveX':
//             return moveX;
//         case 'moveY':
//             return moveY;
//         case 'stopMoveX':
//             return stopMoveX;
//         case 'stopMoveY':
//             return stopMoveY;
//         default:
//             return function () {
//             };
//     }
// }

function setPlayerCoords(userEnvironment) {
    let coordinates = userEnvironment.user.coordinates;
    playerSettings.x = coordinates.x;
    playerSettings.y = coordinates.y;
}

const gameActions = {
    connectToServer: function (document) {
        this._document = document;

        this._server = new Server('ws://localhost:8000/conn')
            .onGreeting(this.onGreeting.bind(this))
            .onMessage(this.onMessage.bind(this))
            .connect();
    },
    onGreeting(response) {
        const greeting = new Greeting(response);

        this.keyListener = new KeyboardListener(this._document, this._server);

        console.log(greeting);
    },
    onMessage: function (response) {
        console.log(response);
        // setPlayerCoords(data.userEnvironment);
    }
};

export default gameActions;
