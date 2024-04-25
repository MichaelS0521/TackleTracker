import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-profile">
        User Profile
      </MenuItem>
      <MenuItem icon="asterisk" to="/created-catch">
        Created Catch
      </MenuItem>
      <MenuItem icon="asterisk" to="/fish-data">
        Fish Data
      </MenuItem>
      <MenuItem icon="asterisk" to="/weather-data">
        Weather Data
      </MenuItem>
      <MenuItem icon="asterisk" to="/bait-data">
        Bait Data
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
