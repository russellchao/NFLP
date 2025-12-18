"""
Request models for API endpoints
"""
from dataclasses import dataclass
from typing import Optional

@dataclass
class SentimentRequest:
    """Request model for sentiment analysis"""
    text: str
    
    def __post_init__(self):
        if not self.text or not self.text.strip():
            raise ValueError("Text cannot be empty")
        
        # Truncate very long texts to avoid timeouts
        if len(self.text) > 10000:
            self.text = self.text[:10000]


@dataclass
class EntityRequest:
    """Request model for entity extraction"""
    text: str
    
    def __post_init__(self):
        if not self.text or not self.text.strip():
            raise ValueError("Text cannot be empty")


@dataclass
class ToxicityRequest:
    """Request model for toxicity detection"""
    text: str
    
    def __post_init__(self):
        if not self.text or not self.text.strip():
            raise ValueError("Text cannot be empty")