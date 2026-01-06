import React from 'react';
import ArticleCard from './ArticleCard';

export default function ArticleList({ 
    articles, 
    selectedIds, 
    onSelect, 
    onAnalyze 
}) {
    if (!articles || articles.length === 0) {
        return (
            <div className="empty-state">
                <div className="empty-state-icon">📰</div>
                <div className="empty-state-title">No Articles Found</div>
                <p>There are no articles to display at this time.</p>
            </div>
        );
    }

    return (
        <div className="article-list">
            {articles.map(article => (
                <ArticleCard
                    key={article.id}
                    article={article}
                    isSelected={selectedIds.includes(article.id)}
                    onSelect={onSelect}
                    onAnalyze={onAnalyze}
                />
            ))}
        </div>
    );
}
