<template>
  <router-view />
</template>

<script setup>
</script>

<style>
:root {
  --primary-color: #00b894;
  --primary-light: #55efc4;
  --primary-dark: #00a383;
  --secondary-color: #0984e3;
  --accent-color: #fd79a8;
  --warning-color: #fdcb6e;
  --danger-color: #ff7675;
  --success-color: #00b894;
  --text-primary: #2d3436;
  --text-secondary: #636e72;
  --text-muted: #b2bec3;
  --bg-primary: #ffffff;
  --bg-secondary: #f8f9fa;
  --bg-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  --shadow-sm: 0 2px 4px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 12px rgba(0, 0, 0, 0.08);
  --shadow-lg: 0 8px 24px rgba(0, 0, 0, 0.12);
  --shadow-xl: 0 12px 40px rgba(0, 0, 0, 0.15);
  --radius-sm: 8px;
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-xl: 24px;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  background: linear-gradient(180deg, #f8f9fa 0%, #e9ecef 100%);
  color: var(--text-primary);
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(180deg, #f8f9fa 0%, #e9ecef 100%);
}

::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: rgba(0, 184, 148, 0.3);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 184, 148, 0.5);
}

.el-button--primary {
  --el-button-color: #ffffff;
  --el-button-bg-color: var(--primary-color);
  --el-button-border-color: var(--primary-color);
  --el-button-hover-color: #ffffff;
  --el-button-hover-bg-color: var(--primary-dark);
  --el-button-active-color: #ffffff;
  --el-button-active-border-color: var(--primary-dark);
}

.el-tag--primary {
  --el-tag-bg-color: rgba(0, 184, 148, 0.1);
  --el-tag-border-color: rgba(0, 184, 148, 0.2);
  --el-tag-color: var(--primary-color);
}
</style>
