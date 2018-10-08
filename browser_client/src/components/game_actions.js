import KeyboardListener from "./keyboard";
import GameServer from './gameServer'
import global from '../entities/global'
import Greeting from '../entities/changes/greeting'
import objectsWarehouse from './objects-warehouse';

const playerSettings = global.playerSettings;

function spawnNewUser(user) {
    objectsWarehouse.spawnNewUser(user.id, user.coordinates);
}

function processUserChange(userChange) {
    objectsWarehouse.setPosition(userChange.id, userChange.coordinates);
}

const gameActions = {
    connectToServer: function (document) {
        this._document = document;

        this._server = new GameServer('ws://localhost:8000/conn')
            .onGreeting(this.onGreeting.bind(this))
            .onMessage(this.onMessage.bind(this))
            .onClose(this.onClose.bind(this))
            .connect();
    },
    onGreeting(response) {
        const greeting = new Greeting(response);
        this.keyListener = new KeyboardListener(this._document, this._server);
        playerSettings.id = greeting.id;

        processUserChange(greeting);

        greeting.users.forEach(spawnNewUser);
    },
    onMessage: function (response) {
        let message = JSON.parse(response.data);
        console.log(message);
        const userChange = message.user;

        if (userChange) {
            if (message.greeting) spawnNewUser(userChange);
            if (userChange.positionChange) processUserChange(userChange);
        }
    },
    onClose() {
        this.keyListener.shutDown();
    }
};

export default gameActions;
