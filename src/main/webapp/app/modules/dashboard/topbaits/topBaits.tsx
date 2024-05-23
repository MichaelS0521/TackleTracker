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

const TopBaits = () => {
  const [baits, setBaits] = useState<Baits[]>([]);
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

  useEffect(() => {
    fetchBaits();
  }, []);

  return (
    <div className="baits-container">
      {baits.length > 0 ? (
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
  );
};

export default TopBaits;
