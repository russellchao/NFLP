import React from 'react';
import NewsPanel from './NewsPanel';
import SocialPanel from './SocialPanel';
import FetchJobStatus from './FetchJobStatus';
import '../../styles/fetch.css';

export default function FetchContent() {
    return (
        <div className="fetch-container">
            <div className="page-header">
                <h1 className="page-title">Fetch New Content</h1>
                <p>This is where you can collect NFL news articles and social media posts for analysis</p>
            </div>

            <div className="content">
                <NewsPanel />
                <SocialPanel />
                <FetchJobStatus />
            </div>
        </div>
    );
}