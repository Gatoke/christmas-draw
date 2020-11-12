import React, {Component} from 'react';
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

const username = prompt("what is your username");

const socket = new SockJS('http://192.168.0.192:8080/messages');
const stompClient = Stomp.over(socket);

class ElementList extends Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            messages: [],
            messageToSend: ''
        };
    }

    componentDidMount() {
        stompClient.connect({}, this.onConnected, this.onError)
    }

    myChangeHandler = (event: any) => {
        this.setState(() => ({
            messageToSend: event.target.value
        }));
    };

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

    onConnected = () => {
        stompClient.subscribe('/topic/public', this.onMessageReceived);
        stompClient.send("/app/chat.newUser", {},
            JSON.stringify({sender: username, type: 'CONNECT'}));
    };

    onError = () => {
        this.setState((prevState: any) => ({
            messages: [...prevState.messages, `<< Error. Something went wrong! >>`]
        }));
    };

    onMessageReceived = (payload: any) => {
        const message = JSON.parse(payload.body);

        if (message.type === 'CONNECT') {
            this.setState((prevState: any) => ({
                messages: [...prevState.messages, `<< WebSocket Client Connected! Username: ${message.sender} >>`]
            }));
        }

        if (message.type === 'CHAT') {
            this.setState((prevState: any) => ({
                messages: [...prevState.messages, message.content]
            }))
        }

        if (message.type === 'DISCONNECT') {
            this.setState((prevState: any) => ({
                messages: [...prevState.messages, '<< Disconnected ! >>', `User: ${message.sender}`]
            }));
        }
    };

    render() {
        return (
            <div>
                <h2>Element List</h2>
                <div>
                    {this.state.messages.map((msg: string) => <p>{msg}</p>)}
                </div>

                <div>
                    <input type='text'
                           value={this.state.messageToSend}
                           onChange={this.myChangeHandler}/>
                </div>
                <button onClick={this.sendForm}>Send message</button>
            </div>
        )
    }
}

export default ElementList;