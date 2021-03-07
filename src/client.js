import {AxesHelper, Mesh, MeshBasicMaterial, PlaneGeometry} from 'three'
import './style.css'
import graphon from './graphics/graphon'
import gameActions from './components/game_actions'
import MainPlayer from "./entities/main-player";

export default function gameLoop(nickname) {

    gameActions.connectToServer(window.document, nickname);

    graphon.initGraph();

    // todo: move player creation after greeting from server
    const player = new MainPlayer(null, null, nickname);
    graphon.addDynamic(player);

    const axesHelper = new AxesHelper(150);
    graphon.addStatic(axesHelper);

    const plane = new Mesh(
        new PlaneGeometry(150, 150, 150, 150),
        new MeshBasicMaterial({color: 0x848384, wireframe: true})
    );
    graphon.addStatic(plane);

    (function gameLoop() {
        graphon.update();
        requestAnimationFrame(gameLoop);
    })();

}
