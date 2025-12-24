# NFL Insights NLP Service

Python microservice that analyzes NFL-related text using natural language processing.

## What It Does

This service provides three main features:

1. **Sentiment Analysis** - Determines if text is positive, negative, or neutral
2. **Toxicity Detection** - Identifies toxic/aggressive language in posts
3. **Entity Extraction** - Finds NFL player names and team names in text

## Setup

```bash
# Install dependencies
pip install -r requirements.txt

# Download NLTK data
python setup_nltk.py

# Download spaCy model
python -m spacy download en_core_web_sm

# Run the service
python main.py
```

Service runs on `http://localhost:5002`

## API Routes

### Sentiment Analysis

**POST** `/api/sentiment`

Analyzes the sentiment of text.

```bash
curl -X POST http://localhost:5002/api/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "Patrick Mahomes threw an amazing touchdown!"}'
```

**Response:**
```json
{
  "score": 0.66,
  "label": "POSITIVE",
  "positive": 0.51,
  "negative": 0.0,
  "neutral": 0.49,
  "compound": 0.66
}
```

- `score`: Overall sentiment (-1 to 1)
- `label`: POSITIVE, NEGATIVE, or NEUTRAL

---

### Toxicity Detection

**POST** `/api/toxicity`

Detects toxic language in text.

```bash
curl -X POST http://localhost:5002/api/toxicity \
  -H "Content-Type: application/json" \
  -d '{"text": "These refs are absolute garbage!"}'
```

**Response:**
```json
{
  "toxicity_score": 0.75,
  "is_toxic": true,
  "flagged_words": ["garbage"]
}
```

- `toxicity_score`: 0 to 1 (higher = more toxic)
- `is_toxic`: true if score > 0.7

---

### Entity Extraction

**POST** `/api/entities`

Extracts NFL players and teams from text.

```bash
curl -X POST http://localhost:5002/api/entities \
  -H "Content-Type: application/json" \
  -d '{"text": "Josh Allen led the Buffalo Bills to victory"}'
```

**Response:**
```json
{
  "players": ["Josh Allen"],
  "teams": ["Buffalo Bills"],
  "all_entities": ["Josh Allen", "Buffalo Bills"]
}
```

---

### Batch Endpoints

All three endpoints have batch versions for analyzing multiple texts at once:

- **POST** `/api/sentiment/batch`
- **POST** `/api/toxicity/batch`
- **POST** `/api/entities/batch`

**Example:**
```bash
curl -X POST http://localhost:5002/api/sentiment/batch \
  -H "Content-Type: application/json" \
  -d '{"texts": ["Great game!", "Terrible call"]}'
```

**Response:**
```json
{
  "results": [
    {"score": 0.6, "label": "POSITIVE", ...},
    {"score": -0.5, "label": "NEGATIVE", ...}
  ]
}
```

## Testing

```bash
# Run tests
python tests/test_sentiment.py
python tests/test_toxicity.py
python tests/test_entity.py
```

## Technology

- **NLTK VADER** - Sentiment analysis
- **spaCy + NLTK** - Entity extraction
- **Keyword matching** - Toxicity detection
- **Flask** - Web framework