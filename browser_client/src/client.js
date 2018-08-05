import {AxesHelper} from 'three'
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

    (function gameLoop() {
        graphon.update();
        requestAnimationFrame(gameLoop);
    })();

}
