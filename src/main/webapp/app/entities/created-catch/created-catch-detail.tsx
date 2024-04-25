import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './created-catch.reducer';

export const CreatedCatchDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const createdCatchEntity = useAppSelector(state => state.createdCatch.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="createdCatchDetailsHeading">Created Catch</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{createdCatchEntity.id}</dd>
          <dt>
            <span id="datestamp">Datestamp</span>
          </dt>
          <dd>
            {createdCatchEntity.datestamp ? (
              <TextFormat value={createdCatchEntity.datestamp} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="location">Location</span>
          </dt>
          <dd>{createdCatchEntity.location}</dd>
          <dt>Baitdata</dt>
          <dd>
            {createdCatchEntity.baitdata
              ? createdCatchEntity.baitdata.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {createdCatchEntity.baitdata && i === createdCatchEntity.baitdata.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Weather</dt>
          <dd>{createdCatchEntity.weather ? createdCatchEntity.weather.id : ''}</dd>
          <dt>User</dt>
          <dd>{createdCatchEntity.user ? createdCatchEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/created-catch" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/created-catch/${createdCatchEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CreatedCatchDetail;
