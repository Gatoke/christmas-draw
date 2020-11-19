import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';

import './Home.css';
import {Button, Paper, TextField} from "@material-ui/core";
import {GlobalStyle} from "./App.styles";


class Home extends Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            channelName: '',
            channelId: ''
        };
    }

    createChannel = (event: any) => {
        console.log("dupa");
        fetch('http://192.168.0.192:8080/createChannel', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json; charset=utf-8'
            },
            body: JSON.stringify({
                channelName: this.state.channelName
            })
        })
            .then(response => response.json())
            .then(channel => this.setState(() => ({
                channelId: channel.id
            })));
        event.preventDefault();
    };

    setChannelName = (event: any) => {
        this.setState(() => ({
            channelName: event.target.value
        }));
    };

    render() {
        if (this.state.channelId && this.state.channelId !== '') {
            return <Redirect to={"channel/" + this.state.channelId}/>
        }
        return (
            <div>
                <GlobalStyle/>
                <Paper className="Home" elevation={20}>
                    <form onSubmit={this.createChannel}>
                        <TextField
                            label="Nazwa pokoju"
                            autoFocus
                            required
                            onChange={this.setChannelName}
                            margin="normal"
                            variant="outlined"
                            className="ChannelNameInput"
                        />
                        <hr style={{
                            opacity: '0',
                            width: '100%',
                            marginTop: '20px',
                            marginBottom: '20px'
                        }}/>
                        <Button
                            variant="contained"
                            onClick={this.createChannel}
                            disabled={this.state.channelName.length <= 0}
                            size="large"
                            className="CreateChannelButton"
                        >
                            Stwórz nowy pokój
                        </Button>
                    </form>
                </Paper>
            </div>
        );
    }
}

export default Home;
