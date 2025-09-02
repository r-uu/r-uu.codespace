# high level system view

Let's start with a very high level system view on jeeeraaah:

<div hidden>
```
@startuml high level view on jeeeraaah system

skinparam linetype ortho

node client [
jeeeraaah
---
client
]
node server [
jeeeraaah
---
server
]
client -r- server : messages

@enduml
```
</div>

At no surprise jeeeeraaah as overall system is distributed in a client server manner. Notice that client and server communicate bidirectional.


[back](../../../readme.md)