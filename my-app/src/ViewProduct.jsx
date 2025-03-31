import React, { useState, useEffect } from 'react';
import {Box,
    TextField, 
    Button,
    Avatar,
    Collapse,
    Typography,
    IconButton,
    Fade,
    styled,
    Paper} from '@mui/material';
import {
  Add as AddIcon,
  Remove as RemoveIcon,
  ShoppingCart as ShoppingCartIcon
} from '@mui/icons-material';
import {Link, useLocation} from 'wouter'
import AppBarComponent from './AppBarComponent.jsx';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';

const AddToCartButton = ({ initialQuantity = 0, maxQuantity = 10 }) => {
  const [quantity, setQuantity] = React.useState(initialQuantity);
  const [isExpanded, setIsExpanded] = React.useState(initialQuantity > 0);
  const [, setLocation] = useLocation();

  useEffect(() => {
    if (quantity === 0) {
      setIsExpanded(false);
    }
  }, [quantity]);

  const handleAddToCart = () => {
    setQuantity(1);
    setIsExpanded(true);
  };

  const handleQuantityChange = (e) => {
    const value = Math.max(0, Math.min(maxQuantity, Number(e.target.value)));
    setQuantity(value);
  };

  const incrementQuantity = () => {
    setQuantity(prev => Math.min(maxQuantity, prev + 1));
  };

  const decrementQuantity = () => {
    setQuantity(prev => Math.max(0, prev - 1));
  };

  const handleConfirm = () => {
    if (quantity === 0) {
      setIsExpanded(false);
    }
    console.log(`Quantity set to ${quantity}`);
    setLocation('/Catalogue');
  };

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
      {!isExpanded ? (
        <Button
          variant="contained"
          color="primary"
          startIcon={<ShoppingCartIcon />}
          onClick={handleAddToCart}
          sx={{
            borderRadius: 2,
            px: 3,
            py: 1,
            fontWeight: 'bold',
            textTransform: 'none',
          }}
        >
          Add to Cart
        </Button>
      ) : (
        <Fade in={isExpanded}>
          <Paper elevation={2}>
            <IconButton 
              onClick={decrementQuantity}
              aria-label="Decrease quantity"
            >
              <RemoveIcon fontSize="small" />
            </IconButton>
            
            <TextField
              value={quantity}
              onChange={handleQuantityChange}
              type="number"
              inputProps={{
                min: 0,
                max: maxQuantity,
                style: {
                  textAlign: 'center',
                  width: '40px',
                  padding: '8px 0',
                }
              }}
              variant="standard"
              sx={{
                mx: 1,
                '& .MuiInput-underline:before': {
                  borderBottom: 'none',
                },
                '& .MuiInput-underline:after': {
                  borderBottom: 'none',
                },
              }}
            />
            
            <IconButton
              onClick={incrementQuantity}
              disabled={quantity >= maxQuantity}
              aria-label="Increase quantity"
            >
              <AddIcon fontSize="small" />
            </IconButton>
            
            <Button
              variant="contained"
              color="primary"
              onClick={handleConfirm}
              sx={{
                ml: 1,
                borderRadius: 2,
                px: 2,
                py: 0.5,
                fontWeight: 'bold',
                textTransform: 'none',
              }}
            >
              {quantity === 0 ? 'Remove' : 'Update'}
            </Button>
          </Paper>
        </Fade>
      )}
    </Box>
  );
};


const ViewProduct = () => {
  const productID = window.location.hash.split('#')[1];
  const [open, setOpen] = React.useState(true);

  const handleClick = () => {
    setOpen(!open);
  };

  return <>
    <AppBarComponent/>
    <Box
      sx={{
        display: 'flex',
        flexWrap: 'wrap',
        '& > :not(style)': {
          m: 1,
          width: 650,
          height: '100%',
        },
      }}
    >
        <Paper sx={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {/* Flex container for the image and heading */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <img src={productData.img} alt="product " style={{ width: '400px', height: 'auto' }} />
            <div>
              <h1 style={{ margin: 0, display: 'flex' }}>{`${productData.title}`}</h1>
              <Link href="/Profile">
                <div style={{ display: 'flex', alignItems: 'center', gap: '5px',  cursor: "pointer"}}>
                  <Avatar 
                    alt={`${profileData.name}`}
                    src={`${profileData.profileImage}`}
                    sx={{ width: 24, height: 24 }}
                  />
                  <p style={{ display:'flex', justifyContent: 'start', color: 'grey', margin: 0}}>
                      {`${profileData.username}`}
                  </p>
                </div>
              </Link>
              <p style={{ display: 'flex', fontSize: '1.7em', fontWeight: 'bold' }}>{productData.price}</p>
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-around'}}>
            <p>Description:</p>
            <IconButton 
              size="large"
              color="inherit" 
              onClick={()=>handleClick()}>
              {open ? 
               <ExpandLessIcon/>
              :<ExpandMoreIcon/>} 
            </IconButton>
          </div>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <p style={{ margin: 0, display: 'flex' }}>{productData.description}</p>
          </Collapse>
          
          {/* <Button
            style={{width:'200px', margin: '0 auto'}}
            variant="contained" 
            onClick={() => handleLogin()}
          >
            Add to Cart
          </Button> */}

          <AddToCartButton style={{width:'200px', margin: '0 auto'}}/>
        </Paper>
    </Box>
    </>
}

const productData = {
    img: 'https://images.unsplash.com/photo-1549388604-817d15aa0110',
    price: '$10',
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
    title: 'Bed',
    author: 'swabdesign',
    productNumber: '01'
};

const profileData = 
{
  profileImage: 'https://images.unsplash.com/photo-1511697073354-8db0d2a165dd',
  name: 'Remy Sharp',
  username: 'swabdesign',
  id: '099'
};

export {ViewProduct};