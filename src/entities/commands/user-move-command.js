import MoveDirectionCommand from "./move-direction-command";

export default class UserMoveCommand extends MoveDirectionCommand {
    constructor(direction) {
        super(direction);
        this.moveStartCommand = true;
    }
}
