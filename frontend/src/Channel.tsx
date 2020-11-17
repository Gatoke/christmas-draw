import React, {Component} from 'react';
import SockJS from "sockjs-client";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    TextField
} from "@material-ui/core"
import {GlobalStyle} from "./App.styles";
import {Stomp} from "@stomp/stompjs";

let socket: any = null;
let stompClient: any = null;

class Channel extends Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            channel: {
                id: this.props.match.params.id,
                connectedUsers: [],
                name: null
            },
            allUsersReady: false,
            pickedResult: '',
            username: '',
            connected: false
        };
    }

    setUsername = (event: any) => {
        this.setState(() => ({
            username: event.target.value
        }))
    };

    connect = () => {
        socket = new SockJS('http://192.168.0.192:8080/messages');
        stompClient = Stomp.over(socket);

        stompClient.connect({},
            (conn: any) => this.onConnected(conn),
            this.onError)
    };

    switchReadyStatus = (event: any) => {
        const request = {
            username: this.state.username,
            channelId: this.state.channel.id
        };
        stompClient.send("/app/chat.switchReadyStatus", {}, JSON.stringify(request));
        event.preventDefault();
    };

    onConnected = (conn: any) => {
        // @ts-ignore
        console.log(socket._transport.url);

        // @ts-ignore
        let sessionId = socket._transport.url.match("(?<=messages\\/([0-9]*)\\/)(.*)(?=\\/websocket)")[0];

        stompClient.subscribe('/topic/channel.' + this.state.channel.id + "-" + sessionId, this.onMessageReceived);
        stompClient.subscribe('/topic/channel.' + this.state.channel.id, this.onMessageReceived);
        console.log("Creating user in" + this.state.channel.id);
        stompClient.send("/app/chat.newUser", {},
            JSON.stringify({username: this.state.username, channelId: this.state.channel.id}));
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
                channel: event.channel,
                connected: true
            }));
        }
    };

    render() {
        if (this.state.connected === false) {
            return (
                <div>
                    <GlobalStyle/>
                    <Dialog open={this.state.connected === false} aria-labelledby="form-dialog-title">
                        <DialogTitle>Kim jesteś</DialogTitle>
                        <DialogContent style={{textAlign: 'center'}}>
                            <DialogContentText>
                                Napisz kim jesteś, aby inni użytkownicy Cię rozpoznali.
                            </DialogContentText>
                            <TextField
                                autoFocus
                                margin="dense"
                                label="Nazwa"
                                variant="outlined"
                                onChange={this.setUsername}
                                fullWidth
                            />
                        </DialogContent>
                        <DialogActions>
                            <Button
                                variant="contained"
                                onClick={this.connect}
                                disabled={this.state.username.length <= 2}
                                size="large"
                                className="CreateChannelButton"
                                style={{margin: 'auto'}}
                            >Akceptuj</Button>
                        </DialogActions>
                    </Dialog>
                </div>
            )
        }

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
