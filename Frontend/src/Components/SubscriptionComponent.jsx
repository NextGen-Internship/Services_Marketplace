import React, { useState } from 'react';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from '../environment.js';
import axios from 'axios';

const SubscriptionComponent = () => {
    const monthlyPriceId = 'price_1OcUNzI2KDxgMJyoxeNLRi93';
  
    const handleCheckout = async (priceId) => {
      const checkout = {
        priceId: priceId,
        cancelUrl: 'http://localhost:3000/canceled',
        successUrl: 'http://localhost:3000/success',
      };
  
      try {
        const response = await axios.post(`http://localhost:8080/api/subscribe/subscription`, checkout);
        const sessionId = response.data.sessionId;
        const stripe = await loadStripe(environment.stripe);
        
        if (stripe) {
          stripe.redirectToCheckout({
            sessionId: sessionId
          });
        }
      } catch (error) {
        console.error('Error during checkout:', error);
      }
    };
  
    return (
      <div className="example-card">
        <h1>Subscribe now!</h1>
        <div>
          <button onClick={() => handleCheckout(monthlyPriceId)}>
            Pay $9.99 per month
          </button>
        </div>
      </div>
    );
  }
  
  export default SubscriptionComponent;