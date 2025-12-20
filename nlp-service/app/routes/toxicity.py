"""
Toxicity detection routes
"""
from flask import Blueprint, request, jsonify
from app.analyzers.toxicity_detector import ToxicityDetector
from app.models.requests import ToxicityRequest

bp = Blueprint('toxicity', __name__, url_prefix='/api')
detector = ToxicityDetector()

@bp.route('/toxicity', methods=['POST'])
def detect_toxicity(): 
    """
    Detect the incoming text for toxicity

    ----- Example: -----

    Request:
    {
        "text": "These refs are absolute garbage trash. Game is rigged."
    }

    Response:
    {
        "toxicity_score": 0.85,
        "is_toxic": true,
        "flagged_words": ["garbage", "trash", "rigged"]
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
            toxicity_request = ToxicityRequest(text=data['text'])
        except ValueError as e:
            return jsonify({
                'error': str(e)
            }), 400
        
        # Detect toxicity  
        result = detector.analyze(toxicity_request.text)

        return jsonify(result.to_dict()), 200
        
    except Exception as e:
        return jsonify({
            'error': 'Internal server error',
            'message': str(e)
        }), 500


@bp.route('/toxicity/batch', methods=['POST'])
def detect_toxicity_batch(): 
    """
    Detect the toxicity of multiple texts

    ----- Example: -----

    Request:
    {
        "texts": [
            "These refs are absolute garbage trash. Game is rigged.",
            "What a fantastic play by the quarterback!",
            "I hate this team so much."
        ]
    }

    Response:
    {
        "results": [
            {
                "toxicity_score": 0.85,
                "is_toxic": true,
                "flagged_words": ["garbage", "trash", "rigged"]
            },
            {
                "toxicity_score": 0.1,
                "is_toxic": false,
                "flagged_words": []
            },
            {
                "toxicity_score": 0.75,
                "is_toxic": true,
                "flagged_words": ["hate"]
            }
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
        results = detector.batch_analyze(data['texts'])
        
        return jsonify({
            'results': [r.to_dict() for r in results]
        }), 200
    
    except Exception as e:
        return jsonify({
            'error': 'Internal server error',
            'message': str(e)
        }), 500