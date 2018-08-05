import {BoxGeometry, Mesh, MeshLambertMaterial} from "three"
import SceneObject from './scene_object'
import global from './global'

export default class Player extends SceneObject {

    constructor() {
        super(Player.buildSceneObject());
    }

    get position() {
        return this._sceneObject.position
    }

    get rotation() {
        return this._sceneObject.rotation
    }

    get onSceneUpdate() {
        return () => {
            this.position.x = global.playerSettings.x;
            this.position.y = global.playerSettings.y;
        }
    }

    static buildSceneObject() {
        let geometry = new BoxGeometry(4, 4, 4);
        let material = new MeshLambertMaterial({color: 0x00ff00});
        return new Mesh(geometry, material);
    }
}
