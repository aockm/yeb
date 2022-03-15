



## 进度：





### 1、创建项目



```shell
vue create yeb
```





选择手动配置

![image-20220110133200881](E:\blog\yeb\assets\yeb前端\image-20220110133200881.png)



进行配置（按空格选择，上下键移动，回车确定）

![image-20220110133302665](E:\blog\yeb\assets\yeb前端\image-20220110133302665.png)

选择vue2.x版本

![image-20220110133344959](E:\blog\yeb\assets\yeb前端\image-20220110133344959.png)

![image-20220110133501938](E:\blog\yeb\assets\yeb前端\image-20220110133501938.png)

![image-20220110133550500](E:\blog\yeb\assets\yeb前端\image-20220110133550500.png)

![image-20220110133627691](E:\blog\yeb\assets\yeb前端\image-20220110133627691.png)





### 2、编写登录页



#### 安装axios

```shell
npm install axios
```

#### 请求拦截器

```js
//请求拦截器
axios.interceptors.request.use(config => {
    let token = window.sessionStorage.getItem('tokenStr')
    //如果存在token，请求携带token
    if (token){
        config.headers['Authorization'] = token;
    }
    return config;
},error => {
    console.log(error);
});
```



#### 响应拦截器

```js
//响应拦截器
axios.interceptors.response.use(success => {

    //业务逻辑错误
    if (success.status && success.status==200){
        if (success.data.code==500||success.data.code==401||success.data.code==403){
            Message.error({message:success.data.message});
            return;
        }
        if (success.data.message){
            Message.success({message:success.data.message});
        }
    }
    return success.data;
}, error => {
    if (error.response.code==504||error.response.code==404){
        Message.error({message:'服务器出错了！'});
    }else if (error.response.code==403){
        Message.error({message:'权限不足'});
    }else if (error.response.code==401){
        Message.error({message: '尚未登录，请先登录！'});
        router.replace('/');
    }else{
        if (error.response.data.message){
            Message.error({message:error.response.data.message});
        }else{
            Message.error({message:'未知错误！'});
        }
    }
    return;
})
```

#### 登录

```vue
<template>
  <div>
    <el-form :model="ruleForm" ref="ruleForm" :rules="rules" class="loginContainer"
        v-loading="loading"
        element-loading-text="正在登录..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)">
      <h3 class="loginTitle">登录</h3>
      <el-form-item prop="username">
        <el-input type="text" v-model="ruleForm.username" autocomplete="off" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="ruleForm.password" autocomplete="off" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input size="normal" type="text" v-model="ruleForm.code"
                  auto-complete="false"
                  placeholder="点击图片更换验证码" style="width:250px;margin-right: 5px"></el-input>
        <img :src="captchaUrl" @click="updateCaptcha"/>
      </el-form-item>
      <!--<el-checkbox class="loginRemember" v-model="checked">记住我</el-checkbox>-->
      <el-button type="primary" style="width: 100%" @click="submitLogin">登录</el-button>
    </el-form>
  </div>
</template>

<script>


export default {
  name: "Login",
  data() {
    return {
      captchaUrl: '/captcha?time=' + new Date(),
      ruleForm: {
        username: 'admin',
        password: '123',
        code: ''
      },
      checked: true,
      loading:false,
      rules: {
        username: [{
          required: true, message: '请输入用户名', trigger:
              'blur'
        }],
        password: [{
          required: true, message: '请输入密码', trigger:
              'blur'
        }],
        code: [{
          required: true, message: '请输入验证码', trigger:
              'blur'
        }]
      }
    }
  },
  methods: {
    //登录
    submitLogin() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          this.postRequest('/login', this.ruleForm).then(resp => {
            if (resp) {
              //alert(JSON.stringify(resp));
              this.loading = false;
              //存储用户token
              const tokenStr = resp.obj.tokenHead + resp.obj.token;
              window.sessionStorage.setItem('tokenStr', tokenStr);
              //清空菜单
              //this.$store.commit('initRoutes', []);
              //页面跳转
              //let path = this.$route.query.redirect;
              //this.$router.replace((path == '/' || path == undefined) ? '/home' : path)
              this.$router.replace(  '/home');
            }
          })
        } else {
          this.$message.error('请输入所有字段');
          return false;
        }
      })
    },
    updateCaptcha() {
      //验证码
      this.captchaUrl = '/captcha?time=' + new Date();
    }
  }
}
</script>

<style>
.loginContainer {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 15px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.loginTitle {
  margin: 0 auto 40px auto;
  text-align: center;
  color: #505458;
}

/*.loginRemember {
  text-align: left;
  margin: 0px 0px 15px 0px;
}*/

.el-form-item__content {
  display: flex;
  align-items: center;
}
</style>
```



