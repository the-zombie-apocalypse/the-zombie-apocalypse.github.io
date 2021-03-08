import global from './../../entities/global'

class Chat {
    constructor(settings) {
        this.settings = settings;
    }

    init() {

    }

    build() {
        const chatFrame = document.createElement("div");
        chatFrame.className = "chat-frame";
        const messagesBoard = document.createElement("div");
        messagesBoard.className = "chat-messages-board";
        chatFrame.appendChild(messagesBoard);
        const messageRow = document.createElement("div");
        messageRow.className = "chat-message-row";
        chatFrame.appendChild(messageRow);
        const message = document.createElement("input");
        message.className = "chat-message";
        message.type = "text";
        message.addEventListener('focusin', ev => {
            global.moveControlsBlocked = true;
        })
        message.addEventListener('focusout', ev => {
            global.moveControlsBlocked = false;
        })
        messageRow.appendChild(message);
        const sendMessageButton = document.createElement("button");
        sendMessageButton.className = "chat-send-message";
        sendMessageButton.textContent = "Send";
        sendMessageButton.addEventListener("click", ev => {
            const messageRow = document.createElement("div");
            messageRow.className = "chat-messages-board-message-row";
            const now = new Date();
            const time = `${now.getHours()}:${now.getMinutes()}`;
            const messageText = message.value || "";
            messageRow.textContent = `[${time}] ${this.settings.userNickname}: ${messageText}`;
            messagesBoard.appendChild(messageRow);
            message.value = "";
        })
        messageRow.appendChild(sendMessageButton);
        return chatFrame;
    }
}

export class ChatSetting {
    constructor({userId, userNickname}) {
        this.userId = userId;
        this.userNickname = userNickname;
    }
}

export default class ChatPlugin {

    constructor(settings) {
        this.settings = settings;
    }

    init() {
        const chat = new Chat(this.settings);
        document.body.appendChild(chat.build())
        // $('body').append();
        chat.init();
    }
}
