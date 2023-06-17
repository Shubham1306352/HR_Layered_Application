package com.thinking.machines.network.server;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import java.net.*; 
import java.io.*;  

class RequestProcessor extends Thread
{
private RequestHandlerInterface requestHandler;
private Socket socket;
public RequestProcessor(Socket socket,RequestHandlerInterface requestHandler) 
{
this.socket=socket;
this.requestHandler=requestHandler; 
start();
}//constructor ends

public void run()
{
try
{
InputStream is=socket.getInputStream();
OutputStream os=socket.getOutputStream();
int bytesToReceive=1024;
byte tmp[]=new byte[1024];
byte header[]=new byte[1024];
int bytesReadCount;
int i,j,k;
i=0;
j=0;

//receiving header

while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
header[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}

//getting the request Length from header 

int requestLength=0;
j=1023;
i=1;
while(j>=0)
{
requestLength=requestLength+(header[j]*i);
i=i*10;
j--;
}

//sending ack.

byte ack[]=new byte[1];
ack[0]=1;
os.write(ack,0,1);
os.flush();

//receiving request in form of byte array

byte requestBytes[]=new byte[requestLength];
bytesToReceive=requestLength;
i=0;
j=0;
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
requestBytes[i]=tmp[k]; 
i++;
}
j=j+bytesReadCount;
}

//deSerializing the data from the byte array

ByteArrayInputStream bais=new ByteArrayInputStream(requestBytes);
ObjectInputStream ois=new ObjectInputStream(bais);
Request request=(Request)ois.readObject();

//getting response from requestHandler
Response response=requestHandler.process(request);

// converting the response string into byte array(serializing)

ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(response);
oos.flush();
byte objectBytes[]=baos.toByteArray();
//creating header of response length ("Data Saved")
int responseLength=objectBytes.length;
int x;
i=1023;
x=responseLength;
header=new byte[1024];
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i--;
}
//sending header
os.write(header,0,1024); // from which index , how many 
os.flush();

//getting the ack.
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break; 
}
//sending byte array of response string in chunks of 1024 
int bytesToSend=responseLength;
int chunkSize=1024;
j=0;
while(j<bytesToSend)
{
if((bytesToSend-j)<chunkSize) chunkSize=bytesToSend-j;
os.write(objectBytes,j,chunkSize);
os.flush();
j=j+chunkSize;
}
//getting ack.
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break; 
}
socket.close();

}catch(Exception e)
{
System.out.println(e);
}  
}//funtion ends

}//class ends
