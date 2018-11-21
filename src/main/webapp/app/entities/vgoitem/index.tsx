import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Vgoitem from './vgoitem';
import VgoitemDetail from './vgoitem-detail';
import VgoitemUpdate from './vgoitem-update';
import VgoitemDeleteDialog from './vgoitem-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VgoitemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VgoitemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VgoitemDetail} />
      <ErrorBoundaryRoute path={match.url} component={Vgoitem} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={VgoitemDeleteDialog} />
  </>
);

export default Routes;
