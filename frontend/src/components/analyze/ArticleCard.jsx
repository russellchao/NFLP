import React, {useState} from 'react';
import '../../styles/ArticleCard.css';
import { getAnalysis } from '../../services/analysisService';
import { getArticleById } from '../../services/newsService';
import AnalysisStatsModal from './AnalysisStatsModal';

export default function ArticleCard({ article, isSelected, onSelect, onAnalyze }) {
    const [viewingStats, setViewingStats] = useState(false);
    const [stats, setStats] = useState(null);
    const [mentionedTeams, setMentionedTeams] = useState([]);
    const [mentionedPlayers, setMentionedPlayers] = useState([]);

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

    const getSourceDisplay = (source) => {
        const sourceMap = {
            'ESPN': '📰 ESPN',
            'NFL': '📰 NFL.com',
            'BLEACHER_REPORT': '📰 Bleacher Report',
            'CBS_SPORTS': '📰 CBS Sports',
            'SPORTS_ILLUSTRATED': '📰 Sports Illustrated'
        };
        return sourceMap[source] || `📰 ${source}`;
    };

    const onViewAnalysis = async (articleId) => {
        console.log(`View analysis for article ID: ${articleId}`);

        // Get the overall analysis stats for this article
        const statsThisArticle = await getAnalysis('ARTICLE', articleId);
        console.log('Fetched analysis stats:', statsThisArticle);
        setStats(statsThisArticle);

        // Get the mentioned players and teams for this article if any
        const articleDetails = await getArticleById(articleId); 
        console.log('Fetched article details:', articleDetails);
        console.log('Fetched article mentioned players and teams:', {
            players: articleDetails.mentionedPlayers,
            teams: articleDetails.mentionedTeams
        });
        setMentionedPlayers(articleDetails.mentionedPlayers || []);
        setMentionedTeams(articleDetails.mentionedTeams || []);

        // Set the analysis stats modal to be visible
        setViewingStats(true);
    };

    return (
        <div className="article-card">
            <div className="article-header">
                <input 
                    type="checkbox" 
                    className="article-checkbox"
                    checked={isSelected}
                    onChange={(e) => onSelect(article.id, e.target.checked)}
                />
                <div className="article-info">
                    <div className="article-title">{article.title}</div>
                    <div className="article-link">
                        <a href={article.url} target="_blank" rel="noopener noreferrer">
                            Link to Article
                        </a>
                    </div>
                    <div className="article-meta">
                        <span>{getSourceDisplay(article.source)}</span>
                        <span>📅 {formatDate(article.publishedAt)}</span>
                        {!article.analyzed && (
                            <span className="badge badge-unanalyzed">Unanalyzed</span>
                        )}
                        {article.analyzed && (
                            <span className="badge badge-analyzed">Analyzed</span>
                        )}
                    </div>
                </div>
            </div>
            {article.description && (
                <div className="article-preview">
                    {article.description.length > 200 
                        ? `${article.description.substring(0, 200)}...` 
                        : article.description}
                </div>
            )}
            <div className="article-actions">
                {!article.analyzed && (
                    <button 
                        className="analyze-btn"
                        onClick={() => onAnalyze(article.id)}
                    >
                        🔬 Analyze This Article
                    </button>
                )}
                {article.analyzed && (
                    <>
                        <span className="analyzed-text">✓ Already analyzed</span>
                        <p></p>
                        <button 
                            className="view-analysis-btn"
                            onClick={() => onViewAnalysis(article.id)}
                        >
                            🔍 View Analysis
                        </button>
                    </>
                )}
            </div>

            {viewingStats && (
                <div className="modal-backdrop" onClick={() => setViewingStats(false)}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <AnalysisStatsModal 
                            stats={stats} 
                            mentionedPlayers={mentionedPlayers} 
                            mentionedTeams={mentionedTeams}   
                            onClose={() => setViewingStats(false)} 
                        />
                    </div>
                </div>
            )}
        </div>
    );
}
