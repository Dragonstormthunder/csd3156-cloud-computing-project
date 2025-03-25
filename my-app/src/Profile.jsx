import React from 'react';
import {Box,
    TextField, 
    Button,
    Paper} from '@mui/material';
import sofaLogo from './assets/sofasogoodicon.png'
import {Link} from 'wouter'
import AppBarComponent from './AppBarComponent.jsx';

const Profile = () => {
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
            <h1>This is Profile</h1>
        </Paper>
    </Box>
    </>
}

export {Profile};