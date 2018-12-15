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
      this.props.reset();
      this.setState({ activePage: 1 }, () => {
        const { activePage, itemsPerPage, sort, order, search } = this.state;
        this.props.getSearchEntities(search, activePage - 1, itemsPerPage, `${sort},${order}`);
      });
    }
  };

  clear = () => {
    this.props.reset();
    this.setState({ search: '', activePage: 1 }, () => {
      this.props.getEntities();
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
    const { activePage, itemsPerPage, sort, order, search } = this.state;
    if (search) {
      this.props.getSearchEntities(search, activePage - 1, itemsPerPage, `${sort},${order}`);
    } else {
      this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
    }
  };

  money = (amount: number) =>
    amount
      .toFixed(2)
      .replace(/\d(?=(\d{3})+\.)/g, '$&,')
      .replace('.00', '');

  render() {
    const { vgoitemList, match } = this.props;
    return (
      <div>
        <h2 id="vgoitem-heading">Vgoitems</h2>
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
                  <th className="hand" onClick={this.sort('name')}>
                    Name <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('op7day')}>
                    S. Price <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('op30day')}>
                    Op 30 Day <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('sales')}>
                    Sales <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('qty')}>
                    Qty <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('minPrice')}>
                    Min Price <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {vgoitemList.map((vgoitem, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Link to={`${match.url}/${vgoitem.id}`}>{vgoitem.name}</Link>
                    </td>
                    <td>${this.money(vgoitem.op7day / 100)}</td>
                    <td>${this.money(vgoitem.op30day / 100)}</td>
                    <td>${vgoitem.sales}</td>
                    <td>${vgoitem.qty}</td>
                    <td>${this.money(vgoitem.minPrice / 100)}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <a href={`https://opskins.com/?app=1912_1&loc=shop_search&search_item=${vgoitem.name}&sort=lh`} target="_blank">
                          <FontAwesomeIcon icon="eye" />
                        </a>
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
