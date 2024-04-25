import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserProfile from './user-profile';
import CreatedCatch from './created-catch';
import FishData from './fish-data';
import WeatherData from './weather-data';
import BaitData from './bait-data';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="created-catch/*" element={<CreatedCatch />} />
        <Route path="fish-data/*" element={<FishData />} />
        <Route path="weather-data/*" element={<WeatherData />} />
        <Route path="bait-data/*" element={<BaitData />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
