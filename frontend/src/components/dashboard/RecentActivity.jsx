export default function RecentActivity() {
    // TODO: Replace with real data from API
    const activities = [
        { time: '2 minutes ago', text: 'Fetched 45 articles from ESPN, NFL.com' },
        { time: '15 minutes ago', text: 'Analyzed 23 Reddit posts for toxicity' },
        { time: '1 hour ago', text: 'Fetched 97 posts from r/nfl' },
        { time: '2 hours ago', text: 'Player insights generated for Patrick Mahomes' }
    ];

    return (
        <div className="section">
            <div className="section-title">🔔 Recent Activity</div>
            <div className="activity-feed">
                {activities.map((activity, index) => (
                    <div key={index} className="activity-item">
                        <div className="activity-time">{activity.time}</div>
                        <div className="activity-text">{activity.text}</div>
                    </div>
                ))}
            </div>
        </div>
    );
}