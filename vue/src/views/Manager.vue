<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" />
        <div class="title">智慧医疗系统</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <div>{{ user.name ||  '管理员' }}</div>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="goToPerson">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <el-menu :default-openeds="['info', 'reserve','user','blog','diagnosis']" router style="border: none" :default-active="$route.path">
          <el-menu-item index="/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>

          <el-submenu index="info" v-if="user.role !== 'USER' " >
            <template slot="title">
              <i class="el-icon-menu"></i><span>信息管理</span>
            </template>
            <el-menu-item index="/notice" v-if="user.role === 'ADMIN' " >公告信息</el-menu-item>
            <el-menu-item index="/department" v-if="user.role === 'ADMIN' ">科室信息</el-menu-item>
            <el-menu-item index="/plan">医生排班</el-menu-item>
          </el-submenu>

          <el-submenu index="reserve">
            <template slot="title">
              <i class="el-icon-menu"></i><span>预约就诊</span>
            </template>
            <el-menu-item index="/doctorCard">预约挂号</el-menu-item>
            <el-menu-item index="/reserve">患者挂号</el-menu-item>
            <el-menu-item index="/record">我的就诊</el-menu-item>
            <el-menu-item index="/registration" v-if="user.role !== 'DOCTOR' ">住院登记</el-menu-item>
          </el-submenu>

          <el-submenu index="user" v-if="user.role === 'ADMIN' ">
            <template slot="title">
              <i class="el-icon-menu"></i><span>用户管理</span>
            </template>
            <el-menu-item index="/admin">管理员信息</el-menu-item>
            <el-menu-item index="/doctor">医生信息</el-menu-item>
            <el-menu-item index="/user">患者信息</el-menu-item>
          </el-submenu>

          <el-submenu index="blog">
            <template slot="title">
              <i class="el-icon-menu"></i><span>病患交流</span>
            </template>
            <el-menu-item index="/category" v-if="user.role ==='ADMIN' ">留言分类</el-menu-item>
            <el-menu-item index="/blog" v-if="user.role === 'ADMIN' ">留言管理</el-menu-item>
            <el-menu-item index="/comment" v-if="user.role === 'ADMIN' ">评论管理</el-menu-item>
            <el-menu-item index="/front/home" >杏林留言</el-menu-item>
          </el-submenu>

          <el-submenu index="diagnosis">
            <template slot="title">
              <i class="el-icon-menu"></i><span>自助诊病</span>
            </template>
            <el-menu-item index="/diagnosis">AI诊断肺炎</el-menu-item>
          </el-submenu>


        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    if (!this.user.id) {
      this.$router.push('/login')
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    goToPerson() {
      if (this.user.role === 'ADMIN') {
        this.$router.push('/adminPerson')
      }
      if (this.user.role === 'DOCTOR') {
        this.$router.push('/doctorPerson')
      }
      if (this.user.role === 'USER') {
        this.$router.push('/userPerson')
      }
    },
    logout() {
      localStorage.removeItem('xm-user')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.manager-container {
  background-color: #f8f8ff;
  min-height: 100vh;
}
.manager-header {
  display: flex;
  background-color: white;
}
.manager-header-left {
  width: 240px;
  height: 50px;
  padding-left: 10px;
  display: flex;
  align-items: center
}
.manager-header-left img {
  width: 30px;
  height: 30px;
}
.manager-header-left .title {
  flex: 1;
  margin-left: 5px;
  font-size: 20px;
  font-weight: bold;
  color: #423f3f
}
.manager-header-center {
  flex: 1;
  width: 0;
  display: flex;
  align-items: center
}
.manager-header-right {
  width: 200px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
.manager-header-right .avatar {
  display: flex;
  align-items: center;
  padding-right: 10px;
  color: #000000;
  cursor: default
}
.manager-header-right .avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 5px
}
.manager-main {
  display: flex;
}

.manager-main-left {
  width: 200px;
  min-height: calc(100vh - 60px);
}

.manager-main-right {
  flex: 1;
  width: 0;
  padding: 10px;
}

.manager-main-right .search {
  padding: 10px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.1);
}

.manager-main-right .operation {
  margin-top: 5px;
  padding: 10px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.1)
}

.manager-main-right .table {
  margin-top: 5px;
  padding: 15px 10px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
}

.manager-main-right .table .pagination {
  margin-top: 10px;
}

.card {
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.1);
}

/* ElementUI */
.el-menu {
  width: 200px;
  height: 100%;
  background-color: white;
}

.el-submenu__title {
  height: 50px;
  line-height: 50px;
}

.el-menu-item {
  height: 50px;
  line-height: 50px;
  background-color: white !important; /* 必须加上这个背景色，否则鼠标离开会出现白色 */
}

.el-menu-item.is-active {
  /*background-color: #2bb7e5 !important;*/
  /*color: #fff;*/
}

.el-menu--inline .el-menu-item {
  padding-left: 50px !important;
}

.el-menu .is-active {
  color: #3399ff !important;
}

.el-submenu__title:hover {
  background-color: #f5f0f0 !important;
}

.el-menu-item:not(.is-active):hover {
  /*background-color: #0376bf !important;*/
  /*color: #fff;*/
}

.el-menu-item:hover {
  background-color: #f5f0f0 !important;
}

.el-submenu__icon-arrow {
  margin-top: -5px;
}

.el-dropdown-menu {
  margin-top: 20px !important;
}

.el-breadcrumb {
  margin-left: 10px;
}

th.el-table__cell {
  background-color: #f8f8f8 !important;
  color: #666;
}

</style>