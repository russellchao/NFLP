"""
Tests for toxicity detection
"""
import sys
import os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app.analyzers.toxicity_detector import ToxicityDetector

def test_non_toxic():
    detector = ToxicityDetector()
    result = detector.analyze("Josh Allen threw an amazing touchdown!")
    
    assert result.is_toxic == False
    assert result.toxicity_score < 0.3
    assert len(result.flagged_words) == 0
    print(f"✓ Non-toxic test passed: {result.toxicity_score}")

def test_mildly_toxic():
    detector = ToxicityDetector()
    result = detector.analyze("That was a terrible call by the refs")
    
    assert result.toxicity_score > 0.3
    assert "terrible" in result.flagged_words
    print(f"✓ Mildly toxic test passed: {result.toxicity_score}, flagged: {result.flagged_words}")

def test_highly_toxic():
    detector = ToxicityDetector()
    result = detector.analyze("These refs are absolute garbage trash. Game is rigged.")
    
    assert result.is_toxic == True
    assert result.toxicity_score > 0.7
    assert len(result.flagged_words) >= 2
    print(f"✓ Highly toxic test passed: {result.toxicity_score}, flagged: {result.flagged_words}")

def test_profanity_detection():
    detector = ToxicityDetector()
    result = detector.analyze("This fucking team is so pathetic!")

    assert result.is_toxic == True
    assert "fucking" in result.flagged_words
    print(f"✓ Profanity detection test passed: {result.toxicity_score}, flagged: {result.flagged_words}")
    
def test_word_variations():
    detector = ToxicityDetector()
    
    # Test plural
    result = detector.analyze("The Dallas Cowboys are such playoff chokers!")
    assert "chokers" in result.flagged_words
    print(f"✓ Plural test passed: flagged {result.flagged_words}")
    
    # Test -ing form
    result = detector.analyze("Stop trashing the team!")
    assert "trashing" in result.flagged_words
    print(f"✓ -ing form test passed: flagged {result.flagged_words}")
    
    # Test -ed form  
    result = detector.analyze("They sucked yesterday")
    assert "sucked" in result.flagged_words
    print(f"✓ -ed form test passed: flagged {result.flagged_words}")
    
    # Test false positive avoidance (e.g. "classic", which contains "ass", should not be flagged)
    result = detector.analyze("This is a classic game")
    assert "classic" not in result.flagged_words
    print(f"✓ False positive test passed: {result.flagged_words}")



if __name__ == '__main__':
    print("Running toxicity detector tests...\n")

    test_non_toxic()
    test_mildly_toxic()
    test_highly_toxic()
    test_profanity_detection()
    test_word_variations()

    print("\n✓ All tests passed!")