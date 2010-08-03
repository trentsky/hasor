/*
 * Copyright 2008-2009 the original author or authors.
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
package org.more.submit;
/**
 * 该接口定义了Submit默认支持的变量作用域。
 * @version : 2010-8-2
 * @author 赵永春(zyc@byshell.org)
 */
public interface ScopeEnum {
    /**ActionStack对象的父级范围*/
    public static final String Scope_Parent  = "Parent";
    /**ActionStack对象范围*/
    public static final String Scope_Stack   = "Stack";
    /**Session接口范围*/
    public static final String Scope_Session = "Session";
    /**SubmitContext接口范围*/
    public static final String Scope_Context = "Context";
};