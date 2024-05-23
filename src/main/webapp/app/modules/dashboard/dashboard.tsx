import axios from 'axios';
import './dashboard.scss';
import React, { useState, useEffect } from 'react';
import TopBaits from './topbaits/topBaits';

interface Weather {
  location: string;
  current: {
    temp_c: number;
    temp_f: number;
    condition: {
      text: string;
      icon: string;
    };
  };
}

const Dashboard = () => {
  const [weatherApi, setWeatherApi] = useState<Weather | null>(null);

  const fetchWeather = async () => {
    try {
      const url = `http://api.weatherapi.com/v1/current.json?key=9981efee200545349f6232130242205&q=dover&aqi=yes`;
      const response = await axios.get(url);
      const weatherData: Weather = {
        location: response.data.location.name,
        current: {
          temp_c: response.data.current.temp_c,
          temp_f: response.data.current.temp_f,
          condition: {
            text: response.data.current.condition.text,
            icon: response.data.current.condition.icon,
          },
        },
      };
      setWeatherApi(weatherData);
    } catch (error) {
      console.error('Error fetching the weather data:', error);
      setWeatherApi(null);
    }
  };

  useEffect(() => {
    fetchWeather();
  }, []);

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
          {weatherApi ? (
            <div className="weather weatherimg">
              <h2>Current Weather</h2>
              <div className="weather-location">{weatherApi.location}</div>
              <div className="weather-temp">{weatherApi.current.temp_f}°F</div>
              <div className="weather-temp">{weatherApi.current.temp_c}°C</div>
              <div className="weather-condition">
                <span>{weatherApi.current.condition.text}</span>
                <span>
                  <img src={weatherApi.current.condition.icon} alt={weatherApi.current.condition.text} />
                </span>
              </div>
            </div>
          ) : (
            <div>Loading weather data...</div>
          )}
          <h2>Baits of the Day</h2>
          <div className="baitimg">
            <TopBaits />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
