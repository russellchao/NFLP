# NFL Insights NLP Service

Python microservice for NLP analysis of NFL content.

## Features
- ✅ Sentiment Analysis (VADER)
- 🔜 Entity Extraction (Players, Teams)
- 🔜 Toxicity Detection

## Setup
```bash
# Create virtual environment
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt

# Download NLTK data
python setup_nltk.py

# Run the service
python main.py
```

## API Endpoints

### POST /api/sentiment
Analyze sentiment of text.

**Request:**
```json
{
  "text": "Josh Allen is amazing!"
}
```

**Response:**
```json
{
  "score": 0.6,
  "label": "POSITIVE",
  "positive": 0.508,
  "negative": 0.0,
  "neutral": 0.492,
  "compound": 0.6
}
```

### GET /health
Health check endpoint.

## Testing
```bash
# Run tests
python tests/test_sentiment.py

# Test with curl
curl -X POST http://localhost:5002/api/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "This is amazing!"}'
```