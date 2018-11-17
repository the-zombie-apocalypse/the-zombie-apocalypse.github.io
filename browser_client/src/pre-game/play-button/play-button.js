import './play-button.css'

import $ from "jquery";
import gameLoop from "../../client";

export default function () {
    return $('<button>', {
        text: 'Play!',
        'class': 'play-button',
        click: function () {
            const $gameMenu = $('.game-menu-area');
            $gameMenu.hide();

            try {
                gameLoop();
            } catch (e) {
                $gameMenu.show();
            }
        }
    })
}
