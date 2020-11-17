import {createGlobalStyle} from 'styled-components';
import BGImage from './assets/christmas_small.jpg';

export const GlobalStyle = createGlobalStyle`
    html {
        height: 100%;
    }
     
    body {
      background-image: url(${BGImage});
      background-position: center;
      background-repeat: no-repeat;
      background-size: cover;
      
      margin: 0;
      padding: 0 20px;
      display: flex;
      justify-content: center;
      min-height: 100vh;
    }
    
`;
