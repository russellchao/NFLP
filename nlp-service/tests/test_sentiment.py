"""
Tests for sentiment analysis
"""
import sys
import os

# Add parent directory to path
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app.analyzers.sentiment_analyzer import SentimentAnalyzer

def test_positive_sentiment():
    analyzer = SentimentAnalyzer()
    result = analyzer.analyze("Josh Allen is absolutely amazing and clutch!")

    print(result)
    assert result.label == "POSITIVE"
    assert result.score > 0.05
    print(f"✓ Positive test passed: {result.score}")

def test_negative_sentiment():
    analyzer = SentimentAnalyzer()
    result = analyzer.analyze("That was a terrible call by the refs. Absolute garbage.")

    print(result)
    assert result.label == "NEGATIVE"
    assert result.score < -0.05
    print(f"✓ Negative test passed: {result.score}")

def test_neutral_sentiment():
    analyzer = SentimentAnalyzer()
    result = analyzer.analyze("The game is scheduled for Sunday at 1pm.")

    print(result)
    assert result.label == "NEUTRAL"
    print(f"✓ Neutral test passed: {result.score}")

def test_reddit_post():
    # This thread was pulled directly from a reddit post
    analyzer = SentimentAnalyzer()
    result = analyzer.analyze("[Garafolo] The NFL has fined #Rams WR Puka Nacua $25,000 for his comments about officiating, source says.")

    print(result)
    assert result.label == "NEUTRAL"
    print(f"✓ Reddit test passed: {result.score}")

def test_failed_sentiment(): 
    # Attempt to return a negative sentiment on what should be a positive sentiment "
    # Should return an assertion error

    analyzer = SentimentAnalyzer()
    result = analyzer.analyze("Tom Brady is the greatest quarterback of all time.")
    
    print(result)
    assert result.label == "NEGATIVE"
    assert result.score < -0.05




if __name__ == '__main__':
    print("Running sentiment analyzer tests...\n")
    test_positive_sentiment()
    test_negative_sentiment()
    test_neutral_sentiment()
    test_reddit_post()
    print("\n✓ All tests passed!")
    print("\nRunning failed sentiment test (should raise AssertionError)...")
    test_failed_sentiment() 