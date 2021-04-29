import React from "react";

import {ConnectedRouter} from "connected-react-router"
import {Route} from "react-router-dom"

import Hello from './Hello';
import Login from './component/Login';
import Signup from './component/Signup';
import Header from './component/Header'
function App() {
  return (
    <div className="App">
      <Header/>
    
      <Route path="/hello"  component={Hello} />
      <Route path="/login"  component={Login} />
      <Route path="/signup"  component={Signup} />
    </div>
  );
}

export default App;
