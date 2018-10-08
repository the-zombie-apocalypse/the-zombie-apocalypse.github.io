import global from "../entities/global";
import OtherPlayer from "../entities/other-player";
import graphon from "../graphics/graphon";


const playerSettings = global.playerSettings;
const visibleArea = global.visibleArea;


function setPlayerCoords(coordinates) {
    playerSettings.x = coordinates.x;
    playerSettings.y = coordinates.y;
}

export default {

    setPosition(id, coordinates) {
        if (playerSettings.id === id) {
            setPlayerCoords(coordinates);
            return;
        }

        (visibleArea[id] = visibleArea[id] || {}).position = coordinates;
    },

    spawnNewUser(id, coordinates) {
        const player = new OtherPlayer(id, coordinates);

        visibleArea[id] = player;

        graphon.addDynamic(player);
    }
}
