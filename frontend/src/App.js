import React from 'react';
import GreetingList from './components/GreetingList';
import GreetingLookup from './components/GreetingLookup';
import GreetingAdd from './components/GreetingAdd';
import GreetingUpdate from './components/GreetingUpdate';
import './App.css';

function App() {
  return (
      <div className="App">
        <GreetingList />
        <GreetingLookup />
        <GreetingAdd />
          <GreetingUpdate />
      </div>
  );
}
export default App;
