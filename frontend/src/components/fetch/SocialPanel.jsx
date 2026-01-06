import React, { useState } from 'react';

export default function SocialPanel() {
    const [subreddit, setSubreddit] = useState('r/nfl');
    const [sortBy, setSortBy] = useState('Hot');
    const [postLimit, setPostLimit] = useState(100);

    const handleFetch = () => {
        // TODO: Implement fetch logic
        console.log('Fetching Reddit posts:', { subreddit, sortBy, postLimit });
    };

    return (
        <div className="panel">
            <div className="panel-title">💬 Fetch Reddit Posts</div>
            
            <div className="form-group">
                <div className="label">Subreddit:</div>
                <input 
                    type="text" 
                    className="input" 
                    value={subreddit}
                    onChange={(e) => setSubreddit(e.target.value)}
                    placeholder="Enter subreddit name"
                />
            </div>

            <div className="form-group">
                <div className="label">Sort By:</div>
                <select 
                    className="input"
                    value={sortBy}
                    onChange={(e) => setSortBy(e.target.value)}
                >
                    <option>Hot</option>
                    <option>New</option>
                    <option>Top</option>
                </select>
            </div>

            <div className="form-group">
                <div className="label">Post Limit:</div>
                <input 
                    type="number" 
                    className="input" 
                    value={postLimit}
                    onChange={(e) => setPostLimit(e.target.value)}
                    placeholder="Max posts to fetch"
                />
            </div>

            <button className="btn" onClick={handleFetch}>
                🔄 Fetch Reddit Posts
            </button>
        </div>
    );
}
