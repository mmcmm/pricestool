import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './vgoitem.reducer';
import { IVgoitem } from 'app/shared/model/vgoitem.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVgoitemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class VgoitemDetail extends React.Component<IVgoitemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { vgoitemEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Vgoitem [<b>{vgoitemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{vgoitemEntity.name}</dd>
            <dt>
              <span id="category">Category</span>
            </dt>
            <dd>{vgoitemEntity.category}</dd>
            <dt>
              <span id="rarity">Rarity</span>
            </dt>
            <dd>{vgoitemEntity.rarity}</dd>
            <dt>
              <span id="type">Type</span>
            </dt>
            <dd>{vgoitemEntity.type}</dd>
            <dt>
              <span id="color">Color</span>
            </dt>
            <dd>{vgoitemEntity.color}</dd>
            <dt>
              <span id="image300px">Image 300 Px</span>
            </dt>
            <dd>{vgoitemEntity.image300px}</dd>
            <dt>
              <span id="image600px">Image 600 Px</span>
            </dt>
            <dd>{vgoitemEntity.image600px}</dd>
            <dt>
              <span id="suggestedPrice">Suggested Price</span>
            </dt>
            <dd>{vgoitemEntity.suggestedPrice}</dd>
            <dt>
              <span id="suggestedPrice7day">Suggested Price 7 Day</span>
            </dt>
            <dd>{vgoitemEntity.suggestedPrice7day}</dd>
            <dt>
              <span id="suggestedPrice30day">Suggested Price 30 Day</span>
            </dt>
            <dd>{vgoitemEntity.suggestedPrice30day}</dd>
          </dl>
          <Button tag={Link} to="/entity/vgoitem" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/vgoitem/${vgoitemEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ vgoitem }: IRootState) => ({
  vgoitemEntity: vgoitem.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VgoitemDetail);
