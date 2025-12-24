"""
Entitiy extraction routes
"""
from flask import Blueprint, request, jsonify
from app.analyzers.entity_extractor import EntityExtractor
from app.models.requests import EntityRequest

bp = Blueprint('entity', __name__, url_prefix='/api')
extractor = EntityExtractor()

@bp.route('/entity', methods=['POST'])
def extract_entities(): 
    """
    Extract entities of the given text

    --- Example: ---

    Request:
    {
        "text": "Josh Allen threw an amazing touchdown to Dalton Kincaid against the Eagles! Bills take the lead."
    }

    Response:
    {
        "players": ["Josh Allen", "Dalton Kincaid"],
        "teams": ["Philadelphia Eagles", "Buffalo Bills"],
        "all_entities": ["Josh Allen", "Dalton Kincaid", "Philadelphia Eagles", "Buffalo Bills"]
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
            entity_request = EntityRequest(text=data['text'])
        except ValueError as e:
            return jsonify({
                'error': str(e)
            }), 400
        
        # Analyze sentiment
        result = extractor.analyze(entity_request.text)
        
        return jsonify(result.to_dict()), 200
        
    except Exception as e:
        return jsonify({
            'error': 'Internal server error',
            'message': str(e)
        }), 500
    

@bp.route('/entity/batch', methods=['POST'])
def extract_entities_batch():
    """
    Extract entities for a batch of texts

    --- Example: ---

    Request:
    {
        "texts": [
            "Josh Allen threw an amazing touchdown to Dalton Kincaid against the Eagles! Bills take the lead.",
            "Patrick Mahomes led the Chiefs to a stunning victory over the Raiders."
        ]
    }

    Response:
    [
        {
            "players": ["Josh Allen", "Dalton Kincaid"],
            "teams": ["Philadelphia Eagles", "Buffalo Bills"],
            "all_entities": ["Josh Allen", "Dalton Kincaid", "Philadelphia Eagles", "Buffalo Bills"]
        },
        {
            "players": ["Patrick Mahomes"],
            "teams": ["Kansas City Chiefs", "Las Vegas Raiders"],
            "all_entities": ["Patrick Mahomes", "Kansas City Chiefs", "Las Vegas Raiders"]
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
        
        results = extractor.batch_analyze(data['texts'])

        return jsonify([res.to_dict() for res in results]), 200
        
    except Exception as e:
        return jsonify({
            'error': 'Internal server error',
            'message': str(e)
        }), 500