"""
Flask application factory
"""
from flask import Flask
from flask_cors import CORS

def create_app():
    """Create and configure the Flask application"""
    app = Flask(__name__)
    
    # Enable CORS for all routes
    CORS(app)
    
    # Register blueprints
    from app.routes import sentiment
    app.register_blueprint(sentiment.bp)
    
    # Health check endpoint
    @app.route('/health')
    def health_check():
        return {'status': 'healthy', 'service': 'nfl-nlp-service'}, 200
    
    @app.route('/')
    def index():
        return {
            'service': 'NFL Insights NLP Service',
            'version': '1.0.0',
            'endpoints': {
                'sentiment': '/api/sentiment',
                'sentiment_batch': '/api/sentiment/batch',
                'health': '/health'
            }
        }, 200
    
    return app