"""
Entity extraction using hybrid approach:
- spaCy for player names (handles multi-word names better)
- NLTK for team names (more consistent recognition)
"""
import spacy
import nltk
from nltk import word_tokenize, pos_tag, ne_chunk
from nltk.tree import Tree
from app.utils.text_processor import TextProcessor
from app.models.responses import EntityResponse

class EntityExtractor:
    """
    Extracts NFL entities using hybrid NER approach
    """
    
    def __init__(self):
        self.text_processor = TextProcessor()
        
        # Load spaCy model for player extraction
        self.nlp = spacy.load("en_core_web_sm")
        
        # Football context keywords
        self.football_keywords = {
            'quarterback', 'qb', 'running back', 'rb', 'wide receiver', 'wr',
            'tight end', 'te', 'defense', 'offense', 'touchdown', 'td', 
            'nfl', 'football', 'game', 'season', 'playoff', 'coach',
            'threw', 'caught', 'rushed', 'scored', 'yards', 'draft', 'led', 'carried'
        }
    

    def analyze(self, text: str) -> EntityResponse:
        """
        Extract NFL entities from text
        
        Args:
            text: Input text to analyze
            
        Returns:
            EntityResponse with found players and teams
        """
        # Light preprocessing (keep capitalization!)
        cleaned = self.text_processor.clean_for_sentiment(text)
        
        # Extract players using spaCy (better for names)
        players = self.extract_players(cleaned)
        
        # Extract teams using NLTK (more consistent)
        teams = self.extract_teams(cleaned)
        
        return EntityResponse(
            players=list(set(players)),
            teams=list(set(teams)),
            all_entities=list(set(players + teams))
        )
    

    def extract_players(self, text: str) -> list:
        """
        Extract player names using spaCy
        spaCy handles multi-word names better (doesn't split "Josh Allen")
        """
        doc = self.nlp(text)
        players = []
        
        for ent in doc.ents:
            if ent.label_ == "PERSON":
                # Verify it's likely a player (not a random person)
                if self.is_likely_player(ent.text, text):
                    players.append(ent.text)
        
        return players
    

    def extract_teams(self, text: str) -> list:
        """
        Extract team names using NLTK
        NLTK is more consistent with team name variations
        """
        # Tokenize and tag
        tokens = word_tokenize(text)
        pos_tags = pos_tag(tokens)
        
        # Named Entity Recognition
        named_entities = ne_chunk(pos_tags, binary=False)
        
        teams = []
        text_lower = text.lower()
        
        for entity in named_entities:
            if isinstance(entity, Tree) and entity.label() == 'ORGANIZATION':
                # Join multi-word names
                name = ' '.join([word for word, tag in entity.leaves()])
                
                # Filter to keep only likely NFL teams
                if self.is_likely_team(name, text_lower):
                    teams.append(name)

        # Check to see if any extracted organizations shouldn't be there (not an NFL team)
        # load nfl_teams_cities.json file
        # with open('../../data/nfl_teams_cities.json', 'r') as f:
            
        return teams
    

    def is_likely_player(self, name: str, context: str) -> bool:
        """
        Check if person entity is likely a football player
        """
        # Must have at least 2 words (first + last name)
        if len(name.split()) < 2:
            return False
        
        # Check if context has football keywords
        context_lower = context.lower()
        for keyword in self.football_keywords:
            if keyword in context_lower:
                return True
        
        return False
    

    def is_likely_team(self, org_name: str, context: str) -> bool:
        """
        Check if organization is likely an NFL team
        """
        org_lower = org_name.lower()
        
        # Common team name patterns
        team_indicators = [
            'chiefs', 'bills', 'eagles', 'cowboys', 'patriots', 'packers',
            'steelers', 'ravens', 'browns', 'bengals', 'raiders', 'chargers',
            'broncos', 'colts', 'titans', 'jaguars', 'texans', 'dolphins',
            'jets', 'rams', 'cardinals', 'seahawks', 'niners', '49ers',
            'saints', 'falcons', 'panthers', 'buccaneers', 'lions',
            'bears', 'vikings', 'commanders', 'giants'
        ]
        
        for indicator in team_indicators:
            if indicator in org_lower:
                return True
        
        # COMMENTING OUT FOR NOW - this code segment sometimes extracts organizations that aren't NFL teams
        # # Check context for football keywords
        # for keyword in self.football_keywords:
        #     if keyword in context:
        #         return True
        
        return False
    

    def batch_analyze(self, texts: list) -> list:
        """
        Analyze multiple texts at once
        
        Args:
            texts: List of text strings
            
        Returns:
            List of EntityResponse objects
        """
        return [self.analyze(text) for text in texts]