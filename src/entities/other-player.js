import Player from "./player";
import global from "./global";


const visibleArea = global.visibleArea;


export default class OtherPlayer extends Player {
    get onSceneUpdate() {
        return () => {
            if (!visibleArea[this.id]) {
                this.destroy()
            }
        }
    }
}
