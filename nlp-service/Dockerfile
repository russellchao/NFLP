FROM python:3.11-slim

# Set working directory
WORKDIR /app

# Install system dependencies (if needed)
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy requirements first (for Docker layer caching)
COPY requirements.txt .

# Install Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Download NLTK data during build 
RUN python -m nltk.downloader -d /usr/local/share/nltk_data \
    vader_lexicon \
    punkt \
    stopwords \
    averaged_perceptron_tagger \
    maxent_ne_chunker \
    words

# Copy application code
COPY . .

# Expose port
EXPOSE 5002

# Set environment variables
ENV FLASK_ENV=production
ENV PORT=5002
ENV NLTK_DATA=/usr/local/share/nltk_data

# Run the application
CMD ["gunicorn", "--bind", "0.0.0.0:5002", "--workers", "2", "--timeout", "120", "main:app"]