import detector from './detector';
import StartGameMenu from "./pre-game/start-game-menu";

window.onload = function () {
    if (!detector.webgl) {
        let warning = detector.getWebGLErrorMessage();
        document.body.appendChild(warning);
        return;
    }

    StartGameMenu.build();
};
