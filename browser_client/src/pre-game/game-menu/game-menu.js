import './game-menu.css'

import $ from 'jquery';
import gameLoop from "../../client";

export default function () {
    return $('<div>', {
        'class': 'game-menu',
        html: [$('<div>', {
            'class': 'game-title',
            text: 'Zorg'
        }), $('<button>', {
            text: 'Play!',
            click: function () {
                $('.game-menu-area').remove();
                gameLoop();
            }
        })]
    })
}
