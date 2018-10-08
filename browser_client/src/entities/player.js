import {BoxGeometry, Mesh, MeshLambertMaterial} from "three"
import SceneObject from './scene_object'

export default class Player extends SceneObject {

    constructor(id, coordinates) {
        super(Player.buildSceneObject());
        id && (this.id = id);
        coordinates && (this.position = coordinates)
    }

    get id() {
        return this._id
    }

    set id(id) {
        this._id = id
    }

    get position() {
        return this._sceneObject.position
    }

    set position(coordinates) {
        this._sceneObject.position.x = coordinates.x;
        this._sceneObject.position.y = coordinates.y;
    }

    get rotation() {
        return this._sceneObject.rotation
    }

    static buildSceneObject() {
        let geometry = new BoxGeometry(4, 4, 4);
        let material = new MeshLambertMaterial({color: 0x00ff00});
        return new Mesh(geometry, material);
    }
}
