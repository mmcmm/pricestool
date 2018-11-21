import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './vgoitem.reducer';
import { IVgoitem } from 'app/shared/model/vgoitem.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IVgoitemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IVgoitemUpdateState {
  isNew: boolean;
}

export class VgoitemUpdate extends React.Component<IVgoitemUpdateProps, IVgoitemUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { vgoitemEntity } = this.props;
      const entity = {
        ...vgoitemEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/vgoitem');
  };

  render() {
    const { vgoitemEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="pricestoolApp.vgoitem.home.createOrEditLabel">Create or edit a Vgoitem</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : vgoitemEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="vgoitem-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField
                    id="vgoitem-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 255, errorMessage: 'This field cannot be longer than 255 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="categoryLabel" for="category">
                    Category
                  </Label>
                  <AvField
                    id="vgoitem-category"
                    type="text"
                    name="category"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 255, errorMessage: 'This field cannot be longer than 255 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="rarityLabel" for="rarity">
                    Rarity
                  </Label>
                  <AvField
                    id="vgoitem-rarity"
                    type="text"
                    name="rarity"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 255, errorMessage: 'This field cannot be longer than 255 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="typeLabel" for="type">
                    Type
                  </Label>
                  <AvField
                    id="vgoitem-type"
                    type="text"
                    name="type"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 255, errorMessage: 'This field cannot be longer than 255 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="colorLabel" for="color">
                    Color
                  </Label>
                  <AvField
                    id="vgoitem-color"
                    type="text"
                    name="color"
                    validate={{
                      maxLength: { value: 20, errorMessage: 'This field cannot be longer than 20 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="image300pxLabel" for="image300px">
                    Image 300 Px
                  </Label>
                  <AvField
                    id="vgoitem-image300px"
                    type="text"
                    name="image300px"
                    validate={{
                      maxLength: { value: 255, errorMessage: 'This field cannot be longer than 255 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="image600pxLabel" for="image600px">
                    Image 600 Px
                  </Label>
                  <AvField
                    id="vgoitem-image600px"
                    type="text"
                    name="image600px"
                    validate={{
                      maxLength: { value: 255, errorMessage: 'This field cannot be longer than 255 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="suggestedPriceLabel" for="suggestedPrice">
                    Suggested Price
                  </Label>
                  <AvField
                    id="vgoitem-suggestedPrice"
                    type="string"
                    className="form-control"
                    name="suggestedPrice"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="suggestedPrice7dayLabel" for="suggestedPrice7day">
                    Suggested Price 7 Day
                  </Label>
                  <AvField id="vgoitem-suggestedPrice7day" type="string" className="form-control" name="suggestedPrice7day" />
                </AvGroup>
                <AvGroup>
                  <Label id="suggestedPrice30dayLabel" for="suggestedPrice30day">
                    Suggested Price 30 Day
                  </Label>
                  <AvField id="vgoitem-suggestedPrice30day" type="string" className="form-control" name="suggestedPrice30day" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/vgoitem" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  vgoitemEntity: storeState.vgoitem.entity,
  loading: storeState.vgoitem.loading,
  updating: storeState.vgoitem.updating,
  updateSuccess: storeState.vgoitem.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VgoitemUpdate);
