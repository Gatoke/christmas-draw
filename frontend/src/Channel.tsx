import React, {Component} from 'react';
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

class Channel extends Component<any, any> {

    socket;
    stompClient;

    constructor(props: any) {
        super(props);
        this.socket = new SockJS('http://192.168.0.192:8080/messages');
        this.stompClient = Stomp.over(this.socket);
        this.state = {
            channel: {
                id: this.props.match.params.id,
                connectedUsers: []
            },
            allUsersReady: false,
            pickedResult: '',
            username: ''
        };
    }

    componentDidMount() {
        const username = prompt("what is your username");
        this.setState(() => ({username: username}));

        this.stompClient.connect({},
            (conn: any) => this.onConnected(conn),
            this.onError)
    }

    switchReadyStatus = (event: any) => {
        const request = {
            username: this.state.username,
            channelId: this.state.channel.id
        };
        this.stompClient.send("/app/chat.switchReadyStatus", {}, JSON.stringify(request));
        event.preventDefault();
    };

    onConnected = (conn: any) => {
        // @ts-ignore
        console.log(this.socket._transport.url);

        // @ts-ignore
        let sessionId = this.socket._transport.url.match("(?<=messages\\/([0-9]*)\\/)(.*)(?=\\/websocket)")[0];

        this.stompClient.subscribe('/topic/channel.' + this.state.channel.id + "-" + sessionId, this.onMessageReceived);
        this.stompClient.subscribe('/topic/channel.' + this.state.channel.id, this.onMessageReceived);
        console.log("Creating user in" + this.state.channel.id);
        this.stompClient.send("/app/chat.newUser", {},
            JSON.stringify({username: this.state.username, channelId: this.state.channel.id}));
        console.log("<< Connected! >>");
    };

    onError = () => {
        console.log("<< Error. Something went wrong! >>");
    };

    onMessageReceived = (payload: any) => {
        const event = JSON.parse(payload.body);
        console.log(event);

        if (event.eventType === 'ALL_USERS_READY') {
            this.setState(() => ({
                allUsersReady: true
            }));
        }
        if (event.eventType === 'RANDOM_PERSON_PICKED') {
            this.setState(() => ({
                pickedResult: event.username
            }));
        } else {
            this.setState(() => ({
                channel: event.channel
            }));
        }
    };

    render() {
        if (this.state.channel.connectedUsers <= 0) {
            return <div>No user connected.</div>
        }

        return (
            <div>
                <h2>{this.state.channel.name}</h2>
                <div>
                    {
                        this.state.channel.connectedUsers.map((connectedUser: any) =>
                            <p key={connectedUser.id}>{connectedUser.name + " - is ready: " + connectedUser.isReady}</p>)
                    }
                </div>

                <button onClick={this.switchReadyStatus}>Ready?</button>

                <div hidden={!this.state.allUsersReady}>ALL USERS ARE READY</div>

                <div>You have picked: {this.state.pickedResult}</div>
            </div>
        )
    }
}

export default Channel;
