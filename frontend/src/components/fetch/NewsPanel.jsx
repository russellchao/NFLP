import React, { useState } from 'react';
import * as newsService from '../../services/newsService';
import LoadingSpinner from '../common/LoadingSpinner';

export default function NewsPanel() {
    const [maxArticles, setMaxArticles] = useState('');
    const [fetchLoading, setFetchLoading] = useState(false);

    const handleFetch = (e) => {
        e.preventDefault();
        console.log('Fetching news with limit:', maxArticles);

        const fetchNews = async () => {
            try {
                const fetchJob = await newsService.fetchNews(maxArticles);
                console.log('Fetch job started:', fetchJob);
                setFetchLoading(true);
            } catch (error) {
                console.error('Error fetching news:', error);
            } finally {
                setFetchLoading(false);
            }
        };

        fetchNews();
    };

    return (
        <div className="panel">
            <div className="panel-title">📰 Fetch News</div>
            
            <form onSubmit={handleFetch}>
                <div className="form-group">
                    <div className="label">News articles are fetched from newsapi.org</div>
                </div>

                <div className="form-group">
                    <div className="label">Max articles:</div>
                    <input 
                        type="number" 
                        className="input" 
                        value={maxArticles}
                        onChange={(e) => {
                            const value = parseInt(e.target.value);
                            if (value <= 10 || e.target.value === '') {
                                setMaxArticles(e.target.value);
                            }
                        }}
                        min="1"
                        max="10"
                        placeholder="Enter the max number of articles to fetch (max 10)"
                        required
                    />
                </div>

                <button type="submit" className="btn">
                    🔄 Fetch Latest News
                </button>

                {fetchLoading && <LoadingSpinner />}
            </form>

        </div>
    );
}
