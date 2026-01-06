import { NavLink } from 'react-router-dom';

export default function NavBar() {
    return (
        <nav className="nav-container"> 
            <div className="nav-title">🏈 NFLP</div>
                
            <div className="nav-links">
                <NavLink to="/" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                    Dashboard
                </NavLink>
                <NavLink to="/fetch" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                    Fetch Content
                </NavLink>
                <NavLink to="/analyze" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                    Analyze Content
                </NavLink>
                <NavLink to="/insights" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                    Player Insights
                </NavLink>
                <NavLink to="/toxicity" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                    Toxicity Monitor
                </NavLink>
            </div>
        </nav>
    );
}