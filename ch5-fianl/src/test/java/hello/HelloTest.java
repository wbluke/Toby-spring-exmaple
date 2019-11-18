package hello;

import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HelloTest {

    @Test
    public void simpleProxy() {
        Hello proxiedHello = new HelloUpperCase(new HelloTarget());

        assertThat(proxiedHello.sayHello("Luke"), is("HELLO LUKE"));
        assertThat(proxiedHello.sayHi("Luke"), is("HI LUKE"));
        assertThat(proxiedHello.sayThankYou("Luke"), is("THANK YOU LUKE"));
    }

    @Test
    public void dynamicProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UpperCaseHandler(new HelloTarget()));

        assertThat(proxiedHello.sayHello("Luke"), is("HELLO LUKE"));
        assertThat(proxiedHello.sayHi("Luke"), is("HI LUKE"));
        assertThat(proxiedHello.sayThankYou("Luke"), is("THANK YOU LUKE"));
    }
}
