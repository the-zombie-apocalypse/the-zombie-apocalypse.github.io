import directions from './directions'
import UserMoveCommand from "../entities/commands/user-move-command";
import UserStopMoveCommand from "../entities/commands/user-stop-move-command";

const serverCommands = {
    goUp: new UserMoveCommand(directions.up).toString(),
    goDown: new UserMoveCommand(directions.down).toString(),
    goWest: new UserMoveCommand(directions.west).toString(),
    goEast: new UserMoveCommand(directions.east).toString(),
    goNorth: new UserMoveCommand(directions.north).toString(),
    goSouth: new UserMoveCommand(directions.south).toString(),
    stopUp: new UserStopMoveCommand(directions.up).toString(),
    stopDown: new UserStopMoveCommand(directions.down).toString(),
    stopWest: new UserStopMoveCommand(directions.west).toString(),
    stopEast: new UserStopMoveCommand(directions.east).toString(),
    stopNorth: new UserStopMoveCommand(directions.north).toString(),
    stopSouth: new UserStopMoveCommand(directions.south).toString(),
};

export default class GameServer {

    constructor(connectionURL) {
        this.connectionURL = connectionURL;
    }

    onGreeting(onGreeting) {
        this._onGreeting = onGreeting;
        return this
    }

    onMessage(onMessage) {
        this._onMessage = onMessage;
        return this
    }

    onClose(onClose) {
        this._onClose = onClose;
        return this;
    }

    handleMessage(message) {
        this._onMessage(message)
    }

    connect() {

        let handle = (message) => {
            let data = JSON.parse(message.data);

            if (data.greeting) {
                this._onGreeting(data);
                handle = this.handleMessage.bind(this);
            } else {
                console.error(data);
                throw Error('There is no valid greeting from server');
            }
        };

        function getHandler(message) {
            return handle(message);
        }

        this._socket = new WebSocket(this.connectionURL);

        window.onbeforeunload = () => {
            this._socket.close();
        };

        this._socket.onmessage = getHandler;
        this._socket.onerror = console.log;
        this._socket.onclose = () => {
            if (this._onClose) this._onClose();
            alert("Connection closed!"); // todo: create pre-game mode and return user to it here
        };

        return this
    }

    sendPlayerMoveUp() {
        this._socket.send(serverCommands.goUp);
    }

    sendPlayerMoveDown() {
        this._socket.send(serverCommands.goDown);
    }

    sendPlayerMoveNorth() {
        this._socket.send(serverCommands.goNorth);
    }

    sendPlayerMoveSouth() {
        this._socket.send(serverCommands.goSouth);
    }

    sendPlayerMoveWest() {
        this._socket.send(serverCommands.goWest);
    }

    sendPlayerMoveEast() {
        this._socket.send(serverCommands.goEast);
    }

    sendPlayerStopMoveUp() {
        this._socket.send(serverCommands.stopUp);
    }

    sendPlayerStopMoveDown() {
        this._socket.send(serverCommands.stopDown);
    }

    sendPlayerStopMoveNorth() {
        this._socket.send(serverCommands.stopNorth);
    }

    sendPlayerStopMoveSouth() {
        this._socket.send(serverCommands.stopSouth);
    }

    sendPlayerStopMoveWest() {
        this._socket.send(serverCommands.stopWest);
    }

    sendPlayerStopMoveEast() {
        this._socket.send(serverCommands.stopEast);
    }
}
