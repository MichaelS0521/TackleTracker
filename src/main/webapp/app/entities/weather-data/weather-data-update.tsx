import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IWeatherData } from 'app/shared/model/weather-data.model';
import { getEntity, updateEntity, createEntity, reset } from './weather-data.reducer';

export const WeatherDataUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const weatherDataEntity = useAppSelector(state => state.weatherData.entity);
  const loading = useAppSelector(state => state.weatherData.loading);
  const updating = useAppSelector(state => state.weatherData.updating);
  const updateSuccess = useAppSelector(state => state.weatherData.updateSuccess);

  const handleClose = () => {
    navigate('/weather-data');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.temperature !== undefined && typeof values.temperature !== 'number') {
      values.temperature = Number(values.temperature);
    }

    const entity = {
      ...weatherDataEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...weatherDataEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tackleTrackerNewApp.weatherData.home.createOrEditLabel" data-cy="WeatherDataCreateUpdateHeading">
            Create or edit a Weather Data
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="weather-data-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Condition" id="weather-data-condition" name="condition" data-cy="condition" type="text" />
              <ValidatedField label="Temperature" id="weather-data-temperature" name="temperature" data-cy="temperature" type="text" />
              <ValidatedField label="Date Stamp" id="weather-data-dateStamp" name="dateStamp" data-cy="dateStamp" type="date" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/weather-data" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default WeatherDataUpdate;
