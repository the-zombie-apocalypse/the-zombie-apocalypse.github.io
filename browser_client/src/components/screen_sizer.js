class _ScreenSizer {

    constructor(window) {
        this.window = window;
        this.screenWidth = window.innerWidth;
        this.screenHeight = window.innerHeight;

        this.halfScreenWidth = this.screenWidth * 0.5;
        this.halfScreenHeight = this.screenHeight * 0.5;

        this.onResizeCallbacks = [];

        window.addEventListener('resize', this.resize.bind(this));
    }

    addOnResize(callback) {
        this.onResizeCallbacks.push(callback)
    }

    resize() {
        this.screenWidth = this.window.innerWidth;
        this.screenHeight = this.window.innerHeight;

        this.halfScreenWidth = this.screenWidth * 0.5;
        this.halfScreenHeight = this.screenHeight * 0.5;

        this.onResizeCallbacks.forEach(function (callMe) {
            callMe.call()
        });
    }
}

let instance = false;

const ScreenSizer = {
    getInstance(window) {
        return instance || (instance = new _ScreenSizer(window));
    }
};

export default ScreenSizer
