import React, {useState} from 'react';
import {Box,
    TextField, 
    Button,
    Card,
    CardMedia,
    CardContent,
    CardActionArea,
    Typography,
    IconButton,
    Paper} from '@mui/material';
import {useLocation} from 'wouter'
import AppBarComponent from './AppBarComponent.jsx';

const Cart = () => {
  const [total, setTotal] = useState(30);
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
          <h1 style={{display: 'flex', margin: 0 }}>Your Cart</h1>
          {first3ListingsData.map((item) => (
            <Card
              key={item.img}
              sx={{display: 'flex'}}
            >
              <CardActionArea
                onClick={()=>setLocation(`/ViewProduct#${item.productNumber}`)}
              >
                <Box sx={{ display: 'flex', flexDirection: 'row' }}>
                  <CardMedia 
                    component="img" 
                    image={item.img} alt="product " 
                    style={{ display:'flex', width: '100px', height: '100px' }} 
                  />
                  <CardContent >
                    <Typography component="div" variant="h5">
                      {item.title}
                    </Typography>
                    <Typography
                      variant="subtitle1"
                      component="div"
                      sx={{ color: 'text.secondary' }}
                    >
                      {item.price}
                    </Typography>
                  </CardContent> 
                  <div style={{display: 'flex', alignItems: 'center', width:'100%', justifyContent: 'end', marginRight: '20px'}}>
                    <Typography
                      variant="subtitle1"
                      component="div"
                      sx={{ color: 'text.secondary' }}
                    >
                      Qty: 1
                    </Typography>
                  </div>
                </Box>
              </CardActionArea>
            </Card>
          ))}
          <p style={{display: 'flex'}}>Total: ${total}</p>
          <div style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
            <Button 
              variant="contained" 
              onClick={() => handleNewListing()}
              sx={{
                borderRadius: 2,
                px: 3,
                py: 1,
                fontWeight: 'bold',
                textTransform: 'none',
              }}
            >
              Proceed to Checkout
            </Button>
          </div>
        </Paper>
    </Box>
    </>
}
const first3ListingsData = [
  {
    img: 'https://images.unsplash.com/photo-1549388604-817d15aa0110',
    price: '$10',
    title: 'Bed',
    author: 'swabdesign',
    productNumber: '01',
    quantity: '99',
  },
  {
    img: 'https://images.unsplash.com/photo-1525097487452-6278ff080c31',
    price: '$10',
    title: 'Books',
    author: 'swabdesign',
    productNumber: '02',
    quantity: '99',
  },
  {
    img: 'https://images.unsplash.com/photo-1523413651479-597eb2da0ad6',
    price: '$10',
    title: 'Sink',
    author: 'swabdesign',
    productNumber: '03',
    quantity: '99',
  },
];
export {Cart};