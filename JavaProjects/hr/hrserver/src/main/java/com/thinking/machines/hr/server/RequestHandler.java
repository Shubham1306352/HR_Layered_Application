package com.thinking.machines.hr.server;
import com.thinking.machines.network.server.*;
import com.thinking.machines.network.common.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;

public class RequestHandler implements RequestHandlerInterface
{
private DesignationManagerInterface designationManager;

public RequestHandler()
{
try
{
designationManager=DesignationManager.getDesignationManager();
}catch(BLException blException)
{
//do nothing
}
}//constructor ends


public Response process(Request request)
{
Response response=new Response();
String manager=request.getManager();
String action=request.getAction();
Object [] arguments=request.getArguments();

if(manager.equals("DesignationManager"))
{
if(designationManager==null)
{
// 
}

if(action.equals("getDesignations"))
{
Object result=designationManager.getDesignations();
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
if(action.equals("addDesignation"))
{
try
{
DesignationInterface designation;
designation=(DesignationInterface)arguments[0];
designationManager.addDesignation(designation);
Object result=(Object)designation;
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}catch(BLException blException)
{
response.setSuccess(false);
response.setResult(null);
response.setException(blException);
return response;
}
}
if(action.equals("removeDesignation"))
{
try
{
int designationCode=(Integer)arguments[0];
designationManager.removeDesignation(designationCode);
Object result=null;
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}catch(BLException blException)
{
response.setSuccess(false);
response.setResult(null);
response.setException(blException);
return response;
}
}
if(action.equals("updateDesignation"))
{
try
{
DesignationInterface designation;
designation=(DesignationInterface)arguments[0];
designationManager.updateDesignation(designation);
Object result=null;
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}catch(BLException blException)
{
response.setSuccess(false);
response.setResult(null);
response.setException(blException);
return response;
}
}
if(action.equals("getDesignationCount"))
{
Integer count=designationManager.getDesignationCount();
Object result=count;
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
if(action.equals("getDesignationByCode"))
{
try
{
DesignationInterface designation;
int code=(Integer)arguments[0];
designation=designationManager.getDesignationByCode(code);
Object result=(Object)designation;
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}catch(BLException blException)
{
response.setSuccess(false);
response.setResult(null);
response.setException(blException);
return response;
}
}
if(action.equals("getDesignationByTitle"))
{
try
{
DesignationInterface designation;
String title=(String)arguments[0];
designation=designationManager.getDesignationByTitle(title);
Object result=(Object)designation;
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}catch(BLException blException)
{
response.setSuccess(false);
response.setResult(null);
response.setException(blException);
return response;
}
}
if(action.equals("designationCodeExists"))
{
Boolean exists; 
int code=(Integer)arguments[0];
exists=designationManager.designationCodeExists(code);
Object result=(Object)exists;
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
if(action.equals("designationTitleExists"))
{
Boolean exists; 
String title=(String)arguments[0];
exists=designationManager.designationTitleExists(title);
Object result=(Object)exists;
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}

}//DesignationManager part ends  
return response;
}//funtion ends 
}//class ends
