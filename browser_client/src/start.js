import gameLoop from './client';
import detector from './detector';

window.onload = function () {
    if (detector.webgl) {
        gameLoop();
    } else {
        let warning = detector.getWebGLErrorMessage();
        document.body.appendChild(warning);
    }
};
