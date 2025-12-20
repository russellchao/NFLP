"""
Sentiment analysis using NLTK's VADER
"""
from nltk.sentiment.vader import SentimentIntensityAnalyzer
from app.utils.text_processor import TextProcessor
from app.models.responses import SentimentResponse

class SentimentAnalyzer:
    """
    Analyzes sentiment of text using VADER (Valence Aware Dictionary 
    and sEntiment Reasoner)
    
    VADER is specifically attuned to sentiments expressed in social media
    and works well with NFL-related content.
    """
    
    def __init__(self):
        self.vader_analyzer = SentimentIntensityAnalyzer()
        self.text_processor = TextProcessor()
    

    def analyze(self, text: str) -> SentimentResponse:
        """
        Analyze sentiment of the given text
        
        Args:
            text: Input text to analyze
            
        Returns:
            SentimentResponse with scores and label
        """
        # Light preprocessing - keep punctuation for sentiment
        cleaned_text = self.text_processor.clean_for_sentiment(text)
        
        # Get VADER scores
        scores = self.vader_analyzer.polarity_scores(cleaned_text)
        
        # Determine label based on compound score
        label = self.get_sentiment_label(scores['compound'])
        
        return SentimentResponse(
            score=scores['compound'],
            label=label,
            positive=scores['pos'],
            negative=scores['neg'],
            neutral=scores['neu'],
            compound=scores['compound']
        )
    

    def get_sentiment_label(self, compound_score: float) -> str:
        """
        Convert compound score to label
        
        VADER compound score ranges from -1 (most negative) to +1 (most positive)
        Typical thresholds:
        - >= 0.05: positive
        - <= -0.05: negative
        - Between: neutral
        """
        if compound_score >= 0.05:
            return "POSITIVE"
        elif compound_score <= -0.05:
            return "NEGATIVE"
        else:
            return "NEUTRAL"
    

    def batch_analyze(self, texts: list) -> list:
        """
        Analyze multiple texts at once
        
        Args:
            texts: List of text strings
            
        Returns:
            List of SentimentResponse objects
        """
        return [self.analyze(text) for text in texts]