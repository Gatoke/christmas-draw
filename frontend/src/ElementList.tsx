import React, {Component} from 'react';
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

const username = prompt("what is your username");
const channelId = "949c6fd1-8417-45ba-b188-66028129d2b5";//todo

const socket = new SockJS('http://192.168.0.192:8080/messages');
const stompClient = Stomp.over(socket);

class ElementList extends Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            channel: {
                connectedUsers: []
            },
            allUsersReady: false,
            sessionId: ''
        };
    }

    componentDidMount() {
        stompClient.connect({},
            (conn: any) => this.onConnected(conn),
            this.onError)
    }

    switchReadyStatus = (event: any) => {
        const request = {
            username: username,
            channelId: channelId
        };
        stompClient.send("/app/chat.switchReadyStatus", {}, JSON.stringify(request));
        event.preventDefault();
    };

    onConnected = (conn: any) => {
        // @ts-ignore
        let sessionId = socket._transport.url.match("(?<=messages\\/([0-9]*)\\/)(.*)(?=\\/websocket)")[0];

        stompClient.subscribe('/topic/channel.' + channelId + "-" + sessionId, this.onMessageReceived);
        stompClient.subscribe('/topic/channel.' + channelId, this.onMessageReceived);
        stompClient.send("/app/chat.newUser", {},
            JSON.stringify({username: username, channelId: channelId}));
        console.log("<< Connected! >>");
    };

    onError = () => {
        console.log("<< Error. Something went wrong! >>");
    };

    onMessageReceived = (payload: any) => {
        const event = JSON.parse(payload.body);
        console.log(event);
        this.setState(() => ({
            channel: event.channel
        }));
        if (event.eventType === 'ALL_USERS_READY') {
            this.setState(() => ({
                allUsersReady: true
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
                        this.state.channel.connectedUsers
                            .map((connectedUser: any) =>
                                <p>{connectedUser.name + " - is ready: " + connectedUser.isReady}</p>)
                    }
                </div>

                <button onClick={this.switchReadyStatus}>Ready?</button>

                <div hidden={!this.state.allUsersReady}>ALL USERS ARE READY</div>
            </div>
        )
    }
}

export default ElementList;
