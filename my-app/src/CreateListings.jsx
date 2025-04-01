import React, {useState} from 'react';
import {Box,
    TextField, 
    Button,
    Modal,
    Avatar,
    Card,
    CardMedia,
    CardContent,
    Typography,
    IconButton,
    styled,
    Paper} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import {Link} from 'wouter'
import AppBarComponent from './AppBarComponent.jsx';

//creates an overlay that allows u to add information inside

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
  padding: '20px'
};

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

const CreateListings = () => {
  const [open, setOpen] = React.useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const handleDelete = () => {};
  const handleNewListing = () => {};
  const handleEdit = () => {
    // sets curr data in listing
    handleOpen();
  };

  const [productName, setProductName] = React.useState('');
  const [productPrice, setProductPrice] = React.useState('');
  const [productImg, setProductImg] = React.useState('');
  const [productQuantity, setProductQuantity] = React.useState('');

  return <>
    <AppBarComponent/>
    <Box
      sx={{
        display: 'flex',
        flexWrap: 'wrap',
        '& > :not(style)': {
          m: 1,
          width: 650,
          minHeight: 620,
          height: '100%'
        },
      }}
    >
        <Paper sx={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <h1 style={{display: 'flex', margin: 0 }}>Your Listings</h1> 
          <div style={{ display: 'flex', alignItems: 'center', gap: '5px'}}>
            <Avatar 
              alt={profileData.name}
              src={profileData.profileImage}
              sx={{ width: 24, height: 24 }}
            />
            <p style={{ display:'flex', justifyContent: 'start', color: 'grey', margin: 0}}>
                {`${profileData.username}`}
            </p>
            <div style={{ display: 'flex', alignItems: 'center', gap: '5px', width: '100%', justifyContent:'end'}}>
              <Button 
                variant="contained" 
                onClick={() => handleOpen()}
                sx={{
                  borderRadius: 2,
                  px: 3,
                  py: 1,
                  fontWeight: 'bold',
                  textTransform: 'none',
                }}
              >
                Create New Listing
              </Button>
              <Modal
                open={open}
                onClose={() => handleClose()}
              >
                <Box sx={style}>
                  <p>Your Listing</p>
                  <TextField 
                    required 
                    id="outlined-basic" 
                    label="Product Name" 
                    variant="outlined"
                    onChange={(event) => {
                      setProductName(event.target.value);
                    }}
                  />
                  <TextField 
                    required 
                    id="outlined-basic" 
                    label="Product Price" 
                    variant="outlined"
                    onChange={(event) => {
                      setProductPrice(event.target.value);
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
                        onChange={(event) => setProductImg(event.target.files)}
                      />
                    </Button>
                  </div>
                  <TextField 
                    required 
                    id="outlined-basic" 
                    label="Product Name" 
                    variant="outlined"
                    onChange={(event) => {
                      setProductQuantity(event.target.value);
                    }}
                  />
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
                    Add Listing
                  </Button>
                </Box>
              </Modal>
            </div>
          </div>
          
          {first3ListingsData.map((item) => (
            <Card
              key={item.img}
              sx={{display: 'flex'}}
            >
              <Box sx={{ display: 'flex', flexDirection: 'row' }}>
                <CardMedia 
                  component="img" 
                  image={item.img} alt="product " 
                  style={{ display:'flex', width: '100px', height: 'auto' }} 
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
                    {`Quantity: ${item.quantity}`}
                  </Typography>
                </CardContent> 
              </Box>
              <div style={{display: 'flex', alignItems: 'center', width:'100%', justifyContent: 'end'}}>
                <IconButton 
                  size="large"
                  color="inherit" 
                  onClick={()=>handleEdit()}>
                  <EditIcon/>
                </IconButton>
                <IconButton 
                  size="large"
                  color="inherit"
                  style={{color: 'red'}} 
                  onClick={()=>handleDelete()}>
                  <DeleteIcon/>
                </IconButton>
              </div>
            </Card>
          ))}
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

const profileData = 
{
  profileImage: 'https://images.unsplash.com/photo-1511697073354-8db0d2a165dd',
  name: 'Remy Sharp',
  username: 'swabdesign',
  id: '099'
};

export {CreateListings};