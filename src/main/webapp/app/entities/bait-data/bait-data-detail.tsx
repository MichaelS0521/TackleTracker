import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bait-data.reducer';

export const BaitDataDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const baitDataEntity = useAppSelector(state => state.baitData.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="baitDataDetailsHeading">Bait Data</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{baitDataEntity.id}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{baitDataEntity.type}</dd>
          <dt>
            <span id="color">Color</span>
          </dt>
          <dd>{baitDataEntity.color}</dd>
          <dt>
            <span id="hard">Hard</span>
          </dt>
          <dd>{baitDataEntity.hard ? 'true' : 'false'}</dd>
          <dt>
            <span id="soft">Soft</span>
          </dt>
          <dd>{baitDataEntity.soft ? 'true' : 'false'}</dd>
          <dt>Catches</dt>
          <dd>
            {baitDataEntity.catches
              ? baitDataEntity.catches.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {baitDataEntity.catches && i === baitDataEntity.catches.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/bait-data" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bait-data/${baitDataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BaitDataDetail;
