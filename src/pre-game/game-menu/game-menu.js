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
            $('<div>', {
                'class': 'nickname-input-wrapper',
                html: $('<input>', {
                    id: 'nickname-field-input',
                    'class': 'nickname-field',
                    type: 'text',
                    placeholder: 'nickname',
                    onload: function () {
                        setTimeout(() => $(this).focus(), 200)
                    },
                    keypress: ev => {
                        if (ev.key === "Enter") {
                            $("#play-game-button").click();
                        }
                    }
                })
            }),
            playButton(),
        ]
    })
}
