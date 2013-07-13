Generic REST Android client

This is a prototype design for an Android REAST client, with a specific implementation for Twitter search (simple query only)

Dev Principles & Patterns: 
- service base using Gooogle Guice


To add more services from a different vendor (i.e facebook) developer should:

- Add the service definition to RestListDef.java
- Provide vendor specific implementation under code.java.restya.providers

TODO:
- Implement Twitter RestAuthenticationProvider (all in the activity now...)
- Create Oauth Activity to facilitate OauthWebView
- Improve OauthWebView for delay time user experience (loading ..)
- Provide simple cache mechanism in RestProvider.java
- Create Guice base factory for the services injection
- Many more ....


Enjoy.





