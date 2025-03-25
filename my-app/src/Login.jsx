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
                <TextField id="outlined-basic" label="Username" variant="outlined" />
            </div>
            <div>
                <TextField id="outlined-basic" label="Password" variant="outlined" />
            </div>
            <div sx={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
              <Button component={Link} variant="contained" href="/Catalogue">
              Login
              </Button>
              <Button component={Link} variant="contained" href="/CreateAccount">
              Create Account
              </Button>
            </div>
            <div>
            {
              failedLogin && (
                <Button variant="contained" onClick={() => setFailedLogin(false)}>
                  Retry?
                </Button>
            )}
            </div>
        </Paper>
    </Box>
    </>
}

export {Login};