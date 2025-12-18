"""
Response models for API endpoints
"""
from dataclasses import dataclass, asdict
from typing import List

@dataclass
class SentimentResponse:
    """Response model for sentiment analysis"""
    score: float          # Compound score (-1 to 1)
    label: str           # POSITIVE, NEGATIVE, or NEUTRAL
    positive: float      # Positive score (0 to 1)
    negative: float      # Negative score (0 to 1)
    neutral: float       # Neutral score (0 to 1)
    compound: float      # Same as score, for compatibility
    
    def to_dict(self):
        return asdict(self)


@dataclass
class EntityResponse:
    """Response model for entity extraction"""
    players: List[str]
    teams: List[str]
    all_entities: List[str]
    
    def to_dict(self):
        return asdict(self)


@dataclass
class ToxicityResponse:
    """Response model for toxicity detection"""
    toxicity_score: float    # 0 to 1
    is_toxic: bool           # True if score > 0.7
    flagged_words: List[str]
    
    def to_dict(self):
        return asdict(self)