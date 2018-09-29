export default class Greeting {

    constructor(greetingData) {
        if (!greetingData.greeting) {
            console.error(greetingData);
            throw Error('Invalid greeting message!')
        }

        console.log(greetingData);

        this.id = greetingData.user.id;
        this.coordinates = greetingData.user.coordinates;
    }

}
