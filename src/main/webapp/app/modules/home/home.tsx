import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        {/* <h1 className="display-4">Welcome to Tackle Tracker!</h1>
        <p> </p>
        <div className="box">
          <h2 className="weather-title">Current Weather</h2>
          <h3 className="weather">Condition - Sunny ☀️</h3>
          <h3 className="weather">Tempature - 80 ℉</h3>
          <h3 className="weather">Cloud - 0%</h3>
        </div> */}
        <p> </p>
        {account?.login ? (
          <div>
            {/* <div className="boxy">
              <h2 className="bait-title">Baits of the Day</h2>
              <h3 className="bait-text">CrankBait - Red - ShallowDiver</h3>
              <h3 className="bait-text">Senko - Black/Blue - 4.25 inch</h3>
              <h3 className="bait-text">Swimbait - White - 4.25 inch</h3>
            </div> */}
            <p> </p>
            <Alert color="success">You are logged in as user &quot;{account.login}&quot;.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              If you want to
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                sign in
              </Link>
              , you can try the default accounts:
              <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;) <br />- User (login=&quot;user&quot; and
              password=&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              You don&apos;t have an account yet?&nbsp;
              <Link to="/account/register" className="alert-link">
                Register a new account
              </Link>
            </Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;
