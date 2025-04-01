import React from 'react';
import {Box,
    TextField, 
    Button,
    styled,
    Paper} from '@mui/material';
import sofaLogo from './assets/sofasogoodicon.png'
import {Link} from 'wouter'
import './style/CreateAccount.css'
import CloudUploadIcon from '@mui/icons-material/CloudUpload';

const VisuallyHiddenInput = styled('input')({
  clip: 'rect(0 0 0 0)',
  clipPath: 'inset(50%)',
  height: 1,
  overflow: 'hidden',
  position: 'absolute',
  bottom: 0,
  left: 0,
  whiteSpace: 'nowrap',
  width: 1,
});

const CreateAccount = () => {
  const [firstName, setFirstName] = React.useState('');
  const [lastName, setLastName] = React.useState('');
  const [userName, setUserName] = React.useState('');
  const [password1, setPassword1] = React.useState('');
  const [password2, setPassword2] = React.useState('');
  const [img, setImg] = React.useState('');

    return <>
    <Box
      sx={{
        display: 'flex',
        flexWrap: 'wrap',
        padding: '20px',
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
            <img src={sofaLogo} className="logo" alt="Sofasogood logo" style={{ width: '100px', height: 'auto' }} />
            <h1>Become a member at SofaSoGood</h1>
          </div>

          <div style={{ display: 'flex', gap: '20px' }}>
            <TextField 
              required 
              id="outlined-basic" 
              label="First Name" 
              variant="outlined"
              onChange={(event) => {
                setFirstName(event.target.value);
              }}
            />
            <TextField 
              required 
              id="outlined-basic" 
              label="Last Name" 
              variant="outlined" 
              onChange={(event) => {
                setLastName(event.target.value);
              }}
            />
          </div>

          <TextField 
            required 
            id="outlined-basic" 
            label="Username" 
            variant="outlined" 
            onChange={(event) => {
              setUserName(event.target.value);
            }}
          />
          <TextField  
            required 
            id="outlined-basic" 
            label="Password" 
            variant="outlined" 
            onChange={(event) => {
              setPassword1(event.target.value);
            }}
          />
          <TextField  
            required
            id="outlined-basic" 
            label="Confirm Password" 
            variant="outlined"
            onChange={(event) => {
              setPassword2(event.target.value);
            }} 
          />
          <div style={{ display: 'flex'}}>
            <Button
              component="label"
              role={undefined}
              variant="contained"
              tabIndex={-1}
              startIcon={<CloudUploadIcon />}
              sx={{
                borderRadius: 2,
                px: 3,
                py: 1,
                fontWeight: 'bold',
                textTransform: 'none'
              }}
            >
              Upload Images
              <VisuallyHiddenInput
                type="file"
                onChange={(event) => setImg(event.target.files)}
              />
            </Button>
          </div>
          <div>
            <Button 
              component={Link} 
              variant="contained" 
              href="/"
              sx={{
                borderRadius: 2,
                px: 3,
                py: 1,
                fontWeight: 'bold',
                textTransform: 'none',
              }}
            >
              Create Account
            </Button>
          </div>
        </Paper>
    </Box>
    </>
}

export {CreateAccount};