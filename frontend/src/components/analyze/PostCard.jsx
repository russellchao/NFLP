import React from 'react';
import '../../styles/PostCard.css';

export default function PostCard({ post, isSelected, onSelect, onAnalyze }) {
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const now = new Date();
        const diffMs = now - date;
        const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
        const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

        if (diffHours < 1) return 'Just now';
        if (diffHours < 24) return `${diffHours} hour${diffHours > 1 ? 's' : ''} ago`;
        if (diffDays === 1) return 'Yesterday';
        if (diffDays < 7) return `${diffDays} days ago`;
        return date.toLocaleDateString();
    };

    const getPlatformIcon = (platform) => {
        const platformMap = {
            'TWITTER': '🐦',
            'REDDIT': '🔴',
            'FACEBOOK': '👤'
        };
        return platformMap[platform] || '💬';
    };

    return (
        <div className="post-card">
            <div className="post-header">
                <input 
                    type="checkbox" 
                    className="post-checkbox"
                    checked={isSelected}
                    onChange={(e) => onSelect(post.id, e.target.checked)}
                />
                <div className="post-info">
                    <div className="post-author">
                        {getPlatformIcon(post.platform)} @{post.author}
                    </div>
                    <div className="post-meta">
                        <span>📅 {formatDate(post.createdAt)}</span>
                        {!post.analyzed && (
                            <span className="badge badge-unanalyzed">Unanalyzed</span>
                        )}
                        {post.analyzed && (
                            <span className="badge badge-analyzed">Analyzed</span>
                        )}
                    </div>
                </div>
            </div>
            <div className="post-content">
                {post.content.length > 280 
                    ? `${post.content.substring(0, 280)}...` 
                    : post.content}
            </div>
            <div className="post-actions">
                {!post.analyzed && (
                    <button 
                        className="analyze-btn"
                        onClick={() => onAnalyze(post.id)}
                    >
                        🔬 Analyze This Post
                    </button>
                )}
                {post.analyzed && (
                    <span className="analyzed-text">✓ Already analyzed</span>
                )}
            </div>
        </div>
    );
}
