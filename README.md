## amazon-fps-spring


Amazon FPS Spring support.

So the Amazon people missed a little bit on the Flexible Payment Service API (FPS).  This project helps to fix of some
of the problems they made; mainly how it's configured.  I'll write some more docs later.  
Just initializing the project at the moment.

## Dependencies
I mavenized their FPS project.  The original was an ANT build.  Since we're in the 21st century I thought stepping up to
maven was a good idea.  You should grab it first:
`git clone git://github.com/aweiland/amazon-fps.git`

## Configuration
The properties file configuration names that were defined by Amazon were not "namespaced" the way you would typically 
expect to see them.  The main (only) Spring bean in this project does a little conversion from one name to another.

Here is a list of configuration parameters

|Project Param|Amazon Param|What is it?|
|-------------|------------|-----------|

> Have you figured out that I haven't finished this part yet?!!!?????!?!?!?!

## Example

If you are using a java/annotation based configuration (and you should, it's awesome) initialize the bean this way:

### Java Spring Configuration
```java

@Configuration
@PropertySource({ "classpath:amazonfps.properties" })
public class SomeConfigClass {

  @Autowired
  private Environment environment;

  @Bean
  public AmazonFpsInterceptorBean amazonFpsInterceptorBean() {
    return new AmazonFpsInterceptorBean(this.environment);
  }
  
}
```

### Properties file:

Assuming *amazonfps.properties* from above:

```
aws.fps.accessKeyId=1234567890
aws.fps.secretKeyId=qwertyuiop
aws.fps.endPoint=https://fps.sandbox.amazonaws.com
aws.cbui.endPoint=https://authorize.payments-sandbox.amazon.com/cobranded-ui/actions/start
```
