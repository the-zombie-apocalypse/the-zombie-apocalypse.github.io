import './play-button.css'

import $ from "jquery";
import gameLoop from "../../client";

export default function () {
    return $('<button>', {
        text: 'Play!',
        'class': 'play-button',
        click: function () {
            $('.game-menu-area').remove();
            gameLoop();
        }
    })
}
