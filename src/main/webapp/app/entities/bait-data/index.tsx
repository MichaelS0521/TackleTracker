import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BaitData from './bait-data';
import BaitDataDetail from './bait-data-detail';
import BaitDataUpdate from './bait-data-update';
import BaitDataDeleteDialog from './bait-data-delete-dialog';

const BaitDataRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BaitData />} />
    <Route path="new" element={<BaitDataUpdate />} />
    <Route path=":id">
      <Route index element={<BaitDataDetail />} />
      <Route path="edit" element={<BaitDataUpdate />} />
      <Route path="delete" element={<BaitDataDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BaitDataRoutes;
