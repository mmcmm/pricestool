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
              <span id="op7day">Op 7 Day</span>
            </dt>
            <dd>{vgoitemEntity.op7day}</dd>
            <dt>
              <span id="op30day">Op 30 Day</span>
            </dt>
            <dd>{vgoitemEntity.op30day}</dd>
            <dt>
              <span id="sales">Sales</span>
            </dt>
            <dd>{vgoitemEntity.sales}</dd>
            <dt>
              <span id="qty">Qty</span>
            </dt>
            <dd>{vgoitemEntity.qty}</dd>
            <dt>
              <span id="minPrice">Min Price</span>
            </dt>
            <dd>{vgoitemEntity.minPrice}</dd>
          </dl>
          <Button tag={Link} to="/entity/vgoitem" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
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
