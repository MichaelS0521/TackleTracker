import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CreatedCatch from './created-catch';
import CreatedCatchDetail from './created-catch-detail';
import CreatedCatchUpdate from './created-catch-update';
import CreatedCatchDeleteDialog from './created-catch-delete-dialog';

const CreatedCatchRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CreatedCatch />} />
    <Route path="new" element={<CreatedCatchUpdate />} />
    <Route path=":id">
      <Route index element={<CreatedCatchDetail />} />
      <Route path="edit" element={<CreatedCatchUpdate />} />
      <Route path="delete" element={<CreatedCatchDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CreatedCatchRoutes;
