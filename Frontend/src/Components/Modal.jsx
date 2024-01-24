import React from 'react'
import '../styles/Modal.css'

const data = {
    id: 1,
    src: 'Subscription',
    title: 'Become Provider',
    description: 'Unlock endless possibilities by subscribing to our Provider plan! As a provider, you gain the ability to effortlessly add and showcase an unlimited number of services. Seize the opportunity to share your expertise and offerings with a broader audience. Elevate your profile, expand your reach, and start providing services seamlessly with our Provider Subscription.',
    price: '9.99'
};

function Modal({ setOpenModal }) {
    
    const checkout = (plan) => {
        // fetch("http://localhost:8080/api/v1/create-subscription-checkout-session", {
        //     method: "POST",
        //     headers: {
        //         "Content-Type": "application/json",
        //     },
        //     mode: "cors",
        //     body: JSON.stringify({ plan: plan, customerId: userId }),
        // })
        //     .then((res) => {
        //         if (res.ok) return res.json();
        //         console.log(res);
        //         return res.json().then((json) => Promise.reject(json));
        //     })
        //     .then(({ session }) => {
        //         window.location = session.url;
        //     })
        //     .catch((e) => {
        //         console.log(e.error);
        //     });
    };


    return (
        <div className="modalBackground">
            <div className="modalContainer">
                <div className="titleCloseBtn">
                    <button
                        onClick={() => {
                            setOpenModal(false);
                        }}
                    >
                        X
                    </button>
                </div>
                <div className="title">
                    <h1>{data.title}</h1>
                </div>
                <div className="body">
                    {/* <p>{data.description}</p> */}
                    <p>${data.price}</p>
                </div>
                <div className="footer">
                    <button onClick={() => checkout(Number(data.price))}>
                        Subscribe
                    </button>
                </div>
            </div>
        </div>
    )
}

export default Modal;
