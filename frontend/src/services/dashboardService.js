import api from './api';

/**
 * Get number of articles analyzed
 * @returns {Promise<number>} Count of analyzed articles
 */
export const getNumArticlesAnalyzed = async () => {
  const response = await api.get('/api/dashboard/num-articles-analyzed');
  return response.data;
};

/**
 * Get number of posts analyzed
 * @returns {Promise<number>} Count of analyzed posts
 */
export const getNumPostsAnalyzed = async () => {
  const response = await api.get('/api/dashboard/num-posts-analyzed');
  return response.data;
};

/**
 * Get number of players tracked
 * @returns {Promise<number>} Count of tracked players
 */
export const getNumPlayersTracked = async () => {
  const response = await api.get('/api/dashboard/players-tracked');
  return response.data;
};

/**
 * Get average sentiment across all content
 * @returns {Promise<number>} Average sentiment score
 */
export const getAvgSentiment = async () => {
  const response = await api.get('/api/dashboard/avg-sentiment');
  return response.data;
};

/**
 * Get all dashboard stats at once
 * @returns {Promise<object>} Object with all dashboard stats
 */
export const getAllDashboardStats = async () => {
  const [articlesAnalyzed, postsAnalyzed, playersTracked, avgSentiment] = await Promise.all([
    getNumArticlesAnalyzed(),
    getNumPostsAnalyzed(),
    getNumPlayersTracked(),
    getAvgSentiment()
  ]);

  return {
    articlesAnalyzed,
    postsAnalyzed,
    playersTracked,
    avgSentiment
  };
};