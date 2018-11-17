import './game-menu.css'

import $ from 'jquery';
import playButton from "../play-button/play-button"

export default function () {
    return $('<div>', {
        'class': 'game-menu',
        html: [
            $('<div>', {
                'class': 'game-title',
                text: 'Zorg'
            }),
            playButton(),
        ]
    })
}
