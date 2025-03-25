import React from 'react';
import {Box,
    TextField, 
    Button,
    Paper} from '@mui/material';
import sofaLogo from './assets/sofasogoodicon.png'
import {Link} from 'wouter'
import './style/CreateAccount.css'

const CreateAccount = () => {
    return <>
    <Box
      sx={{
        display: 'flex',
        flexWrap: 'wrap',
        padding: '20px',
        '& > :not(style)': {
          m: 1,
          width: 650,
          height: 800,
        },
      }}
    >
        <Paper sx={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {/* Flex container for the image and heading */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <img src={sofaLogo} className="logo" alt="Sofasogood logo" style={{ width: '100px', height: 'auto' }} />
            <h1>Become a member at SofaSoGood</h1>
          </div>
          <div style={{ display: 'flex', gap: '20px' }}>
            <TextField required id="outlined-basic" label="First Name" variant="outlined" />
            <TextField required id="outlined-basic" label="Last Name" variant="outlined" />
          </div>
          <TextField required id="outlined-basic" label="E-mail address" variant="outlined" />
          <TextField required id="outlined-basic" label="Birthday" variant="outlined" />
          <TextField required id="outlined-basic" label="Username" variant="outlined" />
          <TextField  required id="outlined-basic" label="Password" variant="outlined" />
          <TextField  required id="outlined-basic" label="Confirm Password" variant="outlined" />
          <div>
            <Button component={Link} variant="contained" href="/">
              Create Account
            </Button>
          </div>
        </Paper>
    </Box>
    </>
}

export {CreateAccount};