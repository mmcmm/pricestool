import React from 'react';
import { DropdownItem, NavItem, NavLink } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  <>
    <NavItem>
      <NavLink tag={Link} to="/entity/vgoitem">
        <FontAwesomeIcon icon="list" />
        <span>VGO</span>
      </NavLink>
    </NavItem>
    <NavItem>
      <NavLink tag={Link} to="/">
        <FontAwesomeIcon icon="list" />
        <span>CSGO</span>
      </NavLink>
    </NavItem>
  </>
);
