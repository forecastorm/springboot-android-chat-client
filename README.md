# springboot-android-chat-client

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Build Status](https://travis-ci.org/Fnil/springboot-android-chat-client.svg?branch=master)](https://travis-ci.org/Fnil/springboot-android-chat-client)

## What is it?

An adroid chat app backend in springboot. You can get the backend code [here](https://github.com/Fnil/springboot-android-chat-server). Supports user status, instant text messaging. 



<img src="https://github.com/Fnil/springboot-android-chat-client/blob/master/screenshot/demo1.gif?raw=true" alt="A screenshot illustratrating the UI of the app" width="150" style="display: inline; "/> <img src="https://github.com/Fnil/springboot-android-chat-client/blob/master/screenshot/demo2.gif?raw=true" alt="A screenshot illustratrating the UI of the app" width="150" style="display: inline; "/>

## Why?

Good starting template for anyone that is interested in building their own chat app. Fast, compact, highly-customizable.

## How to use?

Server is already running on AWS, just download the code and you should be able to access the server. See complete springboot backend code [here](https://github.com/Fnil/springboot-android-chat-server).

Change [Internet Address](https://github.com/Fnil/springboot-android-chat-client/blob/master/app/src/main/java/com/nil/client/config/InternetAddress.java) to your own internet address if you want to build your own. 

```java
package com.nil.client.config;
public class InternetAddress {
    
    public static final String webSocketAddress = "ws://ec2-13-59-82-93.us-east-2.compute.amazonaws.com:8080/my-ws/websocket";

    public static final String volleyUserPostAddress = "http://ec2-13-59-82-93.us-east-2.compute.amazonaws.com:8080/api/users/post";
}


```