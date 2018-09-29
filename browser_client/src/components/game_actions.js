import KeyboardListener from "./keyboard";
import Server from './server'
import global from '../entities/global'
import Greeting from '../entities/changes/greeting'

const playerSettings = global.playerSettings;

function setPlayerCoords(coordinates) {
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

        setPlayerCoords(greeting.coordinates);
    },
    onMessage: function (response) {
        let message = JSON.parse(response.data);

        if (message.user.positionChange) {
            setPlayerCoords(message.user.coordinates);
        }
    }
};

export default gameActions;
