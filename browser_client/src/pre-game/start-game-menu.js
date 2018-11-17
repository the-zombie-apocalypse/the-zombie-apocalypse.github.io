import gameMenuArea from "./game-menu-area/game-menu-area"

let menu;

export default function gameMenu() {
    return menu || (menu = gameMenuArea());
}
