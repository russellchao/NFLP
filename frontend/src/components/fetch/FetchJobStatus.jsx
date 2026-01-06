import React from 'react';

export default function FetchJobStatus() {
    const jobs = [
        {
            id: 1,
            time: new Date('2026-01-01 16:07:35.471911').toLocaleString(), // the time the fetch was initiated
            description: 'Fetched 3 News Articles',
            status: 'Completed'
        },
        {
            id: 2,
            time: new Date('2025-12-31 19:22:18.963363').toLocaleString(), 
            description: 'Fetched 100 Reddit Posts from r/nfl',
            status: 'Completed'
        },
        {
            id: 3,
            time: new Date('2025-12-29 19:26:13.631899').toLocaleString(), 
            description: 'Fetched 1 News Article',
            status: 'Completed'
        }
    ];

    return (
        <div className="panel">
            <div className="panel-title">🔔 Recent Fetch Jobs</div>

            <div>
                <h3 style={{ color: 'red' }}>This is all a placeholder. This portion will contain the last 5 fetch jobs.</h3>
            </div>
            
            <div className="job-list">
                {jobs.map(job => (
                    <div key={job.id} className="job-item">
                        <div className="job-info">
                            <div className="job-time">{job.time}</div>
                            <div className="job-desc">{job.description}</div>
                        </div>
                        <span className="status-badge">{job.status}</span>
                    </div>
                ))}
            </div>
        </div>
    );
}
