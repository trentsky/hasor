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
package net.test.simple.core._06_dependency.forced;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.Module;
/**
 * 强依赖演示，依赖关系：
 * Mode1
 *    Mode2
 *       Mode4    <- err
 *          Mode3
 *    Mode3
 * @version : 2013-7-27
 * @author 赵永春 (zyc@hasor.net)
 */
public class ForcedMode4 implements Module {
    public void init(ApiBinder apiBinder) throws Throwable {
        /*强依赖，当前模块的启动必须依靠目标模块*/
        apiBinder.configModule().mandatory(ForcedMode3.class);
        throw new Exception();//模拟init失败抛出异常
    }
    public void start(AppContext appContext) {
        System.out.println("Mode4 start!");
    }
}