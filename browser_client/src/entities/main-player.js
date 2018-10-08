import Player from "./player";
import global from "./global";


const playerSettings = global.playerSettings;


export default class MainPlayer extends Player {

    get onSceneUpdate() {
        return () => {
            this.position = {
                x: playerSettings.x,
                y: playerSettings.y,
            };
        }
    }
}
