import MoveDirectionCommand from "./move-direction-command";

export default class UserStopMoveCommand extends MoveDirectionCommand {
    constructor(direction) {
        super(direction);
        this.moveStopCommand = true;
    }
}
