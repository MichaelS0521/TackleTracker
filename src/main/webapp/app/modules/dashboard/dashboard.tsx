import './dashboard.scss';
import React from 'react';

const Dashboard = () => {
  return (
    <div>
      <div className="header">
        <h1>
          <a>TackleTracker</a>
        </h1>
      </div>
      <div className="row">
        <div className="side">
          <h2>Recent Catches</h2>
          <h5>Last Catch:</h5>
          <div>Image</div>
          <p>Some text about me in culpa qui officia deserunt mollit anim..</p>
          <h3>More Text</h3>
          <p>Lorem ipsum dolor sit ame.</p>
          <div className="fakeimg">Image</div>
          <div className="fakeimg">Image</div>
          <div className="fakeimg">Image</div>
        </div>

        <div className="main">
          <h2>Baits of the Day</h2>
          <div className="baitimg">Image</div>
          <p>Some text..</p>
          <p>
            Sunt in culpa qui officia deserunt mollit anim id est laborum consectetur adipiscing elit, sed do eiusmod tempor incididunt ut
            labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco.
          </p>
          <h2>Current Weather</h2>
          <div className="weatherimg">Image</div>
          <p>Some text..</p>
          <p>
            Sunt in culpa qui officia deserunt mollit anim id est laborum consectetur adipiscing elit, sed do eiusmod tempor incididunt ut
            labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
