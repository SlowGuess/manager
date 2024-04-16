<template>
  <div>
<!--    <button @click="invokeSpringBoot">Invoke Spring Boot</button>-->
    <div>{{ springBootResponse }}</div>
    <iframe src="http://127.0.0.1:5000/" width="100%" height="600px" frameborder="0"></iframe>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      springBootResponse: ''
    };
  },
  methods: {
    invokeSpringBoot() {
      console.log('Button clicked');
      // Spring Boot 服务的 URL
      let springBootUrl = 'http://localhost:8080/invoke-flask';

      // 发送 POST 请求到 Spring Boot
      axios.post(springBootUrl, {})
          .then(response => {
            // 将 Spring Boot 响应显示在页面上
            this.springBootResponse = response.data;
          })
          .catch(error => {
            console.error('Error invoking Spring Boot:', error);
          });
    }
  }
};
</script>
