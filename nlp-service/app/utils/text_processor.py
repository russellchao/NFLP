"""
Text preprocessing utilities
"""
import re
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords

class TextProcessor:
    """Utilities for text preprocessing"""
    
    def __init__(self):
        self.stop_words = set(stopwords.words('english'))
    
    def preprocess(self, text: str) -> str:
        """
        Basic text preprocessing
        - Lowercase
        - Remove URLs
        - Remove extra whitespace
        """
        if not text:
            return ""
        
        # Lowercase
        text = text.lower()
        
        # Remove URLs
        text = re.sub(r'http\S+|www\S+|https\S+', '', text, flags=re.MULTILINE)
        
        # Remove extra whitespace
        text = ' '.join(text.split())
        
        return text
    
    def tokenize(self, text: str) -> list:
        """Tokenize text into words"""
        return word_tokenize(text.lower())
    
    def remove_stopwords(self, tokens: list) -> list:
        """Remove common stopwords"""
        return [word for word in tokens if word not in self.stop_words]
    
    def clean_for_sentiment(self, text: str) -> str:
        """
        Light cleaning for sentiment analysis
        Keep punctuation and capitalization as they carry sentiment
        """
        # Just remove URLs and extra whitespace
        text = re.sub(r'http\S+|www\S+|https\S+', '', text, flags=re.MULTILINE)
        text = ' '.join(text.split())
        return text