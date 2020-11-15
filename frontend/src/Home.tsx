import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';

import './App.css';

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
            <div className="App">
                <input placeholder="Channel name" type="text" onChange={this.setChannelName}/>
                <button disabled={this.state.channelName.length <= 0}
                        onClick={this.createChannel}>Create new channel
                </button>
            </div>
        );
    }
}

export default Home;
