import React, {Component} from "react";
import {GlobalStyle} from "../App.styles";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    TextField
} from "@material-ui/core";

type MyProps = {
    connected: boolean,
    username: string,
    connect: any,
    setUsername: any
}

export class WhoAreYou extends Component<MyProps> {

    render() {
        return (
            <div>
                <GlobalStyle/>
                <Dialog open={!this.props.connected} aria-labelledby="form-dialog-title">
                    <DialogTitle>Kim jesteś</DialogTitle>
                    <form onSubmit={this.props.connect}>
                        <DialogContent style={{textAlign: 'center'}}>
                            <DialogContentText>
                                Napisz kim jesteś, aby inni użytkownicy Cię rozpoznali.
                            </DialogContentText>
                            <TextField
                                autoFocus
                                margin="dense"
                                label="Nazwa"
                                variant="outlined"
                                onChange={this.props.setUsername}
                                fullWidth
                            />
                        </DialogContent>
                        <DialogActions>
                            <Button
                                variant="contained"
                                onClick={this.props.connect}
                                disabled={this.props.username.length <= 2}
                                size="large"
                                className="CreateChannelButton"
                                style={{margin: 'auto'}}
                            >Wejdź do pokoju</Button>
                        </DialogActions>
                    </form>
                </Dialog>
            </div>
        )
    }
}
