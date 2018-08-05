import {BoxGeometry, Mesh, MeshLambertMaterial} from "three";

export default class Player {

    constructor() {
        this._playerSettings = {
            x: 0,
            y: 0,
            isMovingX: false,
            isMovingY: false,
            walkStepTime: 0
        };

        this._sceneObject = this.buildSceneObject();
    }

    get playerSettings() {
        return this._playerSettings
    }

    get sceneObject() {
        return this._sceneObject
    }

    get position() {
        return this._sceneObject.position
    }

    get rotation() {
        return this._sceneObject.rotation
    }

    buildSceneObject() {
        let geometry = new BoxGeometry(30, 30, 30);
        let material = new MeshLambertMaterial({color: 0x00ff00});
        return new Mesh(geometry, material);
    }
}
