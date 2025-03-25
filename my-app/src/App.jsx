import { useState } from 'react'
import  {Catalogue} from './Catalogue.jsx'
import  {Login} from './Login.jsx'
import { CreateAccount } from './CreateAccount.jsx'
import { Profile } from './Profile.jsx'
import {Dashboard} from './Dashboard.jsx'
import { ViewProduct } from './ViewProduct.jsx'
import { Cart } from './Cart.jsx'
import {Route, Router} from 'wouter'


function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Router>
        {/* <HomeButton />  */}
        <Route path="/" component={Login} />
        <Route path="/Catalogue" component={Catalogue} />
        <Route path="/CreateAccount" component={CreateAccount} />
        <Route path="/Profile" component={Profile} />
        <Route path="/Dashboard" component={Dashboard} />
        <Route path="/ViewProduct" component={ViewProduct} />
        <Route path="/Cart" component={Cart} />
      </Router>
    </>
  )
}

export default App
