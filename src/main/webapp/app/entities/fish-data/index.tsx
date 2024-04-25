import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FishData from './fish-data';
import FishDataDetail from './fish-data-detail';
import FishDataUpdate from './fish-data-update';
import FishDataDeleteDialog from './fish-data-delete-dialog';

const FishDataRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FishData />} />
    <Route path="new" element={<FishDataUpdate />} />
    <Route path=":id">
      <Route index element={<FishDataDetail />} />
      <Route path="edit" element={<FishDataUpdate />} />
      <Route path="delete" element={<FishDataDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FishDataRoutes;
