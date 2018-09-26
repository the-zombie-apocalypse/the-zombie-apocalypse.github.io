export default class Greeting {

    constructor(greetingData) {

        if (!greetingData.greeting) {
            console.error(greetingData);
            throw Error('Invalid greeting message!')
        }

        this.id = greetingData.user.id;
    }

}
