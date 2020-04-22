import {BoxGeometry, Group, Mesh, MeshLambertMaterial, TextBufferGeometry} from "three"
import SceneObject from './scene_object'
import fonts from './../resources/fonts'

export default class Player extends SceneObject {

    constructor(id, coordinates, name) {
        super(Player.buildSceneObject(name));
        if (id) {
            this.id = id;
        }
        this.name = name || 'noname';
        coordinates && (this.position = coordinates)
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

    static buildSceneObject(name) {
        let scale = 1;
        let userGroup = new Group();
        let geometry = new BoxGeometry(4, 4, 4);
        let userMaterial = new MeshLambertMaterial({
            color: 0xF4FFFC,
            // emissive: 0x00FF03
        });
        let nameMaterial = new MeshLambertMaterial({
            color: 0x000400,
            // emissive: 0xFFFFFF
        });

        const nicknameGeometry = new TextBufferGeometry(name, {
            font: fonts.ubuntuBold,
            size: 2,
            height: 0,
        });

        let textMesh = new Mesh(nicknameGeometry, nameMaterial);
        textMesh.scale.set(scale, scale, scale);
        textMesh.position.z = 2;
        textMesh.position.y = 2;
        textMesh.position.x = 2;

        userGroup.add(textMesh);
        userGroup.add(new Mesh(geometry, userMaterial));
        return userGroup;
    }
}
