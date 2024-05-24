import './topBaits.scss';

import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';

interface Baits {
  id: number;
  type: string;
  color: string;
  hard: boolean;
  soft: boolean;
}

interface Fish {
  id: number;
  name: string;
  url: string;
}

const RecentCatch = () => {
  const [baits, setBaits] = useState<Baits[]>([]);
  const [fish, setFish] = useState<Fish[]>([]);
  const { id } = useParams<{ id: string }>();

  const fetchBaits = async () => {
    try {
      console.log('Fetching baits...');
      const response = await axios.get('/api/bait-data'); // Using axios for consistency
      if (response.status !== 200) {
        throw new Error('Failed to fetch baits');
      }
      console.log('Fetched baits:', response.data);
      setBaits(response.data);
    } catch (error) {
      console.error('Error fetching baits:', error);
    }
  };

  const fetchFish = async () => {
    const options = {
      method: 'GET',
      url: 'https://fish-species.p.rapidapi.com/fish_api/fishes',
      headers: {
        'X-RapidAPI-Key': 'ac2d9edcf9msh2615e0c554bdc22p1ffba8jsnc46c9bfcad05',
        'X-RapidAPI-Host': 'fish-species.p.rapidapi.com',
      },
    };

    try {
      console.log('Fetching fishes...');
      const response = await axios.request(options);
      console.log('Fetched fishes response:', response);
      if (response.status !== 200) {
        throw new Error('Failed to fetch fishes');
      }
      setFishes(response.data as Fish[]);
      console.log('Fetched fishes:', response.data);
    } catch (error) {
      console.error('Error fetching fishes:', error);
    }
  };

  useEffect(() => {
    fetchBaits();
    fetchFish();
  }, []);

  return (
    <div>
      <div className="baits-container">
        {baits.length > 5 ? (
          baits.map(bait => (
            <div className="baits-item" key={bait.id}>
              <p>Type: {bait.type}</p>
              <p>Color: {bait.color}</p>
              <p>Hard: {bait.hard ? 'Yes' : 'No'}</p>
              <p>Soft: {bait.soft ? 'Yes' : 'No'}</p>
            </div>
          ))
        ) : (
          <p>No baits found or loading...</p>
        )}
      </div>

      <div className="fishes-container">
        {fish.length > 0 ? (
          fish.map(fish => (
            <div className="fishes-item" key={fish.id}>
              <p>Name: {fish.name}</p>
              <p>Family: {fish.url}</p>
              <p>Order: {fish.id}</p>
              {/* Add other fish properties as needed */}
            </div>
          ))
        ) : (
          <p>No fishes found or loading...</p>
        )}
      </div>
    </div>
  );
};

export default RecentCatch;
