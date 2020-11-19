import {createGlobalStyle} from 'styled-components';
import BGImage from './assets/christmas_small.jpg';

export const GlobalStyle = createGlobalStyle`
    html {
      background-image: url(${BGImage});
      background-position: center;
      background-repeat: no-repeat;
      background-size: cover;
      min-height:100%;
  }
     
    body {
      margin: 0;
      padding: 0;
      display: flex;
      justify-content: center;
    }
    
`;
