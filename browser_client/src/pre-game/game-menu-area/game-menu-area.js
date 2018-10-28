import './game-menu-area.css';

import $ from "jquery";
import gameMenu from "../game-menu/game-menu";

export default function () {
    return $('<div>', {
        'class': 'game-menu-area',
        html: gameMenu()
    });
}
