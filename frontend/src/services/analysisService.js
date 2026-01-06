import api from './api';

/**
 * Analyze a single article
 * @param {number} articleId - Article ID to analyze
 * @returns {Promise} SentimentAnalysis object
 */
export const analyzeArticle = async (articleId) => {
  const response = await api.post('/api/analysis/analyzeArticle', null, {
    params: { articleId }
  });
  return response.data;
};

/**
 * Analyze a single social post
 * @param {number} postId - Post ID to analyze
 * @returns {Promise} SentimentAnalysis object
 */
export const analyzePost = async (postId) => {
  const response = await api.post('/api/analysis/analyzePost', null, {
    params: { postId }
  });
  return response.data;
};

/**
 * Batch analyze multiple articles and posts
 * @param {Array<number>} articleIds - Array of article IDs
 * @param {Array<number>} postIds - Array of post IDs
 * @returns {Promise} BatchAnalysisResult object
 */
export const batchAnalyze = async (articleIds = [], postIds = []) => {
  const response = await api.post('/api/analysis/batchAnalyze', null, {
    params: { 
      articleIds: articleIds.join(','),
      postIds: postIds.join(',')
    }
  });
  return response.data;
};

/**
 * Get analysis for specific content
 * @param {string} contentType - 'ARTICLE' or 'SOCIAL_POST'
 * @param {number} contentId - Content ID
 * @returns {Promise} SentimentAnalysis object
 */
export const getAnalysis = async (contentType, contentId) => {
  const response = await api.get('/api/analysis/get', {
    params: { contentType, contentId }
  });
  return response.data;
};

/**
 * Delete analysis for specific content
 * @param {string} contentType - 'ARTICLE' or 'SOCIAL_POST'
 * @param {number} contentId - Content ID
 * @returns {Promise} void
 */
export const deleteAnalysis = async (contentType, contentId) => {
  await api.delete('/api/analysis/delete', {
    params: { contentType, contentId }
  });
};

/**
 * Get toxic content above threshold
 * @param {number} threshold - Toxicity threshold (0-1)
 * @returns {Promise} Array of SentimentAnalysis objects
 */
export const getToxicContent = async (threshold) => {
  const response = await api.get('/api/analysis/getToxicContent', {
    params: { threshold }
  });
  return response.data;
};