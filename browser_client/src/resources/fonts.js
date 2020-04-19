import ubuntu_bold from './../resources/ubuntu_bold'
import {FontLoader} from "three";

export default {
    get ubuntuBold() {
        return new FontLoader().parse(ubuntu_bold)
    }
}
