import React, {Component} from 'react';

const ws = new WebSocket("ws://192.168.0.192:8080/questions");//todo

class ElementList extends Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            messages: [],
            messageToSend: ''
        };
    }

    componentDidMount() {
        ws.onopen = () => {
            this.setState((prevState: any) => ({
                messages: [...prevState.messages, '<< WebSocket Client Connected! >>']
            }));
        };
        ws.onmessage = incomingMessage => {
            this.setState((prevState: any) => ({
                messages: [...prevState.messages, incomingMessage.data]
            }))
        };
        ws.onclose = message => {
            console.log('closed: ' + message.code);
            this.setState((prevState: any) => ({
                messages: [...prevState.messages, '<< Disconnected ! >>', `code: ${message.code}`]
            }));
        };
    }

    myChangeHandler = (event: any) => {
        this.setState(() => ({
            messageToSend: event.target.value
        }));
    };

    sendForm = () => {
        ws.send(this.state.messageToSend);
        this.setState(() => ({
            messageToSend: ''
        }))
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