import request from '@/utils/request'

export function login(data) {
  return request.post('/auth/login', data)
}

export function register(data) {
  return request.post('/auth/register', data)
}

export function getUserInfo() {
  return request.get('/user/info')
}

export function updateUserInfo(data) {
  return request.put('/user/info', data)
}

export function updatePassword(data) {
  return request.put('/user/password', data)
}

export function getProducts(params) {
  return request.get('/products', { params })
}

export function getProductById(id) {
  return request.get(`/products/${id}`)
}

export function getMyProducts(params) {
  return request.get('/products/my', { params })
}

export function createProduct(data) {
  return request.post('/products', data)
}

export function updateProduct(id, data) {
  return request.put(`/products/${id}`, data)
}

export function deleteProduct(id) {
  return request.delete(`/products/${id}`)
}

export function updateProductStatus(id, status) {
  return request.put(`/products/${id}/status?status=${status}`)
}

export function getCategories() {
  return request.get('/products/categories')
}

export function getFavorites() {
  return request.get('/favorites')
}

export function addFavorite(productId) {
  return request.post(`/favorites/${productId}`)
}

export function removeFavorite(productId) {
  return request.delete(`/favorites/${productId}`)
}

export function checkFavorite(productId) {
  return request.get(`/favorites/check/${productId}`)
}

export function sendMessage(data) {
  return request.post('/messages', data)
}

export function getReceivedMessages(params) {
  return request.get('/messages/received', { params })
}

export function getUnreadCount() {
  return request.get('/messages/unread/count')
}

export function markAsRead(messageId) {
  return request.put(`/messages/read/${messageId}`)
}

export function createOrder(data) {
  return request.post('/orders', data)
}

export function getBuyOrders(params) {
  return request.get('/orders/buy', { params })
}

export function getSellOrders(params) {
  return request.get('/orders/sell', { params })
}

export function getOrderById(id) {
  return request.get(`/orders/${id}`)
}

export function payOrder(id) {
  return request.put(`/orders/${id}/pay`)
}

export function finishOrder(id) {
  return request.put(`/orders/${id}/finish`)
}

export function cancelOrder(id) {
  return request.put(`/orders/${id}/cancel`)
}

export function getUsers(params) {
  return request.get('/admin/users', { params })
}

export function updateUserStatus(id, status) {
  return request.put(`/admin/users/${id}/status?status=${status}`)
}

export function getAdminProducts(params) {
  return request.get('/admin/products', { params })
}

export function deleteAdminProduct(id) {
  return request.delete(`/admin/products/${id}`)
}

export function getStats() {
  return request.get('/admin/stats')
}
