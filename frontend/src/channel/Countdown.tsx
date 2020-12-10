import React, {Component} from "react";
import {GlobalStyle} from "../App.styles";
import {Button, Paper} from "@material-ui/core";
import Countdown from "react-countdown";

type MyProps = {
    pickedResult: string
    openVerifyWindow: any,
}

export class DrawResult extends Component<MyProps> {

    renderer = (seconds: any, completed: any) => {
        if (completed) {
            return <div>
                <h1>Wylosowałeś(-aś): <span style={{color: 'rgb(220,0,0)'}}>{this.props.pickedResult}</span></h1>
                <p><small>Aby się upewnić, że każda osoba dostanie prezent, kliknij przycisk poniżej.</small></p>
                <Button
                    variant="contained"
                    onClick={this.props.openVerifyWindow}
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
}
