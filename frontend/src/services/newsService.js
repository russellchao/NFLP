import api from './api';

/**
 * Fetch news articles from sources
 * @param {number} pageSize - Number of articles to fetch
 * @returns {Promise} FetchJob object
 */
export const fetchNews = async (pageSize) => {
  const response = await api.post('/api/news/fetch', null, {
    params: { pageSize }
  });
  return response.data;
};

/**
 * Get all articles
 * @returns {Promise} Array of articles
 */
export const getAllArticles = async () => {
  const response = await api.get('/api/news/all');
  return response.data;
};

/**
 * Get unanalyzed articles
 * @returns {Promise} Array of unanalyzed articles
 */
export const getUnanalyzedArticles = async () => {
  const response = await api.get('/api/news/unanalyzed');
  return response.data;
};

/**
 * Get article by ID
 * @param {number} id - Article ID
 * @returns {Promise} Article object
 */
export const getArticleById = async (id) => {
  const response = await api.get(`/api/news/${id}`);
  return response.data;
};