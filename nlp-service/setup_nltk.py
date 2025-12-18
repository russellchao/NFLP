"""
Run this once to download required NLTK data
"""
import nltk

print("Downloading NLTK data...")
nltk.download('vader_lexicon')
nltk.download('punkt')
nltk.download('stopwords')
nltk.download('averaged_perceptron_tagger')
nltk.download('maxent_ne_chunker')
nltk.download('words')
print("NLTK data downloaded successfully!")