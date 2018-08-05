import {AxesHelper, Mesh, MeshBasicMaterial, PlaneGeometry} from 'three'
import './style.css'
import Graphon from './graphics/graphon'
import Player from './entities/player'
import gameActions from './components/game_actions'

export default function gameLoop() {

    gameActions.connectToServer();
    gameActions.initKeyboardListener(window.document);

    const graphon = new Graphon(window);
    graphon.initGraph();

    const player = new Player();
    graphon.addDynamic(player);

    const axesHelper = new AxesHelper(150);
    graphon.addStatic(axesHelper);

    const plane = new Mesh(
        new PlaneGeometry(50, 50, 50, 50),
        new MeshBasicMaterial({color: 0x393839, wireframe: true})
    );
    // plane.rotateX(Math.PI/2);
    graphon.addStatic(plane);

    (function gameLoop() {
        graphon.update();
        requestAnimationFrame(gameLoop);
    })();

}
