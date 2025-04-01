import React from 'react';
import {Box,
    ImageList,
    ImageListItem,
    ImageListItemBar
} from '@mui/material';
import './style/Catalogue.css'
import AppBarComponent from './AppBarComponent.jsx';
import {Link, useLocation} from 'wouter'

const Catalogue = () => {
  const [, setLocation] = useLocation();

    return <>
    <AppBarComponent/>
    <ImageList cols={5} gap={8}>
      {dummyData.map((item) => (
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
          subtitle={<div className='subtitile_div'>
              <span>by: {item.author}</span>
              <span className='item_price'>{item.price}</span>
              </div>}
          position="below"
        />
        </ImageListItem>
      ))}
    </ImageList>
    </>
}

const dummyData = [
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
      author: 'Pavel Nekoranec',
      productNumber: '02'
    },
    {
      img: 'https://images.unsplash.com/photo-1523413651479-597eb2da0ad6',
      price: '$10',
      title: 'Sink',
      author: 'Charles Deluvio',
      productNumber: '03'
    },
    {
      img: 'https://images.unsplash.com/photo-1563298723-dcfebaa392e3',
      price: '$10',
      title: 'Kitchen',
      author: 'Christian Mackie',
    },
    {
      img: 'https://images.unsplash.com/photo-1588436706487-9d55d73a39e3',
      price: '$10',
      title: 'Blinds',
      author: 'Darren Richardson',
    },
    {
      img: 'https://images.unsplash.com/photo-1574180045827-681f8a1a9622',
      price: '$10',
      title: 'Chairs',
      author: 'Taylor Simpson',
    },
    {
      img: 'https://images.unsplash.com/photo-1530731141654-5993c3016c77',
      price: '$10',
      title: 'Laptop',
      author: 'Ben Kolde',
    },
    {
      img: 'https://images.unsplash.com/photo-1481277542470-605612bd2d61',
      price: '$10',
      title: 'Doors',
      author: 'Philipp Berndt',
    },
    {
      img: 'https://images.unsplash.com/photo-1517487881594-2787fef5ebf7',
      price: '$10',
      title: 'Coffee',
      author: 'Jen P.',
    },
    {
      img: 'https://images.unsplash.com/photo-1516455207990-7a41ce80f7ee',
      price: '$10',
      title: 'Storage',
      author: 'Douglas Sheppard',
    },
    {
      img: 'https://images.unsplash.com/photo-1597262975002-c5c3b14bbd62',
      price: '$10',
      title: 'Candle',
      author: 'Fi Bell',
    },
    {
      img: 'https://images.unsplash.com/photo-1519710164239-da123dc03ef4',
      price: '$10',
      title: 'Coffee table',
      author: 'Hutomo Abrianto',
    },
  ];

export {Catalogue};