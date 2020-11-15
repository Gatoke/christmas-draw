import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import Home from "./Home";
import {BrowserRouter, Route} from "react-router-dom";
import Channel from "./Channel";

ReactDOM.render(
    <BrowserRouter>
        <Route exact path="/" component={Home}/>
        <Route exact path="/channel/:id" component={Channel}/>
    </BrowserRouter>,
    document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
