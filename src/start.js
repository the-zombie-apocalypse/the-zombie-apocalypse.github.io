import detector from './detector';
import gameMenu from "./pre-game/start-game-menu";
import $ from "jquery";

window.onload = function () {
    if (!detector.webgl) {
        let warning = detector.getWebGLErrorMessage();
        document.body.appendChild(warning);
        return;
    }

    $('body').append(gameMenu());
};
