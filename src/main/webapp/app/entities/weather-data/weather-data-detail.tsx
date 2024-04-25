import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './weather-data.reducer';

export const WeatherDataDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const weatherDataEntity = useAppSelector(state => state.weatherData.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="weatherDataDetailsHeading">Weather Data</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{weatherDataEntity.id}</dd>
          <dt>
            <span id="condition">Condition</span>
          </dt>
          <dd>{weatherDataEntity.condition}</dd>
          <dt>
            <span id="temperature">Temperature</span>
          </dt>
          <dd>{weatherDataEntity.temperature}</dd>
          <dt>
            <span id="dateStamp">Date Stamp</span>
          </dt>
          <dd>
            {weatherDataEntity.dateStamp ? (
              <TextFormat value={weatherDataEntity.dateStamp} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/weather-data" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/weather-data/${weatherDataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WeatherDataDetail;
