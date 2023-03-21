### 支持在Java源代码目录中定义sql模板，便于快速定位
### 定义方式
### 1. 在模块包中声明sql模板文件
### 2. 在本文件中引入
### 以下示例以org模块为例

#namespace("busobject")
#include("busobject/busobject.sql")
#end

#namespace("userorg")
#include("userorg/userorg.sql")
#end

#namespace("buttonpermission")
#include("buttonpermission/buttonpermission.sql")
#end

#namespace("datapermission")
#include("datapermission/datapermission.sql")
#end

#namespace("emailreceiver")
#include("emailreceiver/emailreceiver.sql")
#end

#namespace("menupermission")
#include("menupermission/menupermission.sql")
#end

#namespace("forgeigncurrency")
#include("forgeigncurrency/forgeigncurrency.sql")
#end

#namespace("workregionm")
#include("workregionm/workregionm.sql")
#end

#namespace("person")
#include("person/person.sql")
#end