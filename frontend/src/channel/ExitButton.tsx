import React, {Component} from "react";
import {Redirect} from "react-router";
import {Button} from "@material-ui/core";
import ExitToAppIcon from '@material-ui/icons/ExitToApp';


class ExitButton extends Component<any, any> {

    state = {
        clicked: false
    };

    redirect = () => {
        this.setState(() => ({
            clicked: true
        }))
    };

    render() {

        if (this.state.clicked) {
            return <Redirect to={"/"}/>
        }

        return (
            <Button variant="contained" size="small" style={{marginTop: '20px'}}
                    startIcon={<ExitToAppIcon/>} onClick={this.redirect}>
                Wyjdź
            </Button>
        )
    }
}

export default ExitButton