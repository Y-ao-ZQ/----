import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import Layout from '@/components/Layout.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/Home.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/product/:id',
    component: Layout,
    children: [
      {
        path: '',
        name: 'ProductDetail',
        component: () => import('@/views/ProductDetail.vue')
      }
    ]
  },
  {
    path: '/publish',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Publish',
        component: () => import('@/views/Publish.vue')
      }
    ],
    meta: { requiresAuth: true }
  },
  {
    path: '/my',
    component: Layout,
    children: [
      {
        path: '',
        name: 'My',
        component: () => import('@/views/My.vue')
      }
    ],
    meta: { requiresAuth: true }
  },
  {
    path: '/favorites',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Favorites',
        component: () => import('@/views/Favorites.vue')
      }
    ],
    meta: { requiresAuth: true }
  },
  {
    path: '/messages',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Messages',
        component: () => import('@/views/Messages.vue')
      }
    ],
    meta: { requiresAuth: true }
  },
  {
    path: '/orders',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Orders',
        component: () => import('@/views/Orders.vue')
      }
    ],
    meta: { requiresAuth: true }
  },
  {
    path: '/profile',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Profile',
        component: () => import('@/views/Profile.vue')
      }
    ],
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Admin',
        component: () => import('@/views/Admin.vue')
      }
    ],
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.token) {
    next('/login')
  } else if (to.meta.requiresAdmin && userStore.role !== 1) {
    next('/')
  } else {
    next()
  }
})

export default router
