<template>
  <el-dialog title="新增博客" :visible.sync="visible" width="60%" :close-on-click-modal="false" destroy-on-close>
    <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="标题"></el-input>
      </el-form-item>
      <el-form-item label="简介" prop="descr">
        <el-input type="textarea" v-model="form.descr" placeholder="简介"></el-input>
      </el-form-item>
      <el-form-item label="封面" prop="cover">
        <el-upload
            :action="$baseUrl + '/files/upload'"
            :headers="{ token: user.token }"
            list-type="picture"
            :on-success="handleCoverSuccess"
        >
          <el-button type="primary">上传封面</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="form.categoryId" style="width: 100%">
          <el-option v-for="item in categoryList" :key="item.id" :value="item.id" :label="item.name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标签" prop="tags">
        <el-select v-model="tagsArr" multiple filterable allow-create default-first-option style="width: 100%">
          <el-option value="内科"></el-option>
          <el-option value="外科"></el-option>
          <el-option value="肺炎"></el-option>
          <el-option value="健康"></el-option>
          <el-option value="术后注意"></el-option>
          <el-option value="预防措施"></el-option>
          <el-option value="神经科"></el-option>
          <el-option value="养生"></el-option>
          <el-option value="儿童"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <div id="editor"></div>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import E from "wangeditor";
import hljs from 'highlight.js';

export default {
  data() {
    return {
      visible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {},
      tagsArr: [],
      editor: null
    };
  },
  mounted() {
    this.setRichText();
  },
  methods: {
    handleCoverSuccess(res) {
      this.form.cover = res.data;
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.tags = JSON.stringify(this.tagsArr);
          this.form.content = this.editor.txt.html();
          this.$request({
            url: '/blog/add',
            method: 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功');
              this.visible = false;
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
    setRichText() {
      this.$nextTick(() => {
        this.editor = new E('#editor');
        this.editor.highlight = hljs;
        this.editor.config.uploadImgServer = this.$baseUrl + '/files/editor/upload';
        this.editor.config.uploadFileName = 'file';
        this.editor.config.uploadImgHeaders = {
          token: this.user.token
        };
        this.editor.config.uploadImgParams = {
          type: 'img',
        };
        this.editor.create();
      });
    }
  }
};
</script>

<style scoped>
/* 样式省略 */
</style>
