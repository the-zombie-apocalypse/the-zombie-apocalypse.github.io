import global from './../../entities/global'
import ChatCommunicator from './chatCommunicator'

let chatMessageConsumer = () => {
};

function buildChatMessage(nickname, text) {
    const now = new Date();
    let hours = now.getHours();
    if (hours <= 9) {
        hours = `0${hours}`
    }
    let minutes = now.getMinutes();
    if (minutes <= 9) {
        minutes = `0${minutes}`
    }
    const time = `${hours}:${minutes}`;
    return `[${time}] ${nickname}: ${text}`;
}

class Chat {
    constructor(settings, chatCommunicator) {
        this.settings = settings;
        this.chatCommunicator = chatCommunicator;
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
        const sendMessage = () => {
            const messageText = message.value || "";
            if (!messageText) {
                return
            }
            message.value = "";
            this.chatCommunicator.sendMessage(messageText);
        }
        message.addEventListener('keypress', ev => {
            if (ev.key === "Enter") {
                sendMessage();
                message.blur();
            }
        })
        messageRow.appendChild(message);
        const sendMessageButton = document.createElement("button");
        sendMessageButton.className = "chat-send-message";
        sendMessageButton.textContent = "Send";
        chatMessageConsumer = (nickname, message) => {
            const messageRow = document.createElement("div");
            messageRow.className = "chat-messages-board-message-row";
            messageRow.textContent = buildChatMessage(nickname, message);
            messagesBoard.appendChild(messageRow);
        }
        sendMessageButton.addEventListener("click", ev => {
            sendMessage();
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
    constructor(settings, server) {
        this.settings = settings;
        this.server = server;
    }

    init() {
        const chat = new Chat(this.settings, new ChatCommunicator(this.settings, this.server));
        document.body.appendChild(chat.build());
        chat.init();
    }
}

export const chatMessagesRepository = {
    addMessage(nickname, message) {
        chatMessageConsumer && chatMessageConsumer(nickname, message)
    }
}
