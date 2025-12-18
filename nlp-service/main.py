"""
NFL Insights NLP Service
Entry point for the Flask application
"""
import os
from app import create_app

app = create_app()

if __name__ == '__main__':
    # Get configuration from environment
    port = int(os.environ.get('PORT', 5002))
    debug = os.environ.get('FLASK_ENV', 'development') == 'development'
    
    print(f"Starting NFL Insights NLP Service on port {port}...")
    print(f"Debug mode: {debug}")
    
    app.run(
        host='0.0.0.0',
        port=port,
        debug=debug
    )