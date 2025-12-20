"""
Toxicity detection
"""
from app.utils.text_processor import TextProcessor
from app.models.responses import ToxicityResponse
from app.analyzers.sentiment_analyzer import SentimentAnalyzer

import os

class ToxicityDetector:
    """
    Detects toxic language of NFL-related text
    """

    def __init__(self):
        self.text_processor = TextProcessor()
        self.sentiment_analyzer = SentimentAnalyzer()
        self.profanity_set = self.load_profanity_list()


    def analyze(self, text: str) -> ToxicityResponse:
        """ Analyze the toxicity of the given text """

        # 1. Get sentiment
        sentiment = self.sentiment_analyzer.analyze(text)

        # 2. Preprocess and clean the text for toxicity detection
        cleaned_text = self.text_processor.preprocess(text)

        # 3. Tokenize the text
        tokens = self.text_processor.tokenize(cleaned_text)

        # 4. Check for profanity/aggressive words
        flagged = self.find_flagged_words(tokens)
        
        # 5. Combine signals
        # More flagged words + negative sentiment = higher toxicity
        word_score = len(flagged) * 0.3  # Each word adds 0.3
        sentiment_score = abs(min(sentiment.score, 0)) * 0.5
        toxicity_score = min(word_score + sentiment_score, 1.0)
        
        return ToxicityResponse(
            toxicity_score=toxicity_score,
            is_toxic=toxicity_score > 0.7,
            flagged_words=flagged
        )
    

    def batch_analyze(self, texts: list) -> list:
        """ Detect toxicity for a batch of texts """
        return [self.analyze(text) for text in texts]


    def load_profanity_list(self) -> set:
        """Load profanity list from file"""

        try:
            # Get the path to the data file
            current_dir = os.path.dirname(os.path.abspath(__file__))
            data_file = os.path.join(current_dir, '../../data/profanity_list.txt')
            
            with open(data_file, 'r') as f:
                # Read lines, ignore comments and empty lines
                words = [
                    line.strip().lower() 
                    for line in f 
                    if line.strip() and not line.startswith('#')
                ]  
                return set(words)
            
        except FileNotFoundError:
            print("Warning: profanity_list.txt not found, using empty set")
            return set()
        

    def find_flagged_words(self, tokens: list) -> list:
        """Detects words from a text deemed toxic (including variations)"""

        flagged = []
        for token in tokens:
            # Check exact match first
            if token in self.profanity_set:
                flagged.append(token)
            else:
                # Check if token starts with any profane word
                # This catches: choker → chokers, suck → sucks/sucked/sucking
                for profane_word in self.profanity_set:
                    if token.startswith(profane_word) and len(token) > len(profane_word):
                        flagged.append(token)
                        break
        return flagged