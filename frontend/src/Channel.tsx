import React, {Component} from 'react';
import SockJS from "sockjs-client";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Grid,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Paper,
    TextField
} from "@material-ui/core"
import {GlobalStyle} from "./App.styles";
import {Stomp} from "@stomp/stompjs";
import PersonSharpIcon from '@material-ui/icons/PersonSharp';
import CheckCircleIcon from '@material-ui/icons/CheckCircle';
import CancelIcon from '@material-ui/icons/Cancel';
import Countdown from "react-countdown";

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

    hasResult(): boolean {
        return this.state.pickedResult !== null && this.state.pickedResult !== '';
    }

    renderer = (seconds: any, completed: any) => {
        if (completed) {
            // Render a complete state
            return <div>
                <h1>Wylosowałeś(-aś): <span style={{color: 'rgb(220,0,0)'}}>{this.state.pickedResult}</span></h1>
                <p><small>Aby się upewnić, że każda osoba dostanie prezent, kliknij przycisk poniżej.</small></p>
                <Button
                    variant="contained"
                    onClick={this.connect}
                    size="large"
                    className="CreateChannelButton"
                    style={{margin: 'auto'}}
                >Zweryfikuj</Button>
            </div>
        } else {
            // Render a countdown
            return <h1>{seconds}</h1>
        }
    };

    render() {
        if (this.hasResult()) {
            return (
                <div>
                    <GlobalStyle/>
                    <Paper className="Home" elevation={20}>
                        <div>
                            <Countdown date={Date.now() + 3000}
                                       renderer={props => this.renderer(props.seconds, props.completed)}/>
                        </div>
                    </Paper>
                </div>
            )
        }

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
                            >Wejdź do pokoju</Button>
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
                <GlobalStyle/>
                <Paper className="Home" elevation={20}>
                    <Grid container spacing={2}>

                        <Grid item xs={6}>
                            <p><b>{this.state.channel.name}</b></p>
                        </Grid>
                        <Grid item xs={6}>
                            <p><small>{this.state.channel.id}</small></p>
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
            </div>
        )
    }
}

export default Channel;
