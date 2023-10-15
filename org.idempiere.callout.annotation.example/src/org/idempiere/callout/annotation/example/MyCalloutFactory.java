/**
 * 
 */
package org.idempiere.callout.annotation.example;

import org.adempiere.base.AnnotationBasedColumnCalloutFactory;
import org.adempiere.base.IColumnCalloutFactory;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = IColumnCalloutFactory.class, property = {"service.ranking:Integer=-1"})
public class MyCalloutFactory extends AnnotationBasedColumnCalloutFactory {

	@Override
	protected String[] getPackages() {
		return new String[] {"org.idempiere.callout.annotation.example"};
	}

}
