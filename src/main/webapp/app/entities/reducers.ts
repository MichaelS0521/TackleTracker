import userProfile from 'app/entities/user-profile/user-profile.reducer';
import createdCatch from 'app/entities/created-catch/created-catch.reducer';
import fishData from 'app/entities/fish-data/fish-data.reducer';
import weatherData from 'app/entities/weather-data/weather-data.reducer';
import baitData from 'app/entities/bait-data/bait-data.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  createdCatch,
  fishData,
  weatherData,
  baitData,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
