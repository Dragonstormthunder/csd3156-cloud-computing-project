import * as React from 'react';
// import { useRef } from 'react';
import {Box,
    TextField, 
    Button,
    Paper} from '@mui/material';
import sofaLogo from './assets/sofasogoodicon.png'
import {Link} from 'wouter'
import './style/Login.css'

const Login = () => {
  const [failedLogin, setFailedLogin] = React.useState(false);
  const [userName, setUserName] = React.useState('');
  const [password, setPassword] = React.useState('');

  const handleLogin = (event) => {
    // if fail
    // setFailedLogin(true);
  };

    return <>
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
            <h1>Welcome to SofaSoGood</h1>
            <img src={sofaLogo} className="logo" alt="Sofasogood logo" />
            {/* <h3>Login</h3> */}
            <div>
              <div style={{ padding: '3px' }}>
                <TextField
                   id="outlined-basic" 
                   label="Username" 
                   variant="outlined"
                   onChange={(event) => {
                    setUserName(event.target.value);
                  }}
                />
              </div>
              <div style={{ padding: '3px' }}>
                <TextField 
                  id="outlined-basic" 
                  label="Password" 
                  variant="outlined" 
                  onChange={(event) => {
                    setPassword(event.target.value);
                  }}
                />
              </div>
              <div sx={{ display: 'flex', flexDirection: 'column', gap: '20px' }} style={{padding: '10px'}}>
                <Button 
                  component={Link} 
                  variant="contained" 
                  href="/Catalogue"
                  onClick={() => handleLogin()}
                  sx={{
                    borderRadius: 2,
                    px: 3,
                    py: 1,
                    fontWeight: 'bold',
                    textTransform: 'none',
                  }}
                >
                  Login
                </Button>
                <Button 
                  component={Link} 
                  variant="contained" 
                  href="/CreateAccount"
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
            </div>
            <div>
            {
              failedLogin && (
                <Button 
                  variant="contained" 
                  onClick={() => setFailedLogin(false)}
                  sx={{
                    borderRadius: 2,
                    px: 3,
                    py: 1,
                    fontWeight: 'bold',
                    textTransform: 'none',
                  }}
                >
                  Retry?
                </Button>
            )}
            </div>
        </Paper>
    </Box>
    </>
}

export {Login};