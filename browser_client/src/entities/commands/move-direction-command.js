import Command from "./command";

export default class MoveDirectionCommand extends Command {
    constructor(direction) {
        super();
        this.direction = direction;
    }
}
