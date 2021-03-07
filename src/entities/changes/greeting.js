export default class Greeting {

    constructor(greetingData) {
        if (!greetingData.greeting) {
            console.error(greetingData);
            throw Error('Invalid greeting message!')
        }

        this.id = greetingData.user.id;
        this.coordinates = greetingData.user.coordinates;
        this.users = greetingData.users;
        this.nickname = greetingData.user.nickname;
    }

}
