import React from 'react';
import {Box,
    TextField, 
    Button,
    Paper} from '@mui/material';
import sofaLogo from './assets/sofasogoodicon.png'
import {Link} from 'wouter'
import AppBarComponent from './AppBarComponent.jsx';

const ViewProduct = () => {
    const productName = window.location.hash.split('#')[1];

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
        <Paper>
            <h1>{productName}</h1>
        </Paper>
    </Box>
    </>
}

export {ViewProduct};