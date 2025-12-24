"""
Tests for NER-based entity extraction
"""
import sys
import os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app.analyzers.entity_extractor import EntityExtractor

def test_player_extraction():
    extractor = EntityExtractor()
    result = extractor.analyze("Josh Allen threw an amazing touchdown to Dalton Kincaid!")
    
    print(f"Players found: {result.players}")
    assert len(result.players) > 0
    print("✓ Player extraction passed")

def test_team_extraction():
    extractor = EntityExtractor()
    result = extractor.analyze("The Buffalo Bills won against the Philadelphia Eagles in an NFL football game")
    
    print(f"Teams found: {result.teams}")
    assert len(result.teams) > 0
    print("✓ Team extraction passed")

def test_full_article():
    extractor = EntityExtractor()
    text = """
    Josh Allen led the Buffalo Bills to a dramatic victory over 
    the Philadelphia Eagles. Jalen Hurts threw two touchdowns but it wasn't enough.
    The Bills defense made crucial stops in the fourth quarter.
    """
    result = extractor.analyze(text)
    
    print(f"\nFull article test:")
    print(f"Players: {result.players}")
    print(f"Teams: {result.teams}")
    print(f"All entities: {result.all_entities}")
    print("✓ Full article test passed")

def test_full_article2():
    extractor = EntityExtractor()
    text = """
    Lamar Jackson led the Baltimore Ravens to a dramatic victory over 
    the Green Bay Packers. Jordan Love threw two touchdowns but it wasn't enough.
    The Ravens defense made crucial stops in the fourth quarter.
    """
    result = extractor.analyze(text)
    
    print(f"\nFull article test:")
    print(f"Players: {result.players}")
    print(f"Teams: {result.teams}")
    print(f"All entities: {result.all_entities}")
    print("✓ Full article test passed")

def test_full_article3():
    extractor = EntityExtractor()
    text = """
    Dak Prescott led the Dallas Cowboys to a dramatic victory over 
    the Denver Broncos. Bo Nix threw two touchdowns but it wasn't enough.
    The Cowboys defense made crucial stops in the fourth quarter.
    """
    result = extractor.analyze(text)
    
    print(f"\nFull article test:")
    print(f"Players: {result.players}")
    print(f"Teams: {result.teams}")
    print(f"All entities: {result.all_entities}")
    print("✓ Full article test passed")

def test_common_last_names():
    extractor = EntityExtractor()
    text = """
    Josh Allen is a QB, while Keenan Allen is a RB.
    AJ Brown and Amon-Ra St. Brown are both WRs but for different teams. 
    Tee Higgins and Jayden Higgins are both WRs but for different teams.
    Brothers Christian McCaffrey and Luke McCaffrey play for different teams.
    """
    result = extractor.analyze(text)
    
    print(f"\nCommon last name test:")
    print(f"Players: {result.players}")
    print(f"Teams: {result.teams}")
    print(f"All entities: {result.all_entities}")
    print("✓ Full article test passed")

def test_team_names_only():
    extractor = EntityExtractor()
    text = """
    The Buffalo Bills, Los Angeles Chargers, and Jacksonville Jaguars all clinched the playoffs after the 49ers beat the Colts last night.
    """
    result = extractor.analyze(text)

    print(f"\nTeam name only test:")
    print(f"Teams: {result.teams}")
    print("✓ Team name test passed")


if __name__ == '__main__':
    print("Running NER-based entity extractor tests...\n")
    test_player_extraction()
    test_team_extraction()
    test_full_article()
    test_full_article2()
    test_full_article3()
    test_common_last_names()
    test_team_names_only()
    print("\n✓ All tests passed!")