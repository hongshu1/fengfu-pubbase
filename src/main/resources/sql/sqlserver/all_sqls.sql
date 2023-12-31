### 定义order排序处理规则
#define order()
#if(orderColumns?? && orderTypes??)
 #for(col:orderColumns)#(for.first?" order by ":"")#(col) #if(orderTypes!=null&&orderTypes.length>0)#(orderTypes[for.index])#end #(for.last?"":",")#end
#end
#end

### 定义sql的where条件处理规则
#define where()
#if(myparas)
 where 1=1 #for(myp:myparas) #((or??)?((for.index==0)?' and (':' or '):' and ') #(myp.key) #(customCompare?"":"=") #sqlValue(myp.value) #((for.index==for.size-1)?((or??)?")":""):"") #end #@order()
#else
 #@order()
#end
#end

#namespace("common")
#include("common.sql")
#end

#namespace("user.auth")
#include("user_auth.sql")
#end

#namespace("wechat")
#include("wechat.sql")
#end

#namespace("user")
#include("user.sql")
#end

#namespace("role")
#include("role.sql")
#end

#namespace("permission")
#include("permission.sql")
#end

#namespace("permissionbtn")
#include("permissionbtn.sql")
#end

#namespace("columsmap")
#include("columsmap.sql")
#end

#namespace("columsmapdetail")
#include("columsmapdetail.sql")
#end

#namespace("exchangetable.sql")
#include("exchangetable.sql")
#end

#namespace("printsetting")
#include("printsetting.sql")
#end

#namespace("vouchprocessnote")
#include("vouchprocessnote.sql")
#end

#namespace("organize")
#include("organize.sql")
#end

#namespace("userapp")
#include("userapp.sql")
#end

#namespace("openapi")
#include("openapi.sql")
#end

#namespace("todo")
#include("todo.sql")
#end