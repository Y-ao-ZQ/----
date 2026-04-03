<template>
  <div class="home">
    <div class="categories">
      <el-tag
        v-for="cat in categories"
        :key="cat"
        :type="currentCategory === cat ? 'primary' : ''"
        effect="plain"
        class="category-tag"
        @click="selectCategory(cat)"
      >
        {{ cat }}
      </el-tag>
      <el-tag
        :type="currentCategory === '' ? 'primary' : ''"
        effect="plain"
        class="category-tag"
        @click="selectCategory('')"
      >
        全部
      </el-tag>
    </div>
    
    <div class="product-list">
      <div v-for="product in products" :key="product.id" class="product-card">
        <div class="product-image" @click="goToDetail(product.id)">
          <el-image
            :src="product.images ? product.images.split(',')[0] : ''"
            fit="cover"
            class="product-img"
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </div>
        
        <div class="product-info">
          <div class="product-title" @click="goToDetail(product.id)">{{ product.title }}</div>
          <div class="product-price">￥{{ product.price }}</div>
          <div class="product-meta">
            <span>{{ product.sellerName }}</span>
            <span>{{ product.location }}</span>
          </div>
          <div class="product-footer">
            <span class="view-count">
              <el-icon><View /></el-icon>
              {{ product.viewCount }}
            </span>
            <el-button size="small" type="primary" @click="goToDetail(product.id)">
              查看详情
            </el-button>
          </div>
        </div>
      </div>
    </div>
    
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="loadProducts"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProducts, getCategories } from '@/api'

const route = useRoute()
const router = useRouter()

const categories = ref([])
const currentCategory = ref('')
const products = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadCategories = async () => {
  try {
    const res = await getCategories()
    categories.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const loadProducts = async () => {
  try {
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value
    }
    
    if (currentCategory.value) {
      params.category = currentCategory.value
    }
    
    if (route.query.keyword) {
      params.keyword = route.query.keyword
    }
    
    const res = await getProducts(params)
    const data = res.data
    products.value = data.content || data.elements || []
    total.value = data.totalElements || 0
  } catch (e) {
    console.error(e)
  }
}

const selectCategory = (category) => {
  currentCategory.value = category
  currentPage.value = 1
  loadProducts()
}

const goToDetail = (id) => {
  router.push(`/product/${id}`)
}

watch(() => route.query.keyword, () => {
  currentPage.value = 1
  loadProducts()
})

onMounted(() => {
  loadCategories()
  loadProducts()
})
</script>

<style scoped>
.home {
  min-height: 500px;
}

.categories {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.category-tag {
  cursor: pointer;
  padding: 8px 16px;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.product-image {
  height: 200px;
  overflow: hidden;
  background: #f5f5f5;
}

.product-img {
  width: 100%;
  height: 100%;
}

.image-error {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-size: 40px;
  color: #ccc;
}

.product-info {
  padding: 15px;
}

.product-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: bold;
  margin-bottom: 10px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #999;
  margin-bottom: 10px;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.view-count {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #999;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>
