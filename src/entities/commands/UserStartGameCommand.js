import Command from "./command";

export default class UserStartGameCommand extends Command {
    constructor({nickname}) {
        super();
        this.startGameCommand = true;
        this.nickname = nickname;
    }
}
