import $ from "jquery";
import gameMenuArea from "./game-menu-area/game-menu-area"

let menu;

function getMenu() {
    return menu || (menu = gameMenuArea());
}

export default class StartGameMenu {
    static build() {
        $('body').append(getMenu());
    }
}
