import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities, reset } from './vgoitem.reducer';
import { IVgoitem } from 'app/shared/model/vgoitem.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IVgoitemProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IVgoitemState extends IPaginationBaseState {
  search: string;
}

export class Vgoitem extends React.Component<IVgoitemProps, IVgoitemState> {
  state: IVgoitemState = {
    search: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.props.getEntities();
    this.setState({
      search: ''
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { vgoitemList, match } = this.props;
    return (
      <div>
        <h2 id="vgoitem-heading">
          Vgoitems
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Vgoitem
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput type="text" name="search" value={this.state.search} onChange={this.handleSearch} placeholder="Search" />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    ID <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('name')}>
                    Name <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('category')}>
                    Category <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('rarity')}>
                    Rarity <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('type')}>
                    Type <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('color')}>
                    Color <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('image300px')}>
                    Image 300 Px <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('image600px')}>
                    Image 600 Px <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('suggestedPrice')}>
                    Suggested Price <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('suggestedPrice7day')}>
                    Suggested Price 7 Day <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('suggestedPrice30day')}>
                    Suggested Price 30 Day <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {vgoitemList.map((vgoitem, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${vgoitem.id}`} color="link" size="sm">
                        {vgoitem.id}
                      </Button>
                    </td>
                    <td>{vgoitem.name}</td>
                    <td>{vgoitem.category}</td>
                    <td>{vgoitem.rarity}</td>
                    <td>{vgoitem.type}</td>
                    <td>{vgoitem.color}</td>
                    <td>{vgoitem.image300px}</td>
                    <td>{vgoitem.image600px}</td>
                    <td>{vgoitem.suggestedPrice}</td>
                    <td>{vgoitem.suggestedPrice7day}</td>
                    <td>{vgoitem.suggestedPrice30day}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${vgoitem.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${vgoitem.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${vgoitem.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ vgoitem }: IRootState) => ({
  vgoitemList: vgoitem.entities,
  totalItems: vgoitem.totalItems,
  links: vgoitem.links,
  entity: vgoitem.entity,
  updateSuccess: vgoitem.updateSuccess
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Vgoitem);