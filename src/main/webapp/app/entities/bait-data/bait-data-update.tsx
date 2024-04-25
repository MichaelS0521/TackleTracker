import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICreatedCatch } from 'app/shared/model/created-catch.model';
import { getEntities as getCreatedCatches } from 'app/entities/created-catch/created-catch.reducer';
import { IBaitData } from 'app/shared/model/bait-data.model';
import { getEntity, updateEntity, createEntity, reset } from './bait-data.reducer';

export const BaitDataUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const createdCatches = useAppSelector(state => state.createdCatch.entities);
  const baitDataEntity = useAppSelector(state => state.baitData.entity);
  const loading = useAppSelector(state => state.baitData.loading);
  const updating = useAppSelector(state => state.baitData.updating);
  const updateSuccess = useAppSelector(state => state.baitData.updateSuccess);

  const handleClose = () => {
    navigate('/bait-data');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCreatedCatches({}));
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
      ...baitDataEntity,
      ...values,
      catches: mapIdList(values.catches),
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
          ...baitDataEntity,
          catches: baitDataEntity?.catches?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tackleTrackerNewApp.baitData.home.createOrEditLabel" data-cy="BaitDataCreateUpdateHeading">
            Create or edit a Bait Data
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="bait-data-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Type" id="bait-data-type" name="type" data-cy="type" type="text" />
              <ValidatedField label="Color" id="bait-data-color" name="color" data-cy="color" type="text" />
              <ValidatedField label="Hard" id="bait-data-hard" name="hard" data-cy="hard" check type="checkbox" />
              <ValidatedField label="Soft" id="bait-data-soft" name="soft" data-cy="soft" check type="checkbox" />
              <ValidatedField label="Catches" id="bait-data-catches" data-cy="catches" type="select" multiple name="catches">
                <option value="" key="0" />
                {createdCatches
                  ? createdCatches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bait-data" replace color="info">
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

export default BaitDataUpdate;