### 3、编写首页



P11  0.0

Home

```vue
<template>
  <div>
    <el-container>
      <el-header>Header</el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu router unique-opened>
            <el-submenu :index="index" v-for="(item,index) in routes" :key="index" v-if="!item.hidden">
              <template slot="title">
                <i class="el-icon-location"></i><span>{{item.name}}</span>
              </template>
              <el-menu-item :index="children.path" v-for="(children,indexj) in item.children">
                {{children.name}}
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>


  </div>
</template>

<script>
export default {
  name: "Test1",
  computed:{
    routes() {
      return this.$store.state.routes;
    }
  }
}
</script>

<style scoped>

</style>
```





#### 安装vuex

```shell
npm install vuex --save
```















#### 菜单

```js
import {getRequest} from "./api";

export const initMenu = (router, store) => {
  if (store.state.routes.length > 0) {
    console.log("length");
    return;
  }
  console.log("Erressa")
  getRequest("/system/config/menu").then(data => {
    if (data) {
      //格式化router
      let fmtRoutes = formatRoutes(data);
      //添加到router
      router.addRoutes(fmtRoutes);
      //将数据存入vuex
      store.commit('initRoutes', fmtRoutes);
      //连接websocket
      //store.dispatch('connect');
    }
    console.log("ASD1")
  })
}

export const formatRoutes = (routes) => {
  let fmtRoutes = [];
  routes.forEach(router => {
    let {
      path,
      component,
      name,
      //meta,
      iconCls,
      children,
    } = router;
    if (children && children instanceof Array) {
      //递归
      children = formatRoutes(children);
    }
    let fmRouter = {
      path: path,
      name: name,
      //meta: meta,
      iconCls: iconCls,
      children: children,
      component(resolve) {
        if (component.startsWith("Home")) {
          require(['../views/' + component + '.vue'], resolve);
        } else if (component.startsWith("Emp")) {
          require(['../views/emp/' + component + '.vue'], resolve);
        } else if (component.startsWith("Per")) {
          require(['../views/per/' + component + '.vue'], resolve);
        } else if (component.startsWith("Sal")) {
          require(['../views/sal/' + component + '.vue'], resolve);
        } else if (component.startsWith("Sta")) {
          require(['../views/sta/' + component + '.vue'], resolve);
        } else if (component.startsWith("Sys")) {
          require(['../views/sys/' + component + '.vue'], resolve);
        }
      }
    }
    fmtRoutes.push(fmRouter);
  });
  return fmtRoutes;
}


```





#### 图标依赖安装

```shell
npm install font-awesome
```



### 菜单工具

#### menu.js

==修改 router.addRoutes()为  router.addRoute()==

```javascript
```





### 基本信息也编写

```js
this.$confirm('此操作将永久删除['+data.name+']职位, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteRequest('/system/basic/pos/'+data.id).then(resp => {
            if (resp){
              this.initPositions();
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
```



##### posMana

