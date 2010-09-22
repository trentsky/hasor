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
package org.more.beans.resource.namespace;
import java.util.Map;
import org.more.beans.define.PropertyDefine;
import org.more.beans.define.TemplateBeanDefine;
import org.more.core.xml.stream.EndElementEvent;
import org.more.util.attribute.StackDecorator;
/**
 * ���ڽ���property��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Property extends TagBeans_AbstractPropertyDefine {
    /**����{@link PropertyDefine}����*/
    protected Object createDefine(StackDecorator context) {
        return new PropertyDefine();
    }
    /**�������Ա�ǩ���е�����*/
    public enum PropertyKey {
        name, boolLazyInit
    };
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        Map<Enum<?>, String> propertys = super.getPropertyMappings();
        propertys.put(PropertyKey.boolLazyInit, "lazy");
        propertys.put(PropertyKey.name, "name");
        return propertys;
    }
    /**������ע�ᵽBean�С�*/
    public void endElement(StackDecorator context, String xpath, EndElementEvent event) {
        PropertyDefine property = (PropertyDefine) this.getDefine(context);
        TemplateBeanDefine define = (TemplateBeanDefine) context.getAttribute(TagBeans_TemplateBean.BeanDefine);
        define.addProperty(property);
        super.endElement(context, xpath, event);
    }
}