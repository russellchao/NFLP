import React from 'react';
import PostCard from './PostCard';

export default function PostList({ 
    posts, 
    selectedIds, 
    onSelect, 
    onAnalyze 
}) {
    if (!posts || posts.length === 0) {
        return (
            <div className="empty-state">
                <div className="empty-state-icon">💬</div>
                <div className="empty-state-title">No Posts Found</div>
                <p>There are no social posts to display at this time.</p>
            </div>
        );
    }

    return (
        <div className="post-list">
            {posts.map(post => (
                <PostCard
                    key={post.id}
                    post={post}
                    isSelected={selectedIds.includes(post.id)}
                    onSelect={onSelect}
                    onAnalyze={onAnalyze}
                />
            ))}
        </div>
    );
}
