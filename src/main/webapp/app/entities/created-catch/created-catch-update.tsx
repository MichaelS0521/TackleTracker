import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBaitData } from 'app/shared/model/bait-data.model';
import { getEntities as getBaitData } from 'app/entities/bait-data/bait-data.reducer';
import { IWeatherData } from 'app/shared/model/weather-data.model';
import { getEntities as getWeatherData } from 'app/entities/weather-data/weather-data.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { ICreatedCatch } from 'app/shared/model/created-catch.model';
import { getEntity, updateEntity, createEntity, reset } from './created-catch.reducer';

export const CreatedCatchUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const baitData = useAppSelector(state => state.baitData.entities);
  const weatherData = useAppSelector(state => state.weatherData.entities);
  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const createdCatchEntity = useAppSelector(state => state.createdCatch.entity);
  const loading = useAppSelector(state => state.createdCatch.loading);
  const updating = useAppSelector(state => state.createdCatch.updating);
  const updateSuccess = useAppSelector(state => state.createdCatch.updateSuccess);

  const handleClose = () => {
    navigate('/created-catch');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBaitData({}));
    dispatch(getWeatherData({}));
    dispatch(getUserProfiles({}));
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

    const entity = {
      ...createdCatchEntity,
      ...values,
      baitdata: mapIdList(values.baitdata),
      weather: weatherData.find(it => it.id.toString() === values.weather?.toString()),
      user: userProfiles.find(it => it.id.toString() === values.user?.toString()),
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
          ...createdCatchEntity,
          baitdata: createdCatchEntity?.baitdata?.map(e => e.id.toString()),
          weather: createdCatchEntity?.weather?.id,
          user: createdCatchEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tackleTrackerNewApp.createdCatch.home.createOrEditLabel" data-cy="CreatedCatchCreateUpdateHeading">
            Create or edit a Created Catch
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="created-catch-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Datestamp" id="created-catch-datestamp" name="datestamp" data-cy="datestamp" type="date" />
              <ValidatedField label="Location" id="created-catch-location" name="location" data-cy="location" type="text" />
              <ValidatedField label="Baitdata" id="created-catch-baitdata" data-cy="baitdata" type="select" multiple name="baitdata">
                <option value="" key="0" />
                {baitData
                  ? baitData.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="created-catch-weather" name="weather" data-cy="weather" label="Weather" type="select">
                <option value="" key="0" />
                {weatherData
                  ? weatherData.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="created-catch-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/created-catch" replace color="info">
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

export default CreatedCatchUpdate;
