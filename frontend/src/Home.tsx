import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';

import './Home.css';
import {Button, Grid, Paper, TextField} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";

class Home extends Component<any, any> {

    classes: any;

    constructor(props: any) {
        super(props);
        this.state = {
            channelName: '',
            channelId: ''
        };

        this.classes = makeStyles((theme) => ({
            form: {
                width: '100%', // Fix IE 11 issue.
                marginTop: theme.spacing(1)
            },
            submit: {
                margin: theme.spacing(3, 0, 2),
            },
        }));
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
                <Paper className="Home" elevation={3}>
                    <Grid container direction="column" alignItems="center" justify="center">
                        <TextField
                            label="Channel name"
                            autoFocus
                            required
                            onChange={this.setChannelName}
                            margin="normal"
                            className="ChannelNameInput"
                        />
                        <Button
                            variant="contained"
                            onClick={this.createChannel}
                            disabled={this.state.channelName.length <= 0}
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
