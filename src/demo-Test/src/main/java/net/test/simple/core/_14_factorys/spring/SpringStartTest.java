/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.test.simple.core._14_factorys.spring;
import java.io.IOException;
import java.net.URISyntaxException;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.Hasor;
import net.hasor.core.Module;
import net.hasor.core.factorys.spring.SpringRegisterFactoryCreater;
import net.test.simple.core._03_beans.pojo.PojoBean;
import net.test.simple.core._03_beans.pojo.PojoInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
/**
 * 本示列演示如何启动 Hasor 框架。
 * @version : 2013-8-11
 * @author 赵永春 (zyc@hasor.net)
 */
public class SpringStartTest {
    @Test
    public void springStartTest() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("--->>springStartTest<<--");
        //1.创建一个标准的 Hasor 容器。
        AppContext appContext = Hasor.createAppContext(new SpringRegisterFactoryCreater(), new Module() {
            @Override
            public void loadModule(ApiBinder apiBinder) throws Throwable {
                PojoBean pojo1 = new PojoBean();
                pojo1.setName("马大帅");
                apiBinder.bindType(PojoBean.class).idWith("myBean1").nameWith("myBean").toInstance(pojo1);
                //
                PojoBean pojo2 = new PojoBean();
                pojo2.setName("王二勺");
                apiBinder.bindType(PojoInfo.class).idWith("myBean2").nameWith("myBean").toInstance(pojo2);
            }
        });
        //
        //
        PojoBean bean1 = null;
        PojoInfo bean2 = null;
        //
        bean1 = (PojoBean) appContext.findBindingBean("myBean", PojoBean.class);
        System.out.println(bean1.getName());
        bean2 = (PojoInfo) appContext.findBindingBean("myBean", PojoInfo.class);
        System.out.println(bean2.getName());
        //
        System.out.println("-- 注意 --");
        //
        ApplicationContext spring = appContext.getInstance(ApplicationContext.class);
        bean1 = (PojoBean) spring.getBean("myBean1");
        System.out.println(bean1.getName());
        bean2 = (PojoInfo) spring.getBean("myBean2");
        System.out.println(bean2.getName());
    }
}