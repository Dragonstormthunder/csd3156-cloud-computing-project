import React from 'react';
import {Box,
    Avatar,
    Button,
    ImageList,
    ImageListItem,
    ImageListItemBar,
    Paper} from '@mui/material';
import {Link, useLocation} from 'wouter'
import AppBarComponent from './AppBarComponent.jsx';

const Profile = () => {
    const [, setLocation] = useLocation();
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
              alt={profileData.name}
              src={profileData.profileImage}
              sx={{ width: 88, height: 88 }}
            />
            <div>
              <h1 style={{ margin: 0 }}>
                {`${profileData.name}`}
              </h1>
              <p style={{ display:'flex', justifyContent: 'start', color: 'grey', margin: 0}}>
                {profileData.username}
              </p>
            </div>
          </div>

          <h3 style={{ display:'flex', justifyContent: 'start', margin: 0}}>Your Top Listings</h3>
          
          <ImageList cols={3} gap={8}>
            {first3ListingsData.map((item) => (
              <ImageListItem 
              key={item.img}
              onClick={()=>setLocation(`/ViewProduct#${item.productNumber}`)}
              style={{ cursor: 'pointer' }}>
                <img
                  srcSet={`${item.img}?w=248&fit=crop&auto=format&dpr=2 2x`}
                  src={`${item.img}?w=248&fit=crop&auto=format`}
                  alt={item.title}
                  loading="lazy"
                />
                <ImageListItemBar
                title={item.title}
                subtitle={
                <div className='subtitile_div'>
                    <span>by: {item.author}</span>
                    <span className='item_price'>{item.price}</span>
                </div>}
                position="below"
              />
              </ImageListItem>
            ))}
          </ImageList>

          <Button
            style={{width:'200px', margin: '0 auto'}}
            component={Link} 
            variant="contained" 
            href="/CreateListings"
            onClick={() => handleLogin()}
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

const profileData = 
{
  profileImage: 'https://images.unsplash.com/photo-1511697073354-8db0d2a165dd',
  name: 'Remy Sharp',
  username: 'swabdesign',
  id: '099'
};

const first3ListingsData = [
  {
    img: 'https://images.unsplash.com/photo-1549388604-817d15aa0110',
    price: '$10',
    title: 'Bed',
    author: 'swabdesign',
    productNumber: '01'
  },
  {
    img: 'https://images.unsplash.com/photo-1525097487452-6278ff080c31',
    price: '$10',
    title: 'Books',
    author: 'swabdesign',
    productNumber: '02'
  },
  {
    img: 'https://images.unsplash.com/photo-1523413651479-597eb2da0ad6',
    price: '$10',
    title: 'Sink',
    author: 'swabdesign',
    productNumber: '03'
  },
];

export {Profile};