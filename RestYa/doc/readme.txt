Generic REST Android client

This is a prototype design for an Android REAST client, with a specific implementation for Twitter search (simple query only)

To add more services from a different vendor (i.e facebook) developer should:

- Add the service definition to RestListDef.java
- Provide vendor specific implementation under code.java.listhem.providers

TODO:
- Provide simple cache mechanism in RestProvider.java
- Create Guice base factory for the services injection


Enjoy.





