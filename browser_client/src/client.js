import {AxesHelper, Mesh, MeshBasicMaterial, PlaneGeometry} from 'three'
import './style.css'
import Graphon from './graphics/graphon'
import Player from './entities/player'
import gameActions from './components/game_actions'

export default function gameLoop() {

    gameActions.connectToServer(window.document);

    const graphon = new Graphon(window);
    graphon.initGraph();

    const player = new Player();
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
