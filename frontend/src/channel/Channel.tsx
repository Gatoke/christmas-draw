import React, {Component} from 'react';
import SockJS from "sockjs-client";
import {Button, Grid, List, ListItem, ListItemIcon, ListItemText, Paper} from "@material-ui/core"
import {GlobalStyle} from "../App.styles";
import {Stomp} from "@stomp/stompjs";
import PersonSharpIcon from '@material-ui/icons/PersonSharp';
import CheckCircleIcon from '@material-ui/icons/CheckCircle';
import CancelIcon from '@material-ui/icons/Cancel';
import CopyToClipboard from 'react-copy-to-clipboard';
import FileCopyIcon from '@material-ui/icons/FileCopy';
import ExitButton from "./ExitButton";
import {EventType} from "./EventType";
import {Verify} from "./Verify";
import {DrawResult} from "./Countdown";
import {WhoAreYou} from "./WhoAreYou";

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
            pickedResult: '',
            username: '',
            connected: false,
            verifyWindowOpened: false,
            verifyMessage: '',
            verifyMessageSent: false,
            peopleResults: [],
            sessionId: '',
            copiedLink: false
        };
    }

    sendVerifyMessage = () => {
        stompClient.send("/app/channel.verifyMessage", {},
            JSON.stringify({channelId: this.state.channel.id, message: this.state.verifyMessage}));
    };

    setUsername = (event: any) => {
        this.setState(() => ({
            username: event.target.value
        }))
    };

    connect = (event: any) => {
        event.preventDefault();
        socket = new SockJS('http://localhost:8080/events');
        stompClient = Stomp.over(socket);

        stompClient.connect({},
            () => this.onConnected(),
            this.onError)
    };

    switchReadyStatus = (event: any) => {
        const request = {
            username: this.state.username,
            channelId: this.state.channel.id
        };
        stompClient.send("/app/channel.switchReadyStatus", {}, JSON.stringify(request));
        event.preventDefault();
    };

    onConnected = () => {
        const url = this.exportUrl();
        const sessionId = url
            .replace("/websocket", "") // remove /websocket from the end
            .replace(RegExp("^(.*\/)"), ""); // remove all before / character

        this.setState(() => ({
            sessionId: sessionId
        }));

        stompClient.subscribe('/topic/channel.' + this.state.channel.id + "-" + sessionId, this.onMessageReceived);
        stompClient.subscribe('/topic/channel.' + this.state.channel.id, this.onMessageReceived);
        stompClient.send("/app/channel.newUser", {},
            JSON.stringify({username: this.state.username, channelId: this.state.channel.id}));
    };

    exportUrl(): string {
        const url = socket._transport.url;
        if (url.endsWith("/")) {
            const idx = url.lastIndexOf("/");
            return url.replace(idx, idx, '');
        }
        return url;
    }

    onError = () => {
        console.log("<< Error. Something went wrong! >>");
    };

    onMessageReceived = (payload: any) => {
        const event = JSON.parse(payload.body);

        if (event.eventType === EventType.VERIFICATION_MESSAGE_RECEIVED) {
            if (event.userId === this.state.sessionId) {
                this.setState(() => ({
                    verifyMessageSent: true
                }));
            }
        } else if (event.eventType === EventType.RESULTS_VERIFIED) {
            this.setState(() => ({
                peopleResults: event.peopleResults
            }));
        } else if (event.eventType === EventType.RANDOM_PERSON_PICKED) {
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

    hasResult(): boolean {
        return this.state.pickedResult !== null && this.state.pickedResult !== '';
    }

    openVerifyWindow = () => {
        this.setState(() => ({
            verifyWindowOpened: true,
            verifyMessage: '',
            verifyMessageSent: false,
            peopleResults: []
        }))
    };

    setVerifyMessage = (event: any) => {
        this.setState(() => ({
            verifyMessage: event.target.value
        }))
    };

    copyLink = () => {
        this.setState(() => ({copiedLink: true}))
    };

    render() {
        if (this.state.verifyWindowOpened) {
            return (
                <Verify peopleResults={this.state.peopleResults}
                        channel={this.state.channel}
                        verifyMessageSent={this.state.verifyMessageSent}
                        verifyMessage={this.state.verifyMessage}
                        sendVerifyMessage={this.sendVerifyMessage}
                        setVerifyMessage={this.setVerifyMessage}/>
            )
        } else if (this.hasResult()) {
            return (
                <DrawResult pickedResult={this.state.pickedResult}
                            openVerifyWindow={this.openVerifyWindow}/>
            )
        } else if (!this.state.connected) {
            return (
                <WhoAreYou connected={this.state.connected}
                           username={this.state.username}
                           connect={this.connect}
                           setUsername={this.setUsername}/>
            )
        } else if (this.state.channel.connectedUsers <= 0) {
            return <div>No users connected.</div>
        }

        return (
            <div className="Home">
                <GlobalStyle/>
                <Paper elevation={20} style={{padding: '20px', overflow: 'auto'}}>
                    <Grid container spacing={2}>

                        <Grid item xs={6}>
                            <p><b>{this.state.channel.name}</b></p>
                        </Grid>
                        <Grid item xs={6}>
                            <CopyToClipboard text={window.location.href}>
                                <Button variant="contained" size="small" style={{backgroundColor: 'white'}}
                                        startIcon={<FileCopyIcon/>} onClick={this.copyLink}>
                                    <p><small>
                                        {
                                            this.state.copiedLink ? 'Skopiowano!' : 'Skopiuj link do pokoju'
                                        }
                                    </small></p>
                                </Button>
                            </CopyToClipboard>
                        </Grid>
                    </Grid>
                    <List>
                        {this.state.channel.connectedUsers.map((connectedUser: any) =>
                            <ListItem button divider key={connectedUser.id}
                                      style={connectedUser.name === this.state.username ? {backgroundColor: 'rgba(252, 186, 3,0.1)'} : {}}>
                                <Grid item xs={3}>
                                    <ListItemIcon>
                                        <PersonSharpIcon/>
                                    </ListItemIcon>
                                </Grid>
                                <Grid item xs={5}>
                                    <ListItemText>{connectedUser.name}</ListItemText>
                                </Grid>
                                <Grid item xs={3}>
                                    <ListItemText style={{color: 'green'}}>
                                        {connectedUser.isReady ? 'Gotowy/a' : ''}
                                    </ListItemText>
                                </Grid>
                                <Grid item xs={1}>
                                    <ListItemIcon>
                                        {
                                            connectedUser.isReady
                                                ? <CheckCircleIcon style={{color: 'green'}}/>
                                                : <CancelIcon style={{opacity: '0.2'}}/>
                                        }
                                    </ListItemIcon>
                                </Grid>
                            </ListItem>
                        )}

                        {
                            this.state.channel.connectedUsers.length < 3
                                ? <ListItem button divider key={'needMinimum3People'}
                                            style={{display: 'flex', justifyContent: 'center'}}>
                                    <small>Wymagane są minimum 3 osoby</small>
                                </ListItem>
                                : ''
                        }

                    </List>
                    <Button
                        variant="contained"
                        onClick={this.switchReadyStatus}
                        size="large"
                        className="CreateChannelButton"
                        disabled={this.state.channel.connectedUsers.length < 3 || true === this.state.channel.isClosed}
                    >
                        Gotowy?
                    </Button>
                    <Grid container>
                        <Grid item xs={12}>
                            <p><b>
                                {
                                    this.state.channel.connectedUsers.length < 3 || this.state.channel.isClosed
                                        ? ''
                                        : 'Losowanie rozpocznie się kiedy wszyscy będą gotowi'
                                }
                            </b></p>
                        </Grid>
                    </Grid>
                </Paper>
                <ExitButton/>
            </div>
        )
    }
}

export default Channel;
