"""
Sentiment analysis routes
"""
from flask import Blueprint, request, jsonify
from app.analyzers.sentiment_analyzer import SentimentAnalyzer
from app.models.requests import SentimentRequest

bp = Blueprint('sentiment', __name__, url_prefix='/api')
analyzer = SentimentAnalyzer()

@bp.route('/sentiment', methods=['POST'])
def analyze_sentiment():
    """
    Analyze the sentiment of a text

    ----- Example: -----
    
    Request body:
    {
        "text": "Josh Allen is amazing!"
    }
    
    Response:
    {
        "score": 0.6,
        "label": "POSITIVE",
        "positive": 0.508,
        "negative": 0.0,
        "neutral": 0.492,
        "compound": 0.6
    }
    """
    
    try:
        # Parse request
        data = request.get_json()
        
        if not data or 'text' not in data:
            return jsonify({
                'error': 'Missing required field: text'
            }), 400
        
        # Validate request
        try:
            sentiment_request = SentimentRequest(text=data['text'])
        except ValueError as e:
            return jsonify({
                'error': str(e)
            }), 400
        
        # Analyze sentiment
        result = analyzer.analyze(sentiment_request.text)
        
        return jsonify(result.to_dict()), 200
        
    except Exception as e:
        return jsonify({
            'error': 'Internal server error',
            'message': str(e)
        }), 500


@bp.route('/sentiment/batch', methods=['POST'])
def analyze_sentiment_batch():
    """
    Analyze the sentiment of multiple texts

    ----- Example: -----
    
    Request body:
    {
        "texts": [
            "Josh Allen is amazing!",
            "That was a terrible call by the refs"
        ]
    }
    
    Response:
    {
        "results": [
            {"score": 0.6, "label": "POSITIVE", ...},
            {"score": -0.7, "label": "NEGATIVE", ...}
        ]
    }
    """

    try:
        data = request.get_json()
        
        if not data or 'texts' not in data:
            return jsonify({
                'error': 'Missing required field: texts'
            }), 400
        
        if not isinstance(data['texts'], list):
            return jsonify({
                'error': 'Field texts must be an array'
            }), 400
        
        # Analyze all texts
        results = analyzer.batch_analyze(data['texts'])
        
        return jsonify({
            'results': [r.to_dict() for r in results]
        }), 200
        
    except Exception as e:
        return jsonify({
            'error': 'Internal server error',
            'message': str(e)
        }), 500