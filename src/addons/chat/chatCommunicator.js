import UserSendChatMessageCommand from "../../entities/commands/UserSendChatMessageCommand";

export default class ChatCommunicator {
    constructor(settings, server) {
        this.settings = settings;
        this.server = server;
    }

    sendMessage(message) {
        this.server.send(new UserSendChatMessageCommand(message))
    }
}