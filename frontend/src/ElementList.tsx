import React, {Component} from 'react';
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

const username = prompt("what is your username");
const channelId = "b16b25c7-5731-421c-8e1c-027622ab8598";//todo

const socket = new SockJS('http://192.168.0.192:8080/messages');
const stompClient = Stomp.over(socket);

class ElementList extends Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            channel: {
                connectedUsers: []
            }
        };
    }

    componentDidMount() {
        stompClient.connect({}, this.onConnected, this.onError)
    }

    sendForm = (event: any) => {
        if (stompClient) {
            const chatMessage = {
                sender: username,
                content: this.state.messageToSend,
                type: 'CHAT'
            };
            stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
            this.setState(() => ({
                messageToSend: ''
            }));
        }
        event.preventDefault();
    };

    switchReadyStatus = (event: any) => {
        const request = {
            username: username,
            channelId: channelId
        };
        stompClient.send("/app/chat.switchReadyStatus", {}, JSON.stringify(request));
        event.preventDefault();
    };

    onConnected = () => {
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
        // if (event.eventType === 'USER_CONNECTED') {
        //     this.setState(() => ({
        //         channel: event.channel
        //     }));
        // }
        // if (event.eventType === 'USER_DISCONNECTED') {
        //     this.setState(() => ({
        //         channel: event.channel
        //     }))
        // }
        // if (event.eventType === 'USER_READY_STATUS_CHANGED') {
        //     this.setState(() => ({
        //         channel: event.channel
        //     }))
        // }
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
            </div>
        )
    }
}

export default ElementList;