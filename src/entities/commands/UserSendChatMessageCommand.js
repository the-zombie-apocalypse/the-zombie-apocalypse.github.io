import Command from "./command";

export default class UserSendChatMessageCommand extends Command {
    constructor(message) {
        super();
        this.chatMessage = message;
    }
}
