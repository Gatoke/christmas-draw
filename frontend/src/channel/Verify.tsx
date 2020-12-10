import React, {Component} from "react";
import {GlobalStyle} from "../App.styles";
import {Button, Grid, List, ListItem, ListItemText, Paper, TextField} from "@material-ui/core";
import ExitButton from "./ExitButton";

type MyProps = {
    peopleResults: [],
    channel: any,
    verifyMessageSent: boolean,
    verifyMessage: string,
    sendVerifyMessage: any,
    setVerifyMessage: any
}

export class Verify extends Component<MyProps> {

    render() {
        return (
            <div className="Home">
                <GlobalStyle/>
                <Paper elevation={20} style={{padding: '20px'}}>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <List>
                                {
                                    this.props.peopleResults.map((result: string) => (
                                        <ListItem divider style={{backgroundColor: 'rgb(250,250,250)'}}>
                                            <ListItemText style={{
                                                display: 'flex',
                                                justifyContent: 'center'
                                            }}>
                                                {result}
                                            </ListItemText>
                                        </ListItem>
                                    ))
                                }
                                {
                                    (this.props.peopleResults.length < this.props.channel.connectedUsers.length)
                                        ? <ListItem button divider
                                                    style={{display: 'flex', justifyContent: 'center'}}>
                                            <ListItemText>
                                                Wyślij <b>anonimową</b> wiadomość zawierającą imię osoby, którą
                                                wylosowałeś. Wynik
                                                zostanie wyświelony dopiero wtedy, gdy wszyscy inni zrobią to samo.
                                            </ListItemText>
                                            <ListItemText>
                                                <p><small style={{opacity: '0.7'}}>
                                                    Nie musisz wpisywać imienia dokładnie tak, jak było wyświetlone na
                                                    poprzednim ekranie. Ważne jest tylko, aby każdy wiedział o kogo
                                                    chodzi.</small></p>
                                            </ListItemText>
                                        </ListItem>
                                        :
                                        <p style={{color: 'green'}}><b>Lista pokazuje kogo wylosowali pozostali
                                            uczestnicy. Jeśli jesteś
                                            na liście, to znaczy, że dostaniesz prezent. :-)</b></p>
                                }
                            </List>
                        </Grid>

                        {
                            this.props.peopleResults.length < this.props.channel.connectedUsers.length && this.props.verifyMessageSent
                                ? <b>Czekam na pozostałych uczestników. Nie zamykaj okna przeglądarki.</b>
                                : ''
                        }


                        <Grid item xs={9}
                              hidden={this.props.peopleResults.length >= this.props.channel.connectedUsers.length}>
                            <TextField
                                autoFocus
                                margin="dense"
                                label="Wpisz kogo wybrałeś, np. 'tomek:)'"
                                variant="outlined"
                                onChange={this.props.setVerifyMessage}
                                disabled={this.props.verifyMessageSent}
                                fullWidth
                            />
                        </Grid>
                        <Grid item xs={3}
                              hidden={this.props.peopleResults.length >= this.props.channel.connectedUsers.length}>
                            <Button
                                variant="contained"
                                onClick={this.props.sendVerifyMessage}
                                disabled={this.props.verifyMessage.length < 1 || this.props.verifyMessageSent}
                                size="large"
                                className="CreateChannelButton"
                                style={{margin: 'auto'}}
                            >{this.props.verifyMessageSent ? 'Wysłano' : 'Wyślij'}</Button>
                        </Grid>
                    </Grid>
                </Paper>
                <ExitButton/>
            </div>
        )
    }

}
