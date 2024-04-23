<template>
  <div>
    <div class="front-notice"><i class="el-icon-bell" style="margin-right: 2px"></i>公告：{{ top }}</div>
    <!--头部-->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">杏林留言</div>
      </div>
      <div class="front-header-center">
        <div class="front-header-nav">
          <el-menu :default-active="$route.path" mode="horizontal" router>
						<el-menu-item index="/front/home">首页</el-menu-item>
						<el-menu-item index="/front/person">个人中心</el-menu-item>
            <el-menu-item index="/home">返回系统</el-menu-item>
          </el-menu>
        </div>
      </div>

      <div>
        <el-input style="width: 260px; margin-right: 10px" placeholder="请输入关键字搜索" v-model="title" clearable></el-input>
        <el-button type="success" @click="goSearch">搜 索</el-button>
      </div>

      <div class="front-header-right">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="front-header-dropdown">
              <img :src="user.avatar" alt="">
              <div style="margin-left: 10px;">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div style="text-decoration: none" @click="logout">退出</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!--主体-->
    <div class="main-body">
      <router-view ref="child" @update:user="updateUser" />
    </div>
  </div>

</template>

<script>

export default {
  name: "FrontLayout",

  data () {
    return {
      top: '',
      notice: [],
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
      title:this.$route.query.title
    }
  },

  mounted() {
    this.loadNotice()
  },
  methods: {
    goSearch(){
      location.href = '/front/search?title=' + this.title
    },
    loadNotice() {
      this.$request.get('/notice/selectAll').then(res => {
        this.notice = res.data
        let i = 0
        if (this.notice && this.notice.length) {
          this.top = this.notice[0].content
          setInterval(() => {
            this.top = this.notice[i].content
            i++
            if (i === this.notice.length) {
              i = 0
            }
          }, 2500)
        }
      })
    },
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    // 退出登录
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
  }

}
</script>

<style scoped>
body {
  background-color: #e4e6eb;
}

.front-notice {
  padding: 5px 20px;
  color: #666;
  font-size: 12px;
  background-color: #fff;
}
.front-header {
  display: flex;
  height: 60px;
  line-height: 60px;
  border-bottom: 1px solid #eee;
  background: #0066bc;
}
.front-header-left {
  width: 300px;
  display: flex;
  align-items: center;
  padding-left: 30px;
}
.front-header-left img, .front-header-dropdown img {
  width: 40px;
  height: 40px;
  border-radius: 50%
}
.front-header-left .title {
  flex: 1;
  color: #fff;
  cursor: pointer;
  margin-left: 10px;
  font-size: 20px;
  font-weight: bold;
}
.front-header-center {
  flex: 1;
}
.front-header-right {
  width: 200px;
  padding-right: 20px;
  text-align: right;
}
.front-header-dropdown {
  display: flex;
  align-items: center;
  justify-content: right;
  color: #ffffff;

}
.el-dropdown-menu {
  width: 100px !important;
  text-align: center !important;
}

/*页面具体样式自定义*/
.main-content {
  width: 80%;
  margin: 5px auto;
}
/* ElementUI 样式覆盖 */
.el-menu.el-menu--horizontal {
  border: none !important;
  height: 59px;
}
.el-menu {
  background-color: #0066bc;
}
.el-menu-item.is-active {
  color: #fff !important;
  background-color: #1890ff !important;
  height: 59px !important;
}
.el-menu-item {
  color: #fff !important;
  border: none !important;
}
.el-menu-item:not(.is-active):hover {
  background-color: #59aeff !important;
}
</style>