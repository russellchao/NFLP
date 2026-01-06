import StatsOverview from './StatsOverview';
import QuickActions from './GetStarted';
import RecentActivity from './RecentActivity';

export default function Dashboard() {
  return (
    <div>
      <div className="page-header">
        <h1>NFLP (National Football Language Processing)</h1>
        <p>An app that uses NLP to analyze football-related content and provide insights.</p>
      </div>

      <div className="page-content">
        <QuickActions />
        <StatsOverview />
      </div>
    </div>
  );
}