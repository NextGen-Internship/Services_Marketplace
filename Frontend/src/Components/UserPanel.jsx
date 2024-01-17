import React from 'react';
import '../styles/UserPanel.css';

const UserPanel = () => {
    return (
        <div className='user-panel'>
            <button className="provider-button" onClick={onBecomeProvider}>
                Become a provider
            </button>
        </div>
    );
};

export default UserPanel;