import '../../styles/AnalysisStatsModal.css';

export default function AnalysisStatsModal({ stats, mentionedPlayers, mentionedTeams, onClose }) {

    return (   
        <div className="modal-backdrop">
            <div className="modal-content">
                <h2>Analysis Statistics</h2>

                <br></br>

                <div className="modal-stats-list">
                    <div className="modal-stat-row">
                        <span className="modal-stat-label">Content Type:</span>  
                        <span className="modal-stat-value">{stats.contentType}</span>
                    </div>
                    <div className="modal-stat-row">
                        <span className="modal-stat-label">Sentiment Score:</span>
                        <span className="modal-stat-value">{stats.sentimentScore}</span>
                    </div>
                    <div className="modal-stat-row">
                        <span className="modal-stat-label">Toxicity Score:</span>
                        <span className="modal-stat-value">{stats.toxicityScore}</span>
                    </div>
                    <div className="modal-stat-row">
                        <span className="modal-stat-label">Mentioned Players:</span>
                        <span className="modal-stat-value">{mentionedPlayers}</span>
                    </div>
                    <div className="modal-stat-row">
                        <span className="modal-stat-label">Mentioned Teams:</span>
                        <span className="modal-stat-value">{mentionedTeams}</span>
                    </div>
                </div>
                <div className="modal-actions">
                    <button 
                        style={{ backgroundColor: 'red', color: 'white', width: '20%', height: '40px', borderRadius: '5px', border: 'none', cursor: 'pointer' }}
                        onClick={onClose}
                    >
                        Close
                    </button>
                </div>
            </div>
        </div>
    );
}