import axios from 'axios';
import {
  ICrudSearchAction,
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IVgoitem, defaultValue } from 'app/shared/model/vgoitem.model';

export const ACTION_TYPES = {
  SEARCH_VGOITEMS: 'vgoitem/SEARCH_VGOITEMS',
  FETCH_VGOITEM_LIST: 'vgoitem/FETCH_VGOITEM_LIST',
  FETCH_VGOITEM: 'vgoitem/FETCH_VGOITEM',
  CREATE_VGOITEM: 'vgoitem/CREATE_VGOITEM',
  UPDATE_VGOITEM: 'vgoitem/UPDATE_VGOITEM',
  DELETE_VGOITEM: 'vgoitem/DELETE_VGOITEM',
  RESET: 'vgoitem/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVgoitem>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type VgoitemState = Readonly<typeof initialState>;

// Reducer

export default (state: VgoitemState = initialState, action): VgoitemState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_VGOITEMS):
    case REQUEST(ACTION_TYPES.FETCH_VGOITEM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VGOITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_VGOITEM):
    case REQUEST(ACTION_TYPES.UPDATE_VGOITEM):
    case REQUEST(ACTION_TYPES.DELETE_VGOITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_VGOITEMS):
    case FAILURE(ACTION_TYPES.FETCH_VGOITEM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VGOITEM):
    case FAILURE(ACTION_TYPES.CREATE_VGOITEM):
    case FAILURE(ACTION_TYPES.UPDATE_VGOITEM):
    case FAILURE(ACTION_TYPES.DELETE_VGOITEM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_VGOITEMS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_VGOITEM_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_VGOITEM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_VGOITEM):
    case SUCCESS(ACTION_TYPES.UPDATE_VGOITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_VGOITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/vgoitems';
const apiSearchUrl = 'api/_search/vgoitems';

// Actions

export const getSearchEntities: ICrudSearchAction<IVgoitem> = query => ({
  type: ACTION_TYPES.SEARCH_VGOITEMS,
  payload: axios.get<IVgoitem>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IVgoitem> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_VGOITEM_LIST,
    payload: axios.get<IVgoitem>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IVgoitem> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VGOITEM,
    payload: axios.get<IVgoitem>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IVgoitem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VGOITEM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IVgoitem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VGOITEM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVgoitem> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VGOITEM,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
