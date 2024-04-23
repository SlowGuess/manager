<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入挂号状态" style="width: 200px" v-model="status"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>


    <div class="table">
      <el-table :data="tableData" stripe >
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="患者姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="挂号时间"></el-table-column>
        <el-table-column prop="status" label="挂号状态"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope" >
            <el-button plain type="danger" size="mini" v-if="scope.row.status === '未叫号' && user.role ===  'USER'" @click=del(scope.row.id)>取消挂号</el-button>
            <el-button plain type="warning" size="mini" v-if="user.role === 'DOCTOR'"  @click=call(scope.row)>叫号</el-button>
          </template>
        </el-table-column>
      </el-table>

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
  name: "Reserve",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      status: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {},
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    call(row){
      let reserveData = JSON.parse(JSON.stringify(row));
      reserveData.status = '已叫号'
      this.$request.put('/reserve/update',reserveData).then(res =>{
        if (res.code === '200'){
          this.$message.success('叫号成功')
          this.load(1)
          //往就诊记录里同步一条数据
          this.record(row)
        }
      })
    },
    record(row){
      let data = {
        userId: row.userId,
        doctorId: row.doctorId,

      }
      this.$request.post('/record/add',data).then(res =>{
        if (res.code === '200'){
          this.$message.success('数据同步成功')
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定取消挂号吗？', '确定取消', {type: "warning"}).then(response => {
        this.$request.delete('/reserve/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/reserve/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          status: this.status,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.status = null
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
