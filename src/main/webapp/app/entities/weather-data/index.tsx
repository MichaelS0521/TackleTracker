import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import WeatherData from './weather-data';
import WeatherDataDetail from './weather-data-detail';
import WeatherDataUpdate from './weather-data-update';
import WeatherDataDeleteDialog from './weather-data-delete-dialog';

const WeatherDataRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<WeatherData />} />
    <Route path="new" element={<WeatherDataUpdate />} />
    <Route path=":id">
      <Route index element={<WeatherDataDetail />} />
      <Route path="edit" element={<WeatherDataUpdate />} />
      <Route path="delete" element={<WeatherDataDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WeatherDataRoutes;
