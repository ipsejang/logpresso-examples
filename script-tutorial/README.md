script-tutorial
==================

Logpresso Script Examples

Simple service example that writes logs. User can modify log message by logpresso script.

~~~
    araqne@bombom demo> bundle.install com.logpresso script-tutorial
    Resolving com.logpresso/script-tutorial
      -> trying to download from file:/home/everclear/.m2/repository
      bundle [80] loaded
    araqne@bombom demo> bundle.start 80
    bundle 80 started.
    araqne@bombom demo> samplescript.
    getMessage  setMessage  
    araqne@bombom demo> samplescript.getMessage
    Simple Service Message : dummy
    araqne@bombom demo> samplescript.setMessage
    Simple Service Message? logpresso sample message
    araqne@bombom demo> 
~~~
