import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';

import './Home.css';
import {Button, Grid, Paper, TextField} from "@material-ui/core";


class Home extends Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            channelName: '',
            channelId: ''
        };
    }

    createChannel = () => {
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
    };

    setChannelName = (event: any) => {
        this.setState(() => ({
            channelName: event.target.value
        }))
    };

    render() {
        if (this.state.channelId && this.state.channelId !== '') {
            return <Redirect to={"channel/" + this.state.channelId}/>
        }
        return (

            <div>
                <div className="background-image"/>
                <Paper className="Home" elevation={3}>
                    <Grid container direction="column" alignItems="center" justify="center">
                        <TextField
                            label="Channel name"
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
                            marginTop: '10%',
                            marginBottom: '10%'
                        }}/>
                        <Button
                            variant="contained"
                            onClick={this.createChannel}
                            disabled={this.state.channelName.length <= 0}
                            size="large"
                            className="CreateChannelButton"
                        >
                            Create channel
                        </Button>
                    </Grid>
                </Paper>
            </div>
        );
    }
}

export default Home;
