"""
Flask application factory
"""
from app.routes import entity
from flask import Flask
from flask_cors import CORS

def create_app():
    """Create and configure the Flask application"""
    app = Flask(__name__)
    
    # Enable CORS for all routes
    CORS(app)
    
    # Register blueprints
    from app.routes import entity, sentiment, toxicity
    app.register_blueprint(entity.bp)
    app.register_blueprint(sentiment.bp)
    app.register_blueprint(toxicity.bp)
    
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
                'entity': '/api/entity',
                'entity_batch': '/api/entity/batch',
                'sentiment': '/api/sentiment',
                'sentiment_batch': '/api/sentiment/batch',
                'toxicity': '/api/toxicity',
                'toxicity_batch': '/api/toxicity/batch',
                'health': '/health'
            }
        }, 200
    
    return app