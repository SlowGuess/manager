<template>
  <div>
    <div class="search">
      <el-select v-model="departmentId" placeholder="请选择科室" style="width: 200px">
        <el-option v-for="item in departmentData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table" style="padding: 15px 20px">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in tableData" style="margin-bottom: 20px;">
          <div style="text-align: center; background-color: #ecf8fd;" class="card"  >
            <img :src="item.avatar" alt="" style="width: 100px; height: 100px; border-radius: 50%">
            <div style="font-weight: 550; margin-top: 10px">
              {{item.name}} <span style="color: #383535; margin-left: 5px; font-weight: 500">{{item.departmentName}}</span>
            </div>
            <div style="margin-top: 20px; color: #353523; padding: 0 10px; text-align: left; overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 4;">
              简介：{{item.description}}
            </div>
            <div style="margin-top: 15px">
              挂号费：<span style="color: red; font-weight: 550; margin-right: 20px">￥{{item.price}}</span> 剩余：{{item.num}}
            </div>
            <div style="margin-top: 15px">
              <el-button type="primary" size="mini" @click="reserve(item.id)">挂号</el-button>
            </div>
          </div>
        </el-col>
      </el-row>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Doctor",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      departmentId: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {},
      ids: [],
      departmentData: []
    }
  },
  created() {
    this.load(1)
    this.loadDepartment()
  },
  methods: {
    reserve(doctorId) {
      if (this.user.role !== 'USER'){
        this.$message.warning('您的角色不支持挂号操作')
        return
      }
      let data = {
        userId: this.user.id,
        doctorId: doctorId
      }
      this.$request.post('/reserve/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('挂号成功')
          this.load(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadDepartment() {
      this.$request.get('/department/selectAll').then(res => {
        if (res.code === '200') {
          this.departmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/doctor/selectPage2', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          departmentId: this.departmentId,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.departmentId = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
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
  color: #ccc;
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