```vue
<template>
    <div>
      <div>
        <el-input
            placeholder="添加职位"
            suffix-icon="el-icon-plus"
            v-model="pos.name"
            @keydown.enter.native="addPosition"
            size="small"
            class="addPosInput">
        </el-input>
        <el-button size="small" icon="el-icon-plus" type="primary" @click="addPosition">添加</el-button>
      </div>
      <div class="posMana">
        <el-table
            :data="positions"
            border
            style="width: 70%">
          <el-table-column
              type="selection"
              width="55">
          </el-table-column>
          <el-table-column
              prop="id"
              label="编号"
              width="55">
          </el-table-column>
          <el-table-column
              prop="name"
              label="职位"
              width="120">
          </el-table-column>
          <el-table-column
              prop="createDate"
              label="创建时间"
              width="200">
          </el-table-column>
          <el-table-column
              prop="enabled"
              label="是否启用"
              width="120">
          </el-table-column>
          <el-table-column
              label="操作"
              width="160">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
              <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
</template>

<script>
  import {deleteRequest} from "@/utils/api";

  export default {
    name: "PosMana",
    data() {
      return {
        pos: {
          name: ''
        },
        positions: []
      }
    },
    mounted() {

      this.initPositions();
    },
    methods: {
      addPosition(){
        if (this.pos.name){
          this.postRequest('/system/basic/pos/',this.pos).then(resp => {
            if (resp){
              this.initPositions();
              this.pos.name = '';
            }
          })
        }else {
          this.$message.error("职位名称不能为空")
        }
      },
      handleEdit(index,data){

      },
      handleDelete(index,data){
        //删除职位
        this.$confirm('此操作将永久删除该【' + data.name + '】职位, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteRequest('/system/basic/pos/' + data.id).then(resp => {
            if (resp) {
              this.initPositions();
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      initPositions(){
        this.getRequest('/system/basic/pos/').then(resp => {
          if (resp){
            this.positions = resp;
          }
        })
      }
    }
  }
</script>

<style>
  .addPosInput{
    width: 300px;
    margin-right: 8px;
  }
  .posMana {
    margin-top: 10px;
  }

</style>
```



##### 32

```javascript
this.axios({
          method: 'delete',
          url: '/system/basic/pos/'+data.id,
        }).then(function (resp){
          this.initPositions();
        })
```





2022_1_27

### 员工基本信息页



```vue
<template>
    <div>
      <div style="display: flex;justify-content: space-between">
          <div>
            <el-input prefix-icon="el-icon-search" placeholder="请输入员工名进行搜索"
                      v-model="empName"
                      clearable
                      @clear="initEmps"
                      @keydown.enter.native="initEmps"
                      style="width: 300px;margin-right: 10px"> </el-input>
            <el-button type="primary" icon="el-icon-search" @click="initEmps">搜索</el-button>
            <el-button type="primary">
              <i class="fa fa-angle-double-down" aria-hidden="true"></i>
              高级搜索
            </el-button>
          </div>
          <div>
            <el-button type="success">
              <i class="fa fa-level-up" aria-hidden="true"></i>
              导入数据
            </el-button>
            <el-button type="success">
              <i class="fa fa-level-down" aria-hidden="true"></i>
              导出数据
            </el-button>
            <el-button type="primary" icon="el-icon-plus" @click="showEmpView">添加员工</el-button>
          </div>
      </div>
      <div style="margin-top: 10px">
        <el-table
            :data="emps"
            stripe
            border
            style="width: 100%">
          <el-table-column
              type="selection"
              width="55">
          </el-table-column>
          <el-table-column
              prop="name"
              label="姓名"
              fixed="left"
              align="left"
              width="80" height="30">
          </el-table-column>
          <el-table-column
              prop="workId"
              label="工号"
              align="left"
              width="85">
          </el-table-column>
          <el-table-column
              prop="gender"
              label="性别"
              align="left"
              width="50">
          </el-table-column>
          <el-table-column
              prop="birthday"
              label="出生日期"
              align="left"
              width="95">
          </el-table-column>
          <el-table-column
              prop="idCard"
              label="身份证号码"
              align="left"
              width="150">
          </el-table-column>
          <el-table-column
              prop="wedlock"
              label="婚姻状况"
              width="70">
          </el-table-column>
          <el-table-column
              prop="nation.name"
              label="民族"
              width="50">
          </el-table-column>
          <el-table-column
              prop="nativePlace"
              label="籍贯"
              align="left"
              width="150">
          </el-table-column>
          <el-table-column
              prop="politicsStatus.name"
              label="政治面貌"
              width="130">
          </el-table-column>
          <el-table-column
              prop="phone"
              label="电话号码"
              align="left"
              width="100">
          </el-table-column>
          <el-table-column
              prop="address"
              label="联系地址"
              align="left"
              width="295">
          </el-table-column>
          <el-table-column
              prop="department.name"
              label="部门"
              align="left"
              width="100">
          </el-table-column>
          <el-table-column
              prop="position.name"
              label="职位"
              align="left"
              width="100">
          </el-table-column>
          <el-table-column
              prop="engageForm"
              label="聘用形式"
              align="left"
              width="100">
          </el-table-column>
          <el-table-column
              prop="tiptopDegree"
              label="最高学历"
              align="left"
              width="80">
          </el-table-column>
          <el-table-column
              prop="school"
              label="毕业学校"
              align="left"
              width="150">
          </el-table-column>
          <el-table-column
              prop="contractTerm"
              label="合同期限"
              align="left"
              width="100">
            <template slot-scope="scope">
              <el-tag>{{scope.row.contractTerm}}</el-tag>年
            </template>
          </el-table-column>
          <el-table-column
              prop="beginContract"
              label="合同截止日期"
              align="left"
              width="100">
          </el-table-column>
          <el-table-column
              label="操作"
              fixed="right"
              align="center"
              width="200">
            <template slot-scope="scope">
              <el-button style="padding: 3px" size="mini" >编辑</el-button>
              <el-button style="padding: 3px"  size="mini" type="primary">查看高级资料</el-button>
              <el-button style="padding: 3px"  size="mini" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="display: flex;justify-content: flex-end">
          <el-pagination
              background
              @size-change="sizeChange"
              @current-change="currentChange"
              layout="sizes,prev, pager, next,jumper,->,total"
              :total="total">
          </el-pagination>
        </div>
      </div>
      <el-dialog
          title="添加员工"
          :visible.sync="dialogVisible"
          width="80%">
        <div>
          <el-form ref="empForm" :model="emp">
            <el-row>
              <el-col :span="6">
                <el-form-item label="姓名:" prop="name">
                  <el-input v-model="emp.name" style="width: 150px;" prefix-icon="el-icon-edit" size="mini" placeholder="请输入员工姓名"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="性别:" prop="gender">
                  <el-radio-group v-model="emp.gender">
                  <el-radio  label="1">男</el-radio>
                  <el-radio  label="2">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="出生日期:" prop="birthday">
                  <el-date-picker
                    v-model="emp.birthday"
                    value-formt="yyyy-MM-dd"
                    type="date"
                    size="mini"
                    style="width: 150px"
                    placeholder="出生日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="政治面貌:" prop="politicId">
                  <el-select v-model="emp.politicId" size="mini" style="width: 200px;" placeholder="政治面貌">
                    <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="民族:" prop="nationId">
                  <el-select v-model="emp.nationId" size="mini" style="width: 150px;" placeholder="民族">
                    <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="籍贯:" prop="nativePlace">
                  <el-input v-model="emp.nativePlace" placeholder="请输入籍贯" size="mini" prefix-icon="el-icon-edit" style="width: 120px"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6"></el-col>
              <el-col :span="7"></el-col>
            </el-row>
          </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
      </el-dialog>
    </div>
</template>

<script>
export default {
  name: "EmpBasic",
  data(){
    return{
      emps: [],
      total: 0,
      currentPage: 1,
      size: 10,
      empName: '',
      dialogVisible: false,
      emp:{
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: null,
        nativePlace: '',
        politicId: null,
        email: '',
        phone: '',
        address: '',
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '',
        workId: null,
        contractTerm: null,
        conversionTime: '',
        notworkDate: null,
        beginContract: '',
        endContract: '',
        workAge: null,
        salaryId: null
      },
    }
  },
  mounted() {
    this.initEmps();
  },
  methods:{
    showEmpView(){
      this.dialogVisible = true;
    },
    sizeChange(size){
      this.size = size;
      this.initEmps();
    },
    currentChange(currentPage){
      this.currentPage = currentPage;
      this.initEmps();


    },
    initEmps(){
      this.getRequest('/employee/basic/?currentPage='+this.currentPage+'&size='+this.size+'&name='+this.empName).then(resp => {
        if (resp){
          this.emps = resp.data;
          this.total = resp.total;
        }
      })
    },
  }
}
</script>

<style scoped>
    .el-table__header tr,
    .el-table__header th {
      padding: 0;
      height: 40px;
    }
    .el-table__body tr,
    .el-table__body td {
      padding: 0;
      height: 40px;
    }
    .el-table__row{
      height: 40px;
    }
</style>
```



> 2022_1_30

##### 5



### 文件下载



安装依赖

```shell
npm install js-file-download
```







### 聊天室

下载GitHub上的聊天室项目(vue-chat)

项目链接：https://github.com/Coffcer/vue-chat

在issues中找到项目作品重构（https://github.com/is-liyiwei/vue-Chat-demo）

下载此项目

```shell
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build
```





安装sass依赖

```shell
npm install sass-loader --save-dev
```

安装node-sass依赖

```shell 
npm install node-sass --save-dev
```



```js
###  store/index.js

import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex)

const now = new Date();

const store = new Vuex.Store({
    state: {
        routes: [],
        sessions:[{
            id:1,
            user:{
                name:'示例介绍',
                img:'../src/assets/images/2.png'
            },
            messages:[{
                content:'Hello，这是一个基于Vue + Vuex + Webpack构建的简单chat示例，聊天记录保存在localStorge, 有什么问题可以通过Github Issue问我。',
                date:now
            },{
                content:'项目地址(原作者): https://github.com/coffcer/vue-chat',
                date:now
            },{
                content:'本项目地址(重构): https://github.com/is-liyiwei',
                date:now
            }]
        },{
            id:2,
            user:{
                name:'webpack',
                img:'../src/assets/images/3.jpg'
            },
            messages:[{
                content:'Hi，我是webpack哦',
                date:now
            }]
        }],
        currentSessionId:1,
        filterKey:''
    },
    mutations: {
        initRoutes(state,data){
            state.routes = data;
        },
        changeCurrentSessionId (state,id) {
            state.currentSessionId = id;
        },
        addMessage (state,msg) {
            state.sessions[state.currentSessionId-1].messages.push({
                content:msg,
                date: new Date(),
                self:true
            })
        },
        INIT_DATA (state) {
            let data = localStorage.getItem('vue-chat-session');
            //console.log(data)
            if (data) {
                state.sessions = JSON.parse(data);
            }
        }
    },
    actions:{
        initData (context) {
            context.commit('INIT_DATA')
        }
    }
})

store.watch(function (state) {
    return state.sessions
},function (val) {
    console.log('CHANGE: ', val);
    localStorage.setItem('vue-chat-session', JSON.stringify(val));
},{
    deep:true/*这个貌似是开启watch监测的判断,官方说明也比较模糊*/
})


export default store;
```



<img src="E:\blog\yeb\assets\yeb前端\image-20220221210917226.png" alt="image-20220221210917226" style="zoom:50%;" />



### 个人中心

因为后端有些问题，个人中心还没有完善



 ==2022_2_21==

至此结束！！！



>  2022-2-21视频观看完毕了()

![image-20220221211005257](E:\blog\yeb\assets\yeb前端\image-20220221211005257.png)
