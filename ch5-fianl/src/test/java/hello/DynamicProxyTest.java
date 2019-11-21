package hello;

import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DynamicProxyTest {

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

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UpperCaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("Luke"), is("HELLO LUKE"));
        assertThat(proxiedHello.sayHi("Luke"), is("HI LUKE"));
        assertThat(proxiedHello.sayThankYou("Luke"), is("THANK YOU LUKE"));
    }

    @Test
    public void pointCutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("Luke"), is("HELLO LUKE"));
        assertThat(proxiedHello.sayHi("Luke"), is("HI LUKE"));
        assertThat(proxiedHello.sayThankYou("Luke"), is("Thank You Luke"));
    }
}
