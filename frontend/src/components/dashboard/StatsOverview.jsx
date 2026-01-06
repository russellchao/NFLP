import React, {useEffect, useState} from 'react';
import * as dashboardService from '../../services/dashboardService';
import LoadingSpinner from '../common/LoadingSpinner';

export default function StatsOverview() {
    const [articlesAnalyzed, setArticlesAnalyzed] = useState(0);
    const [socialPostsAnalyzed, setSocialPostsAnalyzed] = useState(0);
    const [playersTracked, setPlayersTracked] = useState(0);
    const [avgSentiment, setAvgSentiment] = useState(0);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        console.log('Fetching dashboard stats...');

        const fetchStats = async () => {
            try {
                const stats = await dashboardService.getAllDashboardStats();
                setArticlesAnalyzed(stats.articlesAnalyzed);
                setSocialPostsAnalyzed(stats.postsAnalyzed);
                setPlayersTracked(stats.playersTracked);
                setAvgSentiment(stats.avgSentiment.toFixed(2));
            } catch (error) {
                console.error('Error fetching dashboard stats:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, []);

    const stats = [
        { value: articlesAnalyzed, label: 'Articles Analyzed' },
        { value: socialPostsAnalyzed, label: 'Social Posts Analyzed' },
        { value: playersTracked, label: 'Players Tracked' },
        { value: avgSentiment, label: 'Average Sentiment' }
    ];

    if (loading) {
        return <LoadingSpinner />;
    }

    return (
        <div className="section">
            <div className="section-title">📊 Community Stats</div>
            {loading ? <LoadingSpinner /> 
            : (
                <div className="stats-grid">
                    {stats.map((stat, index) => (
                        <div key={index} className="stat-box">
                            <div className="stat-value">{stat.value}</div>
                            <div className="stat-label">{stat.label}</div>
                        </div>
                    ))}
                </div>
            )}            
        </div>
    );
}