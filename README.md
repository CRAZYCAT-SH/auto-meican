# auto-meican
美餐提前预定神器

## 美餐MCP 服务器
```
url
http://localhost(替换为你的服务器地址):8080/sse

请求头
Content-Type=application/json
Authorization=Bearer xxx(替换为你的美餐账号)
```

## 点餐流程
1,配置账号
请求添加美餐账号接口: 
```
/api/meicanAccount/addAccount
```
参数示例
```json
{
  "accountName": "//美餐账号必填",
  "accountCookie": "//(cookie和password二选一即可)www.meican.com 获取的cookie",
  "accountPassword": "//(cookie和password二选一即可)美餐密码，"
}
```
2,添加预定任务
请求添加预定任务接口：
```
/api/meicanTask/addTask
```
参数示例
```json
{
  "accountName": "//美餐账号必填",
  "orderDate": "2022-10-08",
  "orderDish": "//预定的菜品名称，关键字匹配美餐菜单即可，不匹配会默认点获取到的第一个菜品"
}
```
