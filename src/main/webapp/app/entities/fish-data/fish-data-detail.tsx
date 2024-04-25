import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './fish-data.reducer';

export const FishDataDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const fishDataEntity = useAppSelector(state => state.fishData.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fishDataDetailsHeading">Fish Data</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{fishDataEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{fishDataEntity.name}</dd>
          <dt>
            <span id="waterType">Water Type</span>
          </dt>
          <dd>{fishDataEntity.waterType}</dd>
          <dt>
            <span id="weight">Weight</span>
          </dt>
          <dd>{fishDataEntity.weight}</dd>
          <dt>Created Catch</dt>
          <dd>{fishDataEntity.createdCatch ? fishDataEntity.createdCatch.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/fish-data" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/fish-data/${fishDataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FishDataDetail;
