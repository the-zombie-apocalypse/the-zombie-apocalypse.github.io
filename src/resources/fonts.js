import ubuntu_bold from './ubuntu_bold.json'
import {FontLoader} from "three";

export default {
    get ubuntuBold() {
        return new FontLoader().parse(ubuntu_bold)
    }
}
