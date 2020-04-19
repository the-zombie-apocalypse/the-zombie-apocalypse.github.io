import font from './../resources/helvetiker_regular'
import {FontLoader} from "three";

export default {
    get helvetikerRegular() {
        return new FontLoader().parse(font)
    }
}
