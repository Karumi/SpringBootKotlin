# ![Karumi logo][karumilogo] Spring Boot Kotlin

[Spring boot][springboot] Kotlin example using some Spring features like Security, Actuator, JPA, Workers and MvcTests.

Application code example it's based on [Play Framework Kotlin repository][playframeworkkotlin] with some news endpoints 
to register and login developers, it uses in-memory database calls H2.


Running
================
```
./gradlew bootRun
```

By default the local port is `5000`, you can change it at the [application.properties][properties] file.

Usage
================
You can use [IntelliJ Http Client][httpclient] files to make some request to the local server [located here][httpfiles]. 
Run `Authentication.http` file before others to have credentials to make requests. 

You can also take a look to all endpoints in Swagger UI Web at http://localhost:5000/swagger-ui.html. 

Actuator
================
[Actuator][actuator] brings production-ready features to monitor our application, gathering metrics, traffic, database metrics, etc...
It uses HTTP endpoints to enable us to interact with it. 

We use [codemetric/spring-boot-admin][codemetric] library to provide an admin web interface using Actuator endpoints in a 
separate server, to run admin panel web: 

```
cd admin-server  
./gradlew bootRun
```

Which is listening at `http://localhost:8080/` with user `admin` and password `admin-password`.


License
-------

    Copyright 2018 Karumi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[karumilogo]: https://cloud.githubusercontent.com/assets/858090/11626547/e5a1dc66-9ce3-11e5-908d-537e07e82090.png
[codemetric]: https://github.com/codecentric/spring-boot-admin
[actuator]: https://www.baeldung.com/spring-boot-actuators
[properties]: https://github.com/Karumi/SpringBootKotlin/blob/master/src/main/resources/application.properties
[httpfiles]: https://github.com/Karumi/SpringBootKotlin/tree/master/src/main/requests
[httpclient]: https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html
[playframeworkkotlin]: https://github.com/Karumi/play-framework-kotlin
[springboot]: https://spring.io/projects/spring-boot
