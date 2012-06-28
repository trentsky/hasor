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
package org.more.webui.components.page;
import java.io.IOException;
import java.io.Writer;
import org.more.webui.components.page.PageCom.Mode;
import org.more.webui.context.ViewContext;
import org.more.webui.render.Render;
import org.more.webui.render.UIRender;
import org.more.webui.tag.TemplateBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
/**
 * ������
 * @version : 2012-6-14
 * @author ������ (zyc@byshell.org)
 */
@UIRender(tagName = "ui_pNoDate")
public class NoDatePageRender implements Render<NoDatePageCom> {
    private boolean isRun(AbstractItemCom component) {
        PageCom page = (PageCom) component.getParent();
        return page.runMode == Mode.NoDate;
    }
    @Override
    public void beginRender(ViewContext viewContext, NoDatePageCom component, TemplateBody arg3, Writer writer) throws IOException, TemplateModelException {}
    @Override
    public void render(ViewContext viewContext, NoDatePageCom component, TemplateBody arg3, Writer writer) throws IOException, TemplateException {
        if (isRun(component) == false)
            return;
        arg3.render(writer);
    }
    @Override
    public void endRender(ViewContext viewContext, NoDatePageCom component, TemplateBody arg3, Writer writer) throws IOException {}
}