package org.idempiere.listbox.group.example;

import org.adempiere.webui.factory.AnnotationBasedFormFactory;
import org.adempiere.webui.factory.IFormFactory;
import org.osgi.service.component.annotations.Component;

@Component(service = IFormFactory.class, immediate = true)
public class MyFormFactory extends AnnotationBasedFormFactory {

	public MyFormFactory() {
	}

	@Override
	protected String[] getPackages() {
		return new String[] {"org.idempiere.listbox.group.example"};
	}

}
