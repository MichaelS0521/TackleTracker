import './create.scss';
import React from 'react';

const Create = () => {
  return (
    <div>
      <form>
        <h3>Lets see what you caught!</h3>
        <label className="fish-type">Type of Fish</label>
        <div>
          <input type="text" placeholder="Fish Species..." id="fishType" />
        </div>

        <label className="fish-type">Weight (lbs.)</label>
        <div>
          <input type="text" placeholder="Weight(lbs.)" id="fish" />
        </div>
        <button type="button" className="submit-button">
          Submit
        </button>
      </form>
    </div>
  );
};

export default Create;
