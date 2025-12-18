from flask import Flask, request, jsonify
from flask_cors import CORS
import nltk
from nltk.sentiment.vader import SentimentIntensityAnalyzer

app = Flask(__name__)
CORS(app)

# Download NLTK data
nltk.download('vader_lexicon')
nltk.download('punkt')

sia = SentimentIntensityAnalyzer()

@app.route('/api/sentiment', methods=['POST'])
def analyze_sentiment():
    text = request.json.get('text', '')
    scores = sia.polarity_scores(text)
    
    return jsonify({
        'score': scores['compound'],
        'label': 'POSITIVE' if scores['compound'] > 0.05 else 'NEGATIVE' if scores['compound'] < -0.05 else 'NEUTRAL',
        'positive': scores['pos'],
        'negative': scores['neg'],
        'neutral': scores['neu'],
        'compound': scores['compound']
    })

if __name__ == '__main__':
    app.run(port=5002, debug=True)