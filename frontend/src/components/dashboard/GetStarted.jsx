export default function QuickActions() {
    const actions = [
        { label: '📰 Fetch Content', action: () => window.location.href = '/fetch' },
        { label: '🔬 Analyze Content', action: () => window.location.href = '/analyze' },
        { label: '📊 Player Insights', action: () => window.location.href = '/insights' },
        { label: '☣️ Toxicity Monitor', action: () => window.location.href = '/toxicity' }
    ];

    return (
        <div className="section">
            <div className="section-title">⚡ Get Started</div>
            <div className="get-started">
                {actions.map((item, index) => (
                    <button key={index} className="action-btn" onClick={item.action}>
                        {item.label}
                    </button>
                ))}
            </div>
        </div>
    );
}