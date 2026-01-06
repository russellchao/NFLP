import React, { useState, useEffect } from 'react';
import '../../styles/AnalyzeContent.css';
import ArticleList from './ArticleList';
import PostList from './PostList';
import LoadingSpinner from '../common/LoadingSpinner';
import * as dashboardService from '../../services/dashboardService';
import * as newsService  from '../../services/newsService';
import * as socialService from '../../services/socialService';
import * as analysisService from '../../services/analysisService';

export default function AnalyzeContent() {
    const [activeTab, setActiveTab] = useState('articles');
    const [articles, setArticles] = useState([]);
    const [posts, setPosts] = useState([]);
    const [selectedArticleIds, setSelectedArticleIds] = useState([]);
    const [selectedPostIds, setSelectedPostIds] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [sortOrder, setSortOrder] = useState('newest');
    const [stats, setStats] = useState({
        analyzedArticles: 0,
        analyzedPosts: 0,
        toxicPosts: 0,
        avgSentiment: 0
    });
    const [displayLimit, setDisplayLimit] = useState(50);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            setLoading(true);
            setError(null);

            const [articlesData, postsData] = await Promise.all([
                newsService.getAllArticles(),
                // socialService.getAllPosts()
            ]);

            setArticles(articlesData);
            // setPosts(postsData);

            // Calculate stats
            const numAnalyzedArticles = articlesData.filter(a => a.analyzed).length;
            // const numAnalyzedPosts = postsData.filter(p => p.analyzed).length;
            const toxicContent = await analysisService.getToxicContent(0.7).catch(() => []);
            const avgSentiment = await dashboardService.getAvgSentiment().catch(() => 0);
            
            setStats({
                analyzedArticles: numAnalyzedArticles,
                analyzedPosts: 0, // TODO: Replace with numAnalyzedPosts when posts are enabled
                toxicPosts: toxicContent.length,
                avgSentiment: avgSentiment 
            });

            setLoading(false);
        } catch (err) {
            console.error('Error loading data:', err);
            setError('Failed to load content. Please try again.');
            setLoading(false);
        }
    };

    const handleSelectArticle = (articleId, isSelected) => {
        setSelectedArticleIds(prev => 
            isSelected 
                ? [...prev, articleId]
                : prev.filter(id => id !== articleId)
        );
    };

    const handleSelectPost = (postId, isSelected) => {
        setSelectedPostIds(prev => 
            isSelected 
                ? [...prev, postId]
                : prev.filter(id => id !== postId)
        );
    };

    const handleSelectAll = () => {
        if (activeTab === 'articles') {
            const displayedArticles = getFilteredAndSortedArticles().slice(0, displayLimit);
            const allIds = displayedArticles.map(a => a.id);
            setSelectedArticleIds(allIds);
        } else {
            const displayedPosts = getFilteredAndSortedPosts().slice(0, displayLimit);
            const allIds = displayedPosts.map(p => p.id);
            setSelectedPostIds(allIds);
        }
    };

    const handleDeselectAll = () => {
        if (activeTab === 'articles') {
            setSelectedArticleIds([]);
        } else {
            setSelectedPostIds([]);
        }
    };

    const handleAnalyzeSingle = async (id) => {
        try {
            if (activeTab === 'articles') {
                // Analyze the requested article
                await analysisService.analyzeArticle(id);

                // Refetch the article to update its analyzed status
                setArticles(prev => prev.map(a => 
                    a.id === id ? { ...a, analyzed: true } : a
                ));

                // Update stats
                stats.analyzedArticles += 1;
                setStats(prev => ({
                    ...prev,
                    analyzedArticles: stats.analyzedArticles
                }));

            } else if (activeTab === 'posts') {
                // Analyze the requested post
                await analysisService.analyzePost(id);

                // Refetch the post to update its analyzed status
                setPosts(prev => prev.map(p => 
                    p.id === id ? { ...p, analyzed: true } : p
                ));

                stats.analyzedPosts += 1;
                setStats(prev => ({
                    ...prev,
                    analyzedPosts: stats.analyzedPosts
                }));

            } else {
                throw new Error('Invalid content type for analysis');
            }
        } catch (err) {
            console.error('Error analyzing content:', err);
            alert('Failed to analyze content. Please try again.');
        }
    };

    const handleBatchAnalyze = async () => {
        if (selectedArticleIds.length === 0 && selectedPostIds.length === 0) {
            return;
        }

        try {
            const articleIds = activeTab === 'articles' ? selectedArticleIds : [];
            const postIds = activeTab === 'posts' ? selectedPostIds : [];
            
            await analysisService.batchAnalyze(articleIds, postIds);

            // Mark selected items as analyzed
            if (activeTab === 'articles') {
                setArticles(prev => prev.map(a => 
                    selectedArticleIds.includes(a.id) ? { ...a, analyzed: true } : a
                ));
                setSelectedArticleIds([]);

                // Only add to the analyzed articles count if the article was unanalyzed before
                if (articleIds.length > 0) {
                    const newlyAnalyzedCount = articles.filter(a => 
                        selectedArticleIds.includes(a.id) && !a.analyzed
                    ).length;
                    stats.analyzedArticles += newlyAnalyzedCount;
                    setStats(prev => ({
                        ...prev,
                        analyzedArticles: stats.analyzedArticles
                    }));
                }
               
            } else if (activeTab === 'posts') {
                setPosts(prev => prev.map(p => 
                    selectedPostIds.includes(p.id) ? { ...p, analyzed: true } : p
                ));
                setSelectedPostIds([]);

                // Only add to the analyzed posts count if the post was unanalyzed before
                if (postIds.length > 0) {
                    const newlyAnalyzedCount = posts.filter(p =>
                        selectedPostIds.includes(p.id) && !p.analyzed
                    ).length;
                    stats.analyzedPosts += newlyAnalyzedCount;
                    setStats(prev => ({
                        ...prev,
                        analyzedPosts: stats.analyzedPosts
                    }));
                }
                
            } else {
                throw new Error('Invalid content type for batch analysis');
            }

            alert(`Successfully analyzed ${articleIds.length + postIds.length} items!`);
        } catch (err) {
            console.error('Error batch analyzing:', err);
            alert('Failed to batch analyze content. Please try again.');
        }
    };

    const getFilteredAndSortedArticles = () => {
        let filtered = [...articles];

        // Apply sort
        filtered.sort((a, b) => {
            const dateA = new Date(a.publishedAt);
            const dateB = new Date(b.publishedAt);
            return sortOrder === 'newest' ? dateB - dateA : dateA - dateB;
        });

        return filtered;
    };

    const getFilteredAndSortedPosts = () => {
        let filtered = [...posts];

        // Apply sort
        filtered.sort((a, b) => {
            const dateA = new Date(a.createdAt);
            const dateB = new Date(a.createdAt);
            return sortOrder === 'newest' ? dateB - dateA : dateA - dateB;
        });

        return filtered;
    };

    const displayedArticles = getFilteredAndSortedArticles().slice(0, displayLimit);
    const displayedPosts = getFilteredAndSortedPosts().slice(0, displayLimit);
    const totalArticles = getFilteredAndSortedArticles().length;
    const totalPosts = getFilteredAndSortedPosts().length;

    const selectedCount = activeTab === 'articles' ? selectedArticleIds.length : selectedPostIds.length;
    const currentDisplayCount = activeTab === 'articles' ? displayedArticles.length : displayedPosts.length;
    
    const allDisplayedSelected = activeTab === 'articles' 
        ? displayedArticles.every(a => selectedArticleIds.includes(a.id))
        : displayedPosts.every(p => selectedPostIds.includes(p.id));

    return (
        <div className="analyze-container">
            <div className="analyze-header">
                <h1 className="analyze-title">Analyze Content</h1>
                <p className="analyze-subtitle">Select and analyze articles or social posts using NLP</p>
            </div>

            <div className="analyze-content">
                {error && (
                    <div className="error-state">
                        {error}
                    </div>
                )}

                <div className="stats-overview">
                    <div className="stat-box">
                        <div className="stat-number">{stats.analyzedArticles}</div>
                        <div className="stat-label">Analyzed Articles</div>
                    </div>
                    <div className="stat-box">
                        <div className="stat-number">{stats.analyzedPosts}</div>
                        <div className="stat-label">Analyzed Posts</div>
                    </div>
                    <div className="stat-box">
                        <div className="stat-number">{stats.toxicPosts}</div>
                        <div className="stat-label">Toxic Posts Found</div>
                    </div>
                    <div className="stat-box">
                        <div className="stat-number">{stats.avgSentiment < 0 ? '-' : ''}{stats.avgSentiment.toFixed(2)}</div>
                        <div className="stat-label">Avg Sentiment</div>
                    </div>
                </div>

                <div className="tab-nav">
                    <button 
                        className={`tab-button ${activeTab === 'articles' ? 'active' : ''}`}
                        onClick={() => setActiveTab('articles')}
                    >
                        Articles ({totalArticles})
                    </button>
                    <button 
                        className={`tab-button ${activeTab === 'posts' ? 'active' : ''}`}
                        onClick={() => setActiveTab('posts')}
                    >
                        Social Posts ({totalPosts})
                    </button>
                </div>

                <div className="controls">
                    <div className="filters">
                        <select 
                            className="filter-select"
                            value={sortOrder}
                            onChange={(e) => setSortOrder(e.target.value)}
                        >
                            <option value="newest">Sort: Newest</option>
                            <option value="oldest">Sort: Oldest</option>
                        </select>
                    </div>
                    <div className="action-bar">
                        <button 
                            className="btn-secondary"
                            onClick={allDisplayedSelected ? handleDeselectAll : handleSelectAll}
                        >
                            {allDisplayedSelected ? '☑ Deselect All' : '☐ Select All'} ({currentDisplayCount})
                        </button>
                        <button 
                            className="btn-primary"
                            onClick={handleBatchAnalyze}
                            disabled={selectedCount === 0}
                        >
                            🔬 Analyze Selected ({selectedCount})
                        </button>
                    </div>
                </div>

                {loading ? (
                    <div className="loading-state">
                        <LoadingSpinner />
                    </div>
                ) : (
                    <>
                        <div className="content-list">
                            {activeTab === 'articles' ? (
                                <ArticleList
                                    articles={displayedArticles}
                                    selectedIds={selectedArticleIds}
                                    onSelect={handleSelectArticle}
                                    onAnalyze={handleAnalyzeSingle}
                                />
                            ) : (
                                <PostList
                                    posts={displayedPosts}
                                    selectedIds={selectedPostIds}
                                    onSelect={handleSelectPost}
                                    onAnalyze={handleAnalyzeSingle}
                                />
                            )}
                        </div>

                        {((activeTab === 'articles' && displayedArticles.length < totalArticles) ||
                          (activeTab === 'posts' && displayedPosts.length < totalPosts)) && (
                            <div className="load-more-container">
                                <button 
                                    className="btn-secondary"
                                    onClick={() => setDisplayLimit(prev => prev + 50)}
                                >
                                    Load More ({activeTab === 'articles' ? totalArticles - displayedArticles.length : totalPosts - displayedPosts.length} remaining)
                                </button>
                            </div>
                        )}
                    </>
                )}
            </div>
        </div>
    );
}