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
import { IFishData } from 'app/shared/model/fish-data.model';
import { getEntity, updateEntity, createEntity, reset } from './fish-data.reducer';

export const FishDataUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const createdCatches = useAppSelector(state => state.createdCatch.entities);
  const fishDataEntity = useAppSelector(state => state.fishData.entity);
  const loading = useAppSelector(state => state.fishData.loading);
  const updating = useAppSelector(state => state.fishData.updating);
  const updateSuccess = useAppSelector(state => state.fishData.updateSuccess);

  const handleClose = () => {
    navigate('/fish-data');
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
    if (values.weight !== undefined && typeof values.weight !== 'number') {
      values.weight = Number(values.weight);
    }

    const entity = {
      ...fishDataEntity,
      ...values,
      createdCatch: createdCatches.find(it => it.id.toString() === values.createdCatch?.toString()),
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
          ...fishDataEntity,
          createdCatch: fishDataEntity?.createdCatch?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tackleTrackerNewApp.fishData.home.createOrEditLabel" data-cy="FishDataCreateUpdateHeading">
            Create or edit a Fish Data
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="fish-data-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="fish-data-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Water Type" id="fish-data-waterType" name="waterType" data-cy="waterType" type="text" />
              <ValidatedField label="Weight" id="fish-data-weight" name="weight" data-cy="weight" type="text" />
              <ValidatedField id="fish-data-createdCatch" name="createdCatch" data-cy="createdCatch" label="Created Catch" type="select">
                <option value="" key="0" />
                {createdCatches
                  ? createdCatches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/fish-data" replace color="info">
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

export default FishDataUpdate;
