/*!************************************************************************
 * \file Profile.jsx
* \author	 Kenzie Lim  | kenzie.l\@digipen.edu
 * \par Course: CSD3156
 * \date 25/03/2025
 * \brief
 * This file defines the frontend for Profile Page.
 *
 * Copyright 2025 DigiPen Institute of Technology Singapore All Rights Reserved
 **************************************************************************/
import React, {useState, useEffect} from 'react';
import {Box,
    Avatar,
    Button,
    ImageList,
    ImageListItem,
    ImageListItemBar,
    Paper} from '@mui/material';
import {Link, useLocation} from 'wouter'
import AppBarComponent from './AppBarComponent.jsx';
import { PHP_URL } from "./AppInclude.jsx";
import axios from 'axios';

const Profile = () => {
    const profileID = sessionStorage.getItem('persistedId');
    const [, setLocation] = useLocation();
    const [open, setOpen] = React.useState(true);
    const [profile, setProfile] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchData = async() => {
      try {
        setLoading(true);
        const {data} = await axios.get(`${PHP_URL}/GetProfileInfo.php`, {
          params: {
            ID: profileID
          }
        });
        setProfile(data);
      } catch (err) {
        setError(err.message || 'Failed to fetch product');
      } finally {
        setLoading(false);
      }
    };

    useEffect(() => {
      if (profileID) {
        fetchData();
        // console.log('in profile');
      }
      
    }, [profileID]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!profile) return <div>No profile found</div>;

    return <>
    <AppBarComponent/>
    <Box
      sx={{
        display: 'flex',
        flexWrap: 'wrap',
        '& > :not(style)': {
          m: 1,
          width: 650,
          height: 620,
        },
      }}
    >
        <Paper sx={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {/* Flex container for the image and heading */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <Avatar 
              alt={profile[0].Name}
              src={profile[0].PFP}
              sx={{ width: 88, height: 88 }}
            />
            <div>
              <h1 style={{ margin: 0 }}>
                {`${profile[0].Name}`}
              </h1>
              <p style={{ display:'flex', justifyContent: 'start', color: 'grey', margin: 0}}>
                {profile[0].Name}
              </p>
            </div>
          </div>

          <h3 style={{ display:'flex', justifyContent: 'start', margin: 0}}>Your Top Listings</h3>
          
          <ImageList cols={3} gap={8}>
            {profile.slice(0,3).map((item) => (
              <ImageListItem 
              key={item.InventoryID}
              onClick={()=>setLocation(`/ViewProduct#${item.InventoryID}`)}
              style={{ cursor: 'pointer' }}>
                <img
                  srcSet={`${item.InventoryImage}`}
                  src={`${item.InventoryImage}`}
                  alt={item.InventoryName}
                  loading="lazy"
                />
                <ImageListItemBar
                title={item.InventoryName}
                subtitle={
                <div className='subtitile_div'>
                    <span>by: {item.Name}</span>
                    <span className='item_price'>{`\$${item.Inventoryprice}`}</span>
                </div>}
                position="below"
              />
              </ImageListItem>
            ))}
          </ImageList>

          <Button
            style={{width:'200px', margin: '0 auto'}}
            variant="contained"
            onClick={()=>setLocation(`/CreateListings/${profileID}`)}
            sx={{
              borderRadius: 2,
              px: 3,
              py: 1,
              fontWeight: 'bold',
              textTransform: 'none',
            }}
            color='primary'
          >
            View All Listings
          </Button>
        </Paper>
    </Box>
    </>
}

export {Profile